package com.josycom.mayorjay.flowoverstack.view.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.josycom.mayorjay.flowoverstack.R
import com.josycom.mayorjay.flowoverstack.data.model.Question
import com.josycom.mayorjay.flowoverstack.databinding.FragmentQuestionsBinding
import com.josycom.mayorjay.flowoverstack.util.AppConstants
import com.josycom.mayorjay.flowoverstack.util.AppUtils
import com.josycom.mayorjay.flowoverstack.view.answer.AnswerActivity
import com.josycom.mayorjay.flowoverstack.viewmodel.QuestionViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * This [Fragment] houses all Questions
 */
@AndroidEntryPoint
class QuestionsFragment : Fragment() {

    private lateinit var binding: FragmentQuestionsBinding
    private val viewModel: QuestionViewModel by viewModels()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuestionsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.init(
            AppConstants.FIRST_PAGE,
            AppConstants.PAGE_SIZE,
            AppConstants.ORDER_DESCENDING,
            sortCondition,
            AppConstants.SITE,
            tagName,
            AppConstants.QUESTION_FILTER,
            AppConstants.API_KEY
        )

        initViews()
        fetchAndDisplayQuestions()
    }

    private fun initViews() {
        (activity as AppCompatActivity).supportActionBar?.title = title
        binding.activityScrollUpFab.isInvisible = true
        binding.activityRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    binding.activityScrollUpFab.isVisible = true
                } else {
                    binding.activityScrollUpFab.isInvisible = true
                }
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

        viewLifecycleOwner.lifecycleScope.launch {
            questionAdapter.loadStateFlow.collect {
                if (it.source.refresh is LoadState.Loading) onLoading()
                if (it.source.refresh is LoadState.Error) onError()
                if (it.source.refresh !is LoadState.Loading && it.source.refresh !is LoadState.Error) onLoaded()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.questionDataFlow?.collectLatest {
                    questionAdapter.submitData(it)
                }
            }
        }

        questionAdapter.setOnClickListeners(viewHolderClickListener, shareClickListener)
        binding.btRetry.setOnClickListener { questionAdapter.retry() }
    }

    private val viewHolderClickListener = View.OnClickListener { v ->
        Intent(requireContext(), AnswerActivity::class.java).apply {
            val currentQuestion = v?.tag as? Question
            currentQuestion?.let { putExtra(AppConstants.EXTRA_QUESTION_KEY, it) }
            startActivity(this)
        }
    }

    private val shareClickListener = View.OnClickListener { v ->
        val currentQuestion = v.tag as? Question
        currentQuestion?.let {
            val content = getString(
                R.string.share_content,
                getString(R.string.question),
                it.link,
                AppConstants.PLAY_STORE_URL
            )
            AppUtils.shareContent(content, requireContext())
        }
    }

    private fun onLoaded() = binding.apply {
        activityPbFetchData.isInvisible = true
        activityRecyclerView.isVisible = true
        activityTvError.isInvisible = true
        btRetry.isInvisible = true
    }

    private fun onError() = binding.apply {
        activityPbFetchData.isInvisible = true
        activityRecyclerView.isInvisible = true
        activityTvError.isVisible = true
        btRetry.isVisible = true
    }

    private fun onLoading() = binding.apply {
        activityPbFetchData.isVisible = true
        activityRecyclerView.isVisible = true
        activityTvError.isInvisible = true
        btRetry.isInvisible = true
    }

    companion object {
        var title = ""
        var sortCondition = ""
        var tagName = ""
    }
}
