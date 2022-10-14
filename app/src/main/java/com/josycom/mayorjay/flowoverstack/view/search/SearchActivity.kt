package com.josycom.mayorjay.flowoverstack.view.search

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.josycom.mayorjay.flowoverstack.R
import com.josycom.mayorjay.flowoverstack.databinding.ActivitySearchBinding
import com.josycom.mayorjay.flowoverstack.data.model.Question
import com.josycom.mayorjay.flowoverstack.data.remote.model.SearchResponse
import com.josycom.mayorjay.flowoverstack.viewmodel.CustomSearchViewModelFactory
import com.josycom.mayorjay.flowoverstack.viewmodel.SearchViewModel
import com.josycom.mayorjay.flowoverstack.util.AppConstants
import com.josycom.mayorjay.flowoverstack.util.AppUtils
import com.josycom.mayorjay.flowoverstack.view.answer.AnswerActivity
import dagger.android.AndroidInjection
import javax.inject.Inject

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private var searchInput: String = ""
    private var questions: List<Question>? = listOf()
    private lateinit var searchViewModel: SearchViewModel

    @Inject
    lateinit var viewModelFactory: CustomSearchViewModelFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModelFactory.setInputs(AppConstants.FIRST_PAGE, AppConstants.SEARCH_PAGE_SIZE)
        searchViewModel = ViewModelProvider(this, viewModelFactory).get(SearchViewModel::class.java)
        binding.apply {
            rvSearchResults.layoutManager = LinearLayoutManager(this@SearchActivity)
            rvSearchResults.setHasFixedSize(true)
            rvSearchResults.itemAnimator = DefaultItemAnimator()
            searchScrollUpFab.isInvisible = true
            searchNestedScrollview.setOnScrollChangeListener { _: NestedScrollView?, _: Int, scrollY: Int, _: Int, _: Int ->
                if (scrollY > 0) {
                    searchScrollUpFab.isVisible = true
                } else {
                    searchScrollUpFab.isInvisible = true
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
                inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
                setQuery()
                binding.ivLookup.isInvisible = true
            }
        }
        val searchAdapter = SearchAdapter()
        searchViewModel.responseLiveData.observe(this) { searchResponse: SearchResponse ->
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
        }
        binding.rvSearchResults.adapter = searchAdapter
        searchAdapter.setOnClickListener(viewHolderClickListener, shareClickListener)
    }

    private val viewHolderClickListener = View.OnClickListener { v ->
        val viewHolder = v.tag as RecyclerView.ViewHolder
        val position = viewHolder.bindingAdapterPosition
        Intent(this, AnswerActivity::class.java).apply {
            val currentQuestion = questions?.get(position)
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
            AppUtils.shareContent(content, this)
        }
    }

    private fun setQuery() {
        searchViewModel.setQuery(searchInput)
    }

    private fun onLoading() = binding.apply {
        searchPbFetchData.isVisible = true
        rvSearchResults.isInvisible = true
        searchTvError.isInvisible = true
    }

    private fun onLoaded() = binding.apply {
        searchPbFetchData.isInvisible = true
        rvSearchResults.isVisible = true
        searchTvError.isInvisible = true
    }

    private fun onNoMatchingResult() = binding.apply {
        searchPbFetchData.isInvisible = true
        rvSearchResults.isInvisible = true
        searchTvError.isVisible = true
        searchTvError.setText(R.string.no_matching_result)
    }

    private fun onError() = binding.apply {
        searchPbFetchData.isInvisible = true
        rvSearchResults.isInvisible = true
        searchTvError.isVisible = true
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