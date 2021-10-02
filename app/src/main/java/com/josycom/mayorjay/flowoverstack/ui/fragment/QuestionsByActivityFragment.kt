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
import com.josycom.mayorjay.flowoverstack.databinding.FragmentQuestionsByActivityBinding
import com.josycom.mayorjay.flowoverstack.model.Question
import com.josycom.mayorjay.flowoverstack.ui.activity.AnswerActivity
import com.josycom.mayorjay.flowoverstack.util.AppConstants.API_KEY
import com.josycom.mayorjay.flowoverstack.util.AppConstants.EXTRA_AVATAR_ADDRESS
import com.josycom.mayorjay.flowoverstack.util.AppConstants.EXTRA_QUESTION_ANSWERS_COUNT
import com.josycom.mayorjay.flowoverstack.util.AppConstants.EXTRA_QUESTION_DATE
import com.josycom.mayorjay.flowoverstack.util.AppConstants.EXTRA_QUESTION_FULL_TEXT
import com.josycom.mayorjay.flowoverstack.util.AppConstants.EXTRA_QUESTION_ID
import com.josycom.mayorjay.flowoverstack.util.AppConstants.EXTRA_QUESTION_NAME
import com.josycom.mayorjay.flowoverstack.util.AppConstants.EXTRA_QUESTION_OWNER_LINK
import com.josycom.mayorjay.flowoverstack.util.AppConstants.EXTRA_QUESTION_TITLE
import com.josycom.mayorjay.flowoverstack.util.AppConstants.EXTRA_QUESTION_VOTES_COUNT
import com.josycom.mayorjay.flowoverstack.util.AppConstants.FAILED
import com.josycom.mayorjay.flowoverstack.util.AppConstants.FIRST_PAGE
import com.josycom.mayorjay.flowoverstack.util.AppConstants.LOADED
import com.josycom.mayorjay.flowoverstack.util.AppConstants.LOADING
import com.josycom.mayorjay.flowoverstack.util.AppConstants.ORDER_DESCENDING
import com.josycom.mayorjay.flowoverstack.util.AppConstants.PAGE_SIZE
import com.josycom.mayorjay.flowoverstack.util.AppConstants.QUESTION_FILTER
import com.josycom.mayorjay.flowoverstack.util.AppConstants.SITE
import com.josycom.mayorjay.flowoverstack.util.AppConstants.SORT_BY_ACTIVITY
import com.josycom.mayorjay.flowoverstack.util.DateUtil
import com.josycom.mayorjay.flowoverstack.viewmodel.CustomQuestionViewModelFactory
import com.josycom.mayorjay.flowoverstack.viewmodel.QuestionViewModel
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

/**
 * This [Fragment] houses the Activity Questions
 */
class QuestionsByActivityFragment : Fragment() {

    private lateinit var mFragmentQuestionsByActivityBinding: FragmentQuestionsByActivityBinding
    private lateinit var mQuestions: PagedList<Question>
    private lateinit var mOnClickListener: View.OnClickListener
    @Inject
    lateinit var viewModelFactory: CustomQuestionViewModelFactory

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)

        viewModelFactory.setInputs(
                FIRST_PAGE,
                PAGE_SIZE,
                ORDER_DESCENDING,
                SORT_BY_ACTIVITY,
                SITE,
                QUESTION_FILTER,
                API_KEY
        )
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        mFragmentQuestionsByActivityBinding = FragmentQuestionsByActivityBinding.inflate(inflater, container, false)
        mFragmentQuestionsByActivityBinding.apply {
            activitySwipeContainer.setColorSchemeResources(R.color.colorPrimaryLight)
            activityScrollUpFab.visibility = View.INVISIBLE

            activityRecyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    activityScrollUpFab.visibility = if (dy > 0) View.VISIBLE else View.INVISIBLE
                }
            })
            activityScrollUpFab.setOnClickListener { activityRecyclerView.scrollToPosition(0) }
        }
        mOnClickListener = View.OnClickListener {
            val viewHolder = it.tag as RecyclerView.ViewHolder
            val position = viewHolder.adapterPosition
            Intent(context, AnswerActivity::class.java).apply {
                val currentQuestion = mQuestions.get(position)
                assert(currentQuestion != null)
                val questionOwner = currentQuestion!!.owner

                putExtra(EXTRA_QUESTION_TITLE, currentQuestion.title)
                putExtra(EXTRA_QUESTION_NAME, questionOwner.displayName)
                putExtra(EXTRA_QUESTION_DATE, DateUtil.toNormalDate(currentQuestion.creationDate.toLong()))
                putExtra(EXTRA_QUESTION_FULL_TEXT, currentQuestion.body)
                putExtra(EXTRA_AVATAR_ADDRESS, questionOwner.profileImage)
                putExtra(EXTRA_QUESTION_ANSWERS_COUNT, currentQuestion.answerCount)
                putExtra(EXTRA_QUESTION_ID, currentQuestion.questionId)
                putExtra(EXTRA_QUESTION_VOTES_COUNT, currentQuestion.score)
                putExtra(EXTRA_QUESTION_OWNER_LINK, questionOwner.link)

                startActivity(this)
            }

        }
        handleRecyclerView()
        return mFragmentQuestionsByActivityBinding.root
    }

    private fun handleRecyclerView() {
        val questionAdapter = QuestionAdapter()
        val linearLayoutManager = LinearLayoutManager(context)

        mFragmentQuestionsByActivityBinding.activityRecyclerView.apply {
            layoutManager = linearLayoutManager
            itemAnimator = DefaultItemAnimator()
        }

        val questionViewModel = ViewModelProvider(this, viewModelFactory)
                .get(QuestionViewModel::class.java)
                .apply {
                    networkState.observe(viewLifecycleOwner, {
                        when (it) {
                            LOADING -> onLoading()
                            LOADED -> onLoaded()
                            FAILED -> onError()
                        }
                    })

                    questionPagedList.observe(viewLifecycleOwner, {
                        mQuestions = it
                        questionAdapter.submitList(it)
                    })
                }
        mFragmentQuestionsByActivityBinding.apply {
            activityRecyclerView.adapter = questionAdapter.apply {
                setOnClickListener(mOnClickListener)
            }
            activitySwipeContainer.setOnRefreshListener {
                questionViewModel.refresh()
                activitySwipeContainer.isRefreshing = false
            }
        }
    }

    private fun onLoaded() = mFragmentQuestionsByActivityBinding.apply {
        activityPbFetchData.visibility = View.INVISIBLE
        activityRecyclerView.visibility = View.VISIBLE
        activityTvError.visibility = View.INVISIBLE
    }

    private fun onError() = mFragmentQuestionsByActivityBinding.apply {
        activityPbFetchData.visibility = View.INVISIBLE
        activityRecyclerView.visibility = View.INVISIBLE
        activityTvError.visibility = View.VISIBLE
    }

    private fun onLoading() = mFragmentQuestionsByActivityBinding.apply {
        activityPbFetchData.visibility = View.VISIBLE
        activityRecyclerView.visibility = View.VISIBLE
        activityTvError.visibility = View.INVISIBLE
    }
}
