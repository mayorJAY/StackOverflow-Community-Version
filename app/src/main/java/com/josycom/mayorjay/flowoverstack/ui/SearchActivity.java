package com.josycom.mayorjay.flowoverstack.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.josycom.mayorjay.flowoverstack.R;
import com.josycom.mayorjay.flowoverstack.adapters.SearchAdapter;
import com.josycom.mayorjay.flowoverstack.databinding.ActivitySearchBinding;
import com.josycom.mayorjay.flowoverstack.model.Owner;
import com.josycom.mayorjay.flowoverstack.model.Question;
import com.josycom.mayorjay.flowoverstack.util.DateUtil;
import com.josycom.mayorjay.flowoverstack.util.StringConstants;
import com.josycom.mayorjay.flowoverstack.viewmodel.SearchViewModel;

import java.util.List;
import java.util.Objects;

import static com.josycom.mayorjay.flowoverstack.util.StringConstants.EXTRA_AVATAR_ADDRESS;
import static com.josycom.mayorjay.flowoverstack.util.StringConstants.EXTRA_QUESTION_ANSWERS_COUNT;
import static com.josycom.mayorjay.flowoverstack.util.StringConstants.EXTRA_QUESTION_DATE;
import static com.josycom.mayorjay.flowoverstack.util.StringConstants.EXTRA_QUESTION_FULL_TEXT;
import static com.josycom.mayorjay.flowoverstack.util.StringConstants.EXTRA_QUESTION_ID;
import static com.josycom.mayorjay.flowoverstack.util.StringConstants.EXTRA_QUESTION_NAME;
import static com.josycom.mayorjay.flowoverstack.util.StringConstants.EXTRA_QUESTION_OWNER_LINK;
import static com.josycom.mayorjay.flowoverstack.util.StringConstants.EXTRA_QUESTION_TITLE;

public class SearchActivity extends AppCompatActivity {

    private ActivitySearchBinding mActivitySearchBinding;
    private String mSearchInput;
    private List<Question> mQuestions;
    private SearchViewModel mSearchViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivitySearchBinding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(mActivitySearchBinding.getRoot());

        mActivitySearchBinding.rvSearchResults.setLayoutManager(new LinearLayoutManager(this));
        mActivitySearchBinding.rvSearchResults.setHasFixedSize(true);
        mActivitySearchBinding.rvSearchResults.setItemAnimator(new DefaultItemAnimator());

        mActivitySearchBinding.searchScrollUpFab.setVisibility(View.INVISIBLE);
        mActivitySearchBinding.searchNestedScrollview.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY > 0) {
                mActivitySearchBinding.searchScrollUpFab.setVisibility(View.VISIBLE);
            } else {
                mActivitySearchBinding.searchScrollUpFab.setVisibility(View.INVISIBLE);
            }
        });
        mActivitySearchBinding.searchScrollUpFab.setOnClickListener(v -> mActivitySearchBinding.searchNestedScrollview.scrollTo(0, 0));

        View.OnClickListener mOnClickListener = v -> {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) v.getTag();
            int position = viewHolder.getAdapterPosition();
            Intent answerActivityIntent = new Intent(getApplicationContext(), AnswerActivity.class);
            Question currentQuestion = mQuestions.get(position);
            Owner questionOwner = currentQuestion.getOwner();

            answerActivityIntent.putExtra(EXTRA_QUESTION_TITLE, currentQuestion.getTitle());
            answerActivityIntent.putExtra(EXTRA_QUESTION_NAME, questionOwner.getDisplayName());
            answerActivityIntent.putExtra(EXTRA_QUESTION_DATE,
                    DateUtil.toNormalDate(currentQuestion.getCreationDate()));
            answerActivityIntent.putExtra(EXTRA_QUESTION_FULL_TEXT, currentQuestion.getBody());
            answerActivityIntent.putExtra(EXTRA_AVATAR_ADDRESS, questionOwner.getProfileImage());
            answerActivityIntent.putExtra(EXTRA_QUESTION_ANSWERS_COUNT, currentQuestion.getAnswerCount());
            answerActivityIntent.putExtra(EXTRA_QUESTION_ID, currentQuestion.getQuestionId());
            answerActivityIntent.putExtra(EXTRA_QUESTION_OWNER_LINK, questionOwner.getLink());

            startActivity(answerActivityIntent);
            overridePendingTransition(R.anim.fade_in_anim, R.anim.fade_out_anim);
        };

        // What happens when the search button is clicked
        mActivitySearchBinding.searchButton.setOnClickListener(v -> {
            if (TextUtils.isEmpty(Objects.requireNonNull(mActivitySearchBinding.searchTextInputEditText.getText()).toString())) {
                mActivitySearchBinding.searchTextInputEditText.setError(getString(R.string.type_a_search_query));
            } else {
                mSearchInput = Objects.requireNonNull(mActivitySearchBinding.searchTextInputEditText.getText()).toString();
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (inputMethodManager != null) {
                    inputMethodManager.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), 0);
                }
                makeSearch();
            }
        });
        final SearchAdapter searchAdapter = new SearchAdapter();
        mSearchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        mSearchViewModel.getResponseLiveData().observe(this, searchResponse -> {
            switch (searchResponse.networkState) {
                case StringConstants.LOADING:
                    onLoading();
                    break;
                case StringConstants.LOADED:
                    onLoaded();
                    mQuestions = searchResponse.questions;
                    searchAdapter.setQuestions(searchResponse.questions);
                    break;
                case StringConstants.NO_MATCHING_RESULT:
                    onNoMatchingResult();
                    break;
                case StringConstants.FAILED:
                    onError();
                    break;
            }
        });
        mActivitySearchBinding.rvSearchResults.setAdapter(searchAdapter);
        searchAdapter.setOnClickListener(mOnClickListener);
    }

    private void makeSearch() {
        mSearchViewModel.setQuery(mSearchInput);
    }

    private void onLoading() {
        mActivitySearchBinding.searchPbFetchData.setVisibility(View.VISIBLE);
        mActivitySearchBinding.rvSearchResults.setVisibility(View.INVISIBLE);
        mActivitySearchBinding.searchTvError.setVisibility(View.INVISIBLE);
    }

    private void onLoaded() {
        mActivitySearchBinding.searchPbFetchData.setVisibility(View.INVISIBLE);
        mActivitySearchBinding.rvSearchResults.setVisibility(View.VISIBLE);
        mActivitySearchBinding.searchTvError.setVisibility(View.INVISIBLE);
    }

    private void onNoMatchingResult() {
        mActivitySearchBinding.searchPbFetchData.setVisibility(View.INVISIBLE);
        mActivitySearchBinding.rvSearchResults.setVisibility(View.INVISIBLE);
        mActivitySearchBinding.searchTvError.setVisibility(View.VISIBLE);
        mActivitySearchBinding.searchTvError.setText(R.string.no_matching_result);
    }

    private void onError() {
        mActivitySearchBinding.searchPbFetchData.setVisibility(View.INVISIBLE);
        mActivitySearchBinding.rvSearchResults.setVisibility(View.INVISIBLE);
        mActivitySearchBinding.searchTvError.setVisibility(View.VISIBLE);
        mActivitySearchBinding.searchTvError.setText(R.string.search_error_message);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in_anim, R.anim.fade_out_anim);
    }
}