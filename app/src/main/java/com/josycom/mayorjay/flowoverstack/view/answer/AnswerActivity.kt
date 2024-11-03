package com.josycom.mayorjay.flowoverstack.view.answer

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.josycom.mayorjay.flowoverstack.R
import com.josycom.mayorjay.flowoverstack.data.model.Answer
import com.josycom.mayorjay.flowoverstack.data.model.Question
import com.josycom.mayorjay.flowoverstack.databinding.ActivityAnswerBinding
import com.josycom.mayorjay.flowoverstack.util.AppConstants
import com.josycom.mayorjay.flowoverstack.util.AppUtils
import com.josycom.mayorjay.flowoverstack.view.home.PagingLoadStateAdapter
import com.josycom.mayorjay.flowoverstack.viewmodel.AnswerViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.jsoup.Jsoup

@AndroidEntryPoint
class AnswerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAnswerBinding
    private val answerViewModel: AnswerViewModel by viewModels()
    private var ownerQuestionLink: String? = null
    private var questionId = 0
    private var questionLink: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnswerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewContents(intent)
        answerViewModel.init(questionId,
        AppConstants.ORDER_DESCENDING,
        AppConstants.SORT_BY_ACTIVITY,
        AppConstants.SITE,
        AppConstants.ANSWER_FILTER,
        AppConstants.API_KEY)

        fetchAndDisplayAnswers()
        setupListeners()
    }

    private fun setupViewContents(intent: Intent?) {
        var avatarAddress: String? = ""
        intent?.let {
            val question = it.getSerializableExtra(AppConstants.EXTRA_QUESTION_KEY) as Question
            binding.apply {
                tvQuestionDetail.text = Jsoup.parse(question.title).text()
                markDownView.setMarkDownText(question.body)
                tvDateQuestionDetail.text = AppUtils.toNormalDate(question.creationDate.toLong())
                tvNameQuestionDetail.text = question.owner.displayName
            }
            val voteCount = question.score
            if (voteCount <= 0) {
                binding.tvVotesCountItem.text = voteCount.toString()
            } else {
                binding.tvVotesCountItem.text = getString(R.string.plus_score, voteCount)
            }
            questionId = question.questionId
            avatarAddress = question.owner.profileImage
            ownerQuestionLink = question.owner.link
            questionLink = question.link
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
            ivShare.setOnClickListener {
                val content = getString(
                    R.string.share_content,
                    getString(R.string.question),
                    questionLink ?: "",
                    AppConstants.PLAY_STORE_URL
                )
                AppUtils.shareContent(content, this@AnswerActivity)
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
                binding.pbFetchData.isVisible = it.source.refresh is LoadState.Loading
                binding.tvError.isVisible = it.source.refresh is LoadState.Error
                binding.btRetry.isVisible = it.source.refresh is LoadState.Error
                if (it.source.refresh is LoadState.NotLoading && answerAdapter.itemCount <= 0) {
                    binding.tvNoAnswerQuestionDetail.isVisible = true
                } else {
                    binding.tvNoAnswerQuestionDetail.isInvisible = true
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
        answerAdapter.setOnClickListener(shareClickListener)
    }

    private val shareClickListener = View.OnClickListener { v ->
        val currentAnswer = v.tag as? Answer
        currentAnswer?.let {
            val content = getString(
                R.string.share_content,
                getString(R.string.answer),
                "https://stackoverflow.com/a/${it.answerId}",
                AppConstants.PLAY_STORE_URL
            )
            AppUtils.shareContent(content, this)
        }
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