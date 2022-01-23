package com.josycom.mayorjay.flowoverstack.ui.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.josycom.mayorjay.flowoverstack.R
import com.josycom.mayorjay.flowoverstack.databinding.ActivitySearchBinding
import com.josycom.mayorjay.flowoverstack.model.Question
import com.josycom.mayorjay.flowoverstack.network.SearchResponse
import com.josycom.mayorjay.flowoverstack.ui.adapters.SearchAdapter
import com.josycom.mayorjay.flowoverstack.ui.viewmodel.CustomSearchViewModelFactory
import com.josycom.mayorjay.flowoverstack.ui.viewmodel.SearchViewModel
import com.josycom.mayorjay.flowoverstack.util.AppConstants
import com.josycom.mayorjay.flowoverstack.util.AppUtils
import dagger.android.AndroidInjection
import javax.inject.Inject

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private var searchInput: String? = null
    private var questions: List<Question>? = null
    private lateinit var searchViewModel: SearchViewModel
    @Inject
    lateinit var viewModelFactory: CustomSearchViewModelFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModelFactory.setInputs(AppConstants.FIRST_PAGE, AppConstants.SEARCH_PAGE_SIZE)
        binding.apply {
            rvSearchResults.layoutManager = LinearLayoutManager(this@SearchActivity)
            rvSearchResults.setHasFixedSize(true)
            rvSearchResults.itemAnimator = DefaultItemAnimator()
            searchScrollUpFab.visibility = View.INVISIBLE
            searchNestedScrollview.setOnScrollChangeListener { _: NestedScrollView?, _: Int, scrollY: Int, _: Int, _: Int ->
                if (scrollY > 0) {
                    searchScrollUpFab.visibility = View.VISIBLE
                } else {
                    searchScrollUpFab.visibility = View.INVISIBLE
                }
            }
            searchScrollUpFab.setOnClickListener { searchNestedScrollview.scrollTo(0, 0) }
        }

        binding.searchButton.setOnClickListener {
            if (TextUtils.isEmpty(binding.searchTextInputEditText.text.toString())) {
                binding.searchTextInputEditText.error = getString(R.string.type_a_search_query)
            } else {
                searchInput = binding.searchTextInputEditText.text.toString().trim()
                val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
                setQuery()
                binding.ivLookup.visibility = View.INVISIBLE
            }
        }
        val searchAdapter = SearchAdapter()
        searchViewModel = ViewModelProvider(this, viewModelFactory).get(SearchViewModel::class.java)
        searchViewModel.responseLiveData.observe(this, { searchResponse: SearchResponse ->
            when (searchResponse.networkState) {
                AppConstants.LOADING -> onLoading()
                AppConstants.LOADED -> {
                    onLoaded()
                    questions = searchResponse.questions
                    searchAdapter.setQuestions(searchResponse.questions)
                }
                AppConstants.NO_MATCHING_RESULT -> onNoMatchingResult()
                AppConstants.FAILED -> onError()
            }
        })
        binding.rvSearchResults.adapter = searchAdapter
        searchAdapter.setOnClickListener(viewHolderClickListener, shareClickListener)
    }

    private val viewHolderClickListener = View.OnClickListener {
        val viewHolder = it.tag as RecyclerView.ViewHolder
        val position = viewHolder.bindingAdapterPosition
        Intent(this, AnswerActivity::class.java).apply {
            val currentQuestion = questions!![position]
            putExtra(AppConstants.EXTRA_QUESTION_TITLE, currentQuestion.title)
            putExtra(AppConstants.EXTRA_QUESTION_DATE, AppUtils.toNormalDate(currentQuestion.creationDate!!.toLong()))
            putExtra(AppConstants.EXTRA_QUESTION_FULL_TEXT, currentQuestion.body)
            putExtra(AppConstants.EXTRA_QUESTION_ANSWERS_COUNT, currentQuestion.answerCount)
            putExtra(AppConstants.EXTRA_QUESTION_ID, currentQuestion.questionId)
            putExtra(AppConstants.EXTRA_QUESTION_VOTES_COUNT, currentQuestion.score)
            putExtra(AppConstants.EXTRA_QUESTION_LINK, currentQuestion.link)
            val questionOwner = currentQuestion.owner
            if (questionOwner != null) {
                putExtra(AppConstants.EXTRA_QUESTION_NAME, questionOwner.displayName)
                putExtra(AppConstants.EXTRA_AVATAR_ADDRESS, questionOwner.profileImage)
                putExtra(AppConstants.EXTRA_QUESTION_OWNER_LINK, questionOwner.link)
            }
            startActivity(this)
        }
    }

    private val shareClickListener = View.OnClickListener { v ->
        val currentQuestion = v.tag as? Question
        if (currentQuestion != null) {
            AppUtils.shareContent(currentQuestion.link!!, this)
        }
    }

    private fun setQuery() {
        searchViewModel.setQuery(searchInput!!)
    }

    private fun onLoading() = binding.apply {
        searchPbFetchData.visibility = View.VISIBLE
        rvSearchResults.visibility = View.INVISIBLE
        searchTvError.visibility = View.INVISIBLE
    }

    private fun onLoaded() = binding.apply {
        searchPbFetchData.visibility = View.INVISIBLE
        rvSearchResults.visibility = View.VISIBLE
        searchTvError.visibility = View.INVISIBLE
    }

    private fun onNoMatchingResult() = binding.apply {
        searchPbFetchData.visibility = View.INVISIBLE
        rvSearchResults.visibility = View.INVISIBLE
        searchTvError.visibility = View.VISIBLE
        searchTvError.setText(R.string.no_matching_result)
    }

    private fun onError() = binding.apply {
        searchPbFetchData.visibility = View.INVISIBLE
        rvSearchResults.visibility = View.INVISIBLE
        searchTvError.visibility = View.VISIBLE
        searchTvError.setText(R.string.network_error_message)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}