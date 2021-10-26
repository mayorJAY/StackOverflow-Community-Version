package com.josycom.mayorjay.flowoverstack.ui.activity

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.josycom.mayorjay.flowoverstack.R
import com.josycom.mayorjay.flowoverstack.adapters.AnswerAdapter
import com.josycom.mayorjay.flowoverstack.databinding.ActivityAnswerBinding
import com.josycom.mayorjay.flowoverstack.model.Answer
import com.josycom.mayorjay.flowoverstack.util.AppConstants
import com.josycom.mayorjay.flowoverstack.util.AppUtils
import com.josycom.mayorjay.flowoverstack.viewmodel.AnswerViewModel
import com.josycom.mayorjay.flowoverstack.viewmodel.CustomAnswerViewModelFactory
import dagger.android.AndroidInjection
import org.jsoup.Jsoup
import javax.inject.Inject

class AnswerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAnswerBinding
    private lateinit var answerAdapter: AnswerAdapter
    private var ownerQuestionLink: String? = null
    private var answers: List<Answer>? = null
    @Inject
    lateinit var viewModelFactory: CustomAnswerViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityAnswerBinding.inflate(layoutInflater)
        binding.apply {
            setContentView(this.root)
            rvAnswers.layoutManager = LinearLayoutManager(this@AnswerActivity)
        }
        answerAdapter = AnswerAdapter()
        val mIntent = intent
        var questionId = 0
        var avatarAddress: String? = ""
        if (mIntent != null) {
            binding.apply {
                tvQuestionDetail.text = Jsoup.parse(mIntent.getStringExtra(AppConstants.EXTRA_QUESTION_TITLE)).text()
                markDownView.setMarkDownText(mIntent.getStringExtra(AppConstants.EXTRA_QUESTION_FULL_TEXT))
                tvDateQuestionDetail.text = mIntent.getStringExtra(AppConstants.EXTRA_QUESTION_DATE)
                tvNameQuestionDetail.text = mIntent.getStringExtra(AppConstants.EXTRA_QUESTION_NAME)
            }
            val voteCount = mIntent.getIntExtra(AppConstants.EXTRA_QUESTION_VOTES_COUNT, 0)
            if (voteCount <= 0) {
                binding.tvVotesCountItem.text = voteCount.toString()
            } else {
                binding.tvVotesCountItem.text = getString(R.string.plus_score, voteCount)
            }
            questionId = mIntent.getIntExtra(AppConstants.EXTRA_QUESTION_ID, 0)
            avatarAddress = mIntent.getStringExtra(AppConstants.EXTRA_AVATAR_ADDRESS)
        }
        ownerQuestionLink = mIntent.getStringExtra(AppConstants.EXTRA_QUESTION_OWNER_LINK)
        Glide.with(this)
                .load(avatarAddress)
                .placeholder(R.drawable.loading)
                .into(binding.ivAvatarQuestionDetail)

        viewModelFactory.setInputs(questionId,
                AppConstants.ORDER_DESCENDING,
                AppConstants.SORT_BY_ACTIVITY,
                AppConstants.SITE,
                AppConstants.ANSWER_FILTER,
                AppConstants.API_KEY)
        val answerViewModel = ViewModelProvider(this, viewModelFactory).get(AnswerViewModel::class.java)
        answerViewModel.answersLiveData.observe(this, {
            answers = it
            if (answers!!.isEmpty()) {
                binding.tvNoAnswerQuestionDetail.visibility = View.VISIBLE
            } else {
                binding.tvNoAnswerQuestionDetail.visibility = View.INVISIBLE
                answerAdapter.setAnswers(it)
            }
        })

        binding.apply {
            rvAnswers.adapter = answerAdapter
            avatarCard.setOnClickListener {
                AppUtils.directLinkToBrowser(this@AnswerActivity, ownerQuestionLink)
            }
            tvNameQuestionDetail.setOnClickListener {
                AppUtils.directLinkToBrowser(this@AnswerActivity, ownerQuestionLink)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        answers = null
        answerAdapter.notifyDataSetChanged()
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