package com.josycom.mayorjay.flowoverstack.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.josycom.mayorjay.flowoverstack.R
import com.josycom.mayorjay.flowoverstack.adapters.QuestionAdapter
import com.josycom.mayorjay.flowoverstack.databinding.FragmentQuestionsBinding
import com.josycom.mayorjay.flowoverstack.model.Question
import com.josycom.mayorjay.flowoverstack.ui.activity.AnswerActivity
import com.josycom.mayorjay.flowoverstack.util.AppConstants
import com.josycom.mayorjay.flowoverstack.util.AppUtils
import com.josycom.mayorjay.flowoverstack.viewmodel.CustomQuestionViewModelFactory
import com.josycom.mayorjay.flowoverstack.viewmodel.QuestionViewModel
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

/**
 * This [Fragment] houses all Questions
 */
class QuestionsFragment : Fragment() {

    private lateinit var binding: FragmentQuestionsBinding
    private lateinit var questions: PagedList<Question>
    private lateinit var onClickListener: View.OnClickListener
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
        (activity as AppCompatActivity).supportActionBar?.title = title
        binding.apply {
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

        onClickListener = View.OnClickListener {
            val viewHolder = it.tag as RecyclerView.ViewHolder
            val position = viewHolder.adapterPosition
            Intent(context, AnswerActivity::class.java).apply {
                val currentQuestion = questions[position]
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
        binding.activityRecyclerView.apply {
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
                        questions = it
                        questionAdapter.submitList(it)
                    })
                }
        binding.apply {
            activityRecyclerView.adapter = questionAdapter.apply {
                setOnClickListener(onClickListener)
            }
            activitySwipeContainer.setOnRefreshListener {
                questionViewModel.refresh()
                activitySwipeContainer.isRefreshing = false
            }
        }
    }

    private fun onLoaded() = binding.apply {
        activityPbFetchData.visibility = View.INVISIBLE
        activityRecyclerView.visibility = View.VISIBLE
        activityTvError.visibility = View.INVISIBLE
    }

    private fun onError() = binding.apply {
        activityPbFetchData.visibility = View.INVISIBLE
        activityRecyclerView.visibility = View.INVISIBLE
        activityTvError.visibility = View.VISIBLE
    }

    private fun onLoading() = binding.apply {
        activityPbFetchData.visibility = View.VISIBLE
        activityRecyclerView.visibility = View.VISIBLE
        activityTvError.visibility = View.INVISIBLE
    }

    companion object {
        var title = ""
        var sortCondition = ""
    }
}
