package com.josycom.mayorjay.flowoverstack.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.josycom.mayorjay.flowoverstack.R
import com.josycom.mayorjay.flowoverstack.ui.adapters.AnswerAdapter
import com.josycom.mayorjay.flowoverstack.databinding.ActivityAnswerBinding
import com.josycom.mayorjay.flowoverstack.ui.adapters.PagingLoadStateAdapter
import com.josycom.mayorjay.flowoverstack.util.AppConstants
import com.josycom.mayorjay.flowoverstack.util.AppUtils
import com.josycom.mayorjay.flowoverstack.ui.viewmodel.AnswerViewModel
import com.josycom.mayorjay.flowoverstack.ui.viewmodel.CustomAnswerViewModelFactory
import dagger.android.AndroidInjection
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import javax.inject.Inject

class AnswerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAnswerBinding
    private lateinit var answerViewModel: AnswerViewModel
    private var ownerQuestionLink: String? = null
    private var questionId = 0
    @Inject
    lateinit var viewModelFactory: CustomAnswerViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityAnswerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewContents(intent)
        viewModelFactory.setInputs(questionId,
        AppConstants.ORDER_DESCENDING,
        AppConstants.SORT_BY_ACTIVITY,
        AppConstants.SITE,
        AppConstants.ANSWER_FILTER,
        AppConstants.API_KEY)
        answerViewModel = ViewModelProvider(this, viewModelFactory).get(AnswerViewModel::class.java)

        fetchAndDisplayAnswers()
        setupListeners()
    }

    private fun setupViewContents(intent: Intent?) {
        var avatarAddress: String? = ""
        if (intent != null) {
            binding.apply {
                tvQuestionDetail.text = Jsoup.parse(intent.getStringExtra(AppConstants.EXTRA_QUESTION_TITLE)).text()
                markDownView.setMarkDownText(intent.getStringExtra(AppConstants.EXTRA_QUESTION_FULL_TEXT))
                tvDateQuestionDetail.text = intent.getStringExtra(AppConstants.EXTRA_QUESTION_DATE)
                tvNameQuestionDetail.text = intent.getStringExtra(AppConstants.EXTRA_QUESTION_NAME)
            }
            val voteCount = intent.getIntExtra(AppConstants.EXTRA_QUESTION_VOTES_COUNT, 0)
            if (voteCount <= 0) {
                binding.tvVotesCountItem.text = voteCount.toString()
            } else {
                binding.tvVotesCountItem.text = getString(R.string.plus_score, voteCount)
            }
            questionId = intent.getIntExtra(AppConstants.EXTRA_QUESTION_ID, 0)
            avatarAddress = intent.getStringExtra(AppConstants.EXTRA_AVATAR_ADDRESS)
            ownerQuestionLink = intent.getStringExtra(AppConstants.EXTRA_QUESTION_OWNER_LINK)
        }
        Glide.with(this)
                .load(avatarAddress)
                .placeholder(R.drawable.loading)
                .into(binding.ivAvatarQuestionDetail)
    }

    private fun setupListeners() {
        binding.apply {
            avatarCard.setOnClickListener {
                AppUtils.directLinkToBrowser(this@AnswerActivity, ownerQuestionLink)
            }
            tvNameQuestionDetail.setOnClickListener {
                AppUtils.directLinkToBrowser(this@AnswerActivity, ownerQuestionLink)
            }
        }
    }

    private fun fetchAndDisplayAnswers() {
        val answerAdapter = AnswerAdapter()
        binding.rvAnswers.apply {
            layoutManager = LinearLayoutManager(this@AnswerActivity)
            itemAnimator = DefaultItemAnimator()
            adapter = answerAdapter.withLoadStateFooter(PagingLoadStateAdapter { answerAdapter.retry() })
        }

        lifecycleScope.launch {
            answerAdapter.loadStateFlow.collect {
                binding.pbFetchData.visibility = if (it.source.refresh is LoadState.Loading) View.VISIBLE else View.GONE
                binding.tvError.visibility = if (it.source.refresh is LoadState.Error) View.VISIBLE else View.GONE
                binding.btRetry.visibility = if (it.source.refresh is LoadState.Error) View.VISIBLE else View.GONE
                if (it.source.refresh is LoadState.NotLoading && answerAdapter.itemCount <= 0) {
                    binding.tvNoAnswerQuestionDetail.visibility = View.VISIBLE
                } else {
                    binding.tvNoAnswerQuestionDetail.visibility = View.INVISIBLE
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                answerViewModel.answerDataFlow?.collectLatest {
                    answerAdapter.submitData(it)
                }
            }
        }
        binding.btRetry.setOnClickListener { answerAdapter.retry() }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (binding.markDownView.canGoBack()) {
            binding.markDownView.goBack()
        } else {
            super.onBackPressed()
            finish()
        }
    }
}