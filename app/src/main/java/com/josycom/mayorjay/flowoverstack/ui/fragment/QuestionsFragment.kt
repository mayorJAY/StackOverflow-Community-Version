package com.josycom.mayorjay.flowoverstack.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.josycom.mayorjay.flowoverstack.ui.adapters.QuestionAdapter
import com.josycom.mayorjay.flowoverstack.databinding.FragmentQuestionsBinding
import com.josycom.mayorjay.flowoverstack.model.Question
import com.josycom.mayorjay.flowoverstack.ui.activity.AnswerActivity
import com.josycom.mayorjay.flowoverstack.ui.adapters.PagingLoadStateAdapter
import com.josycom.mayorjay.flowoverstack.util.AppConstants
import com.josycom.mayorjay.flowoverstack.util.AppUtils
import com.josycom.mayorjay.flowoverstack.ui.viewmodel.CustomQuestionViewModelFactory
import com.josycom.mayorjay.flowoverstack.ui.viewmodel.QuestionViewModel
import dagger.android.support.AndroidSupportInjection
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * This [Fragment] houses all Questions
 */
class QuestionsFragment : Fragment() {

    private lateinit var binding: FragmentQuestionsBinding
    private lateinit var viewModel: QuestionViewModel
    @Inject
    lateinit var viewModelFactory: CustomQuestionViewModelFactory

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)

        viewModelFactory.setInputs(
                AppConstants.FIRST_PAGE,
                AppConstants.PAGE_SIZE,
                AppConstants.ORDER_DESCENDING,
                sortCondition,
                AppConstants.SITE,
                tagName,
                AppConstants.QUESTION_FILTER,
                AppConstants.API_KEY
        )
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuestionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, viewModelFactory).get(QuestionViewModel::class.java)

        initViews()
        fetchAndDisplayQuestions()
    }

    private fun initViews() {
        (activity as AppCompatActivity).supportActionBar?.title = title
        binding.activityScrollUpFab.visibility = View.INVISIBLE
        binding.activityRecyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                binding.activityScrollUpFab.visibility = if (dy > 0) View.VISIBLE else View.INVISIBLE
            }
        })
        binding.activityScrollUpFab.setOnClickListener {
            binding.activityRecyclerView.scrollToPosition(0)
        }
    }

    private fun fetchAndDisplayQuestions() {
        val questionAdapter = QuestionAdapter()
        binding.activityRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            itemAnimator = DefaultItemAnimator()
            adapter = questionAdapter.withLoadStateFooter(PagingLoadStateAdapter { questionAdapter.retry() })
        }

        lifecycleScope.launch {
            questionAdapter.loadStateFlow.collect {
                if (it.source.refresh is LoadState.Loading) onLoading()
                if (it.source.refresh is LoadState.Error) onError()
                if (it.source.refresh !is LoadState.Loading && it.source.refresh !is LoadState.Error) onLoaded()
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.questionDataFlow?.collectLatest {
                    questionAdapter.submitData(it)
                }
            }
        }

        questionAdapter.setOnClickListener(onClickListener)
        binding.btRetry.setOnClickListener { questionAdapter.retry() }
    }

    private val onClickListener = View.OnClickListener { v ->
        Intent(context, AnswerActivity::class.java).apply {
            val currentQuestion = v?.tag as? Question
            if (currentQuestion != null) {
                putExtra(AppConstants.EXTRA_QUESTION_TITLE, currentQuestion.title)
                if (currentQuestion.creationDate != null) {
                    putExtra(AppConstants.EXTRA_QUESTION_DATE, AppUtils.toNormalDate(currentQuestion.creationDate!!.toLong()))
                }
                putExtra(AppConstants.EXTRA_QUESTION_FULL_TEXT, currentQuestion.body)
                putExtra(AppConstants.EXTRA_QUESTION_ANSWERS_COUNT, currentQuestion.answerCount)
                putExtra(AppConstants.EXTRA_QUESTION_ID, currentQuestion.questionId)
                putExtra(AppConstants.EXTRA_QUESTION_VOTES_COUNT, currentQuestion.score)
                val questionOwner = currentQuestion.owner
                if (questionOwner != null) {
                    putExtra(AppConstants.EXTRA_QUESTION_NAME, questionOwner.displayName)
                    putExtra(AppConstants.EXTRA_AVATAR_ADDRESS, questionOwner.profileImage)
                    putExtra(AppConstants.EXTRA_QUESTION_OWNER_LINK, questionOwner.link)
                }
            }
            startActivity(this)
        }
    }

    private fun onLoaded() = binding.apply {
        activityPbFetchData.visibility = View.INVISIBLE
        activityRecyclerView.visibility = View.VISIBLE
        activityTvError.visibility = View.INVISIBLE
        btRetry.visibility = View.INVISIBLE
    }

    private fun onError() = binding.apply {
        activityPbFetchData.visibility = View.INVISIBLE
        activityRecyclerView.visibility = View.INVISIBLE
        activityTvError.visibility = View.VISIBLE
        btRetry.visibility = View.VISIBLE
    }

    private fun onLoading() = binding.apply {
        activityPbFetchData.visibility = View.VISIBLE
        activityRecyclerView.visibility = View.VISIBLE
        activityTvError.visibility = View.INVISIBLE
        btRetry.visibility = View.INVISIBLE
    }

    companion object {
        var title = ""
        var sortCondition = ""
        var tagName = ""
    }
}
