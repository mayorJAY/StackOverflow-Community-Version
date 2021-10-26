package com.josycom.mayorjay.flowoverstack.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.josycom.mayorjay.flowoverstack.R
import com.josycom.mayorjay.flowoverstack.adapters.QuestionAdapter
import com.josycom.mayorjay.flowoverstack.databinding.FragmentQuestionsByHotBinding
import com.josycom.mayorjay.flowoverstack.model.Question
import com.josycom.mayorjay.flowoverstack.ui.activity.AnswerActivity
import com.josycom.mayorjay.flowoverstack.util.AppConstants
import com.josycom.mayorjay.flowoverstack.util.AppUtils
import com.josycom.mayorjay.flowoverstack.viewmodel.CustomQuestionViewModelFactory
import com.josycom.mayorjay.flowoverstack.viewmodel.QuestionViewModel
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

/**
 * This fragment houses the Hot Questions
 */
class QuestionsByHotFragment : Fragment() {

    private lateinit var mFragmentQuestionsByHotBinding: FragmentQuestionsByHotBinding
    private lateinit var mQuestions: PagedList<Question>
    private lateinit var mOnClickListener: View.OnClickListener
    @Inject
    lateinit var viewModelFactory: CustomQuestionViewModelFactory

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
        viewModelFactory.setInputs(AppConstants.FIRST_PAGE,
                AppConstants.PAGE_SIZE,
                AppConstants.ORDER_DESCENDING,
                AppConstants.SORT_BY_HOT,
                AppConstants.SITE,
                AppConstants.QUESTION_FILTER,
                AppConstants.API_KEY)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        mFragmentQuestionsByHotBinding = FragmentQuestionsByHotBinding.inflate(inflater, container, false)
        return mFragmentQuestionsByHotBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mFragmentQuestionsByHotBinding.apply {
            hotSwipeContainer.setColorSchemeResources(R.color.colorPrimaryLight)
            hotScrollUpFab.visibility = View.INVISIBLE
            hotRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (dy > 0) {
                        hotScrollUpFab.visibility = View.VISIBLE
                    } else {
                        hotScrollUpFab.visibility = View.INVISIBLE
                    }
                }
            })
            hotScrollUpFab.setOnClickListener { hotRecyclerView.scrollToPosition(0) }
        }

        mOnClickListener = View.OnClickListener {
            val viewHolder = it.tag as RecyclerView.ViewHolder
            val position = viewHolder.adapterPosition
            Intent(context, AnswerActivity::class.java).apply {
                val currentQuestion = mQuestions[position]
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
        handleRecyclerView()
    }

    private fun handleRecyclerView() {
        val questionAdapter = QuestionAdapter()
        mFragmentQuestionsByHotBinding.hotRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            itemAnimator = DefaultItemAnimator()
        }
        val questionViewModel = ViewModelProvider(this, viewModelFactory)
                .get(QuestionViewModel::class.java)
                .apply {
                    networkState.observe(viewLifecycleOwner, {
                        when (it) {
                            AppConstants.LOADING -> onLoading()
                            AppConstants.LOADED -> onLoaded()
                            AppConstants.FAILED -> onError()
                        }
                    })
                    questionPagedList!!.observe(viewLifecycleOwner, {
                        mQuestions = it
                        questionAdapter.submitList(it)
                    })
                }
        mFragmentQuestionsByHotBinding.apply {
            hotRecyclerView.adapter = questionAdapter.apply {
                setOnClickListener(mOnClickListener)
            }
            hotSwipeContainer.setOnRefreshListener {
                questionViewModel.refresh()
                hotSwipeContainer.isRefreshing = false
            }
        }
    }

    private fun onLoaded() = mFragmentQuestionsByHotBinding.apply {
        hotPbFetchData.visibility = View.INVISIBLE
        hotRecyclerView.visibility = View.VISIBLE
        hotTvError.visibility = View.INVISIBLE
    }

    private fun onError() = mFragmentQuestionsByHotBinding.apply {
        hotPbFetchData.visibility = View.INVISIBLE
        hotRecyclerView.visibility = View.INVISIBLE
        hotTvError.visibility = View.VISIBLE
    }

    private fun onLoading() = mFragmentQuestionsByHotBinding.apply {
        hotPbFetchData.visibility = View.VISIBLE
        hotRecyclerView.visibility = View.INVISIBLE
        hotTvError.visibility = View.INVISIBLE
    }
}