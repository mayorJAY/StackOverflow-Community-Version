package com.example.josycom.flowoverstack.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.josycom.flowoverstack.R;
import com.example.josycom.flowoverstack.adapters.SearchAdapter;
import com.example.josycom.flowoverstack.model.Owner;
import com.example.josycom.flowoverstack.model.Question;
import com.example.josycom.flowoverstack.util.DateUtil;
import com.example.josycom.flowoverstack.util.StringConstants;
import com.example.josycom.flowoverstack.viewmodel.SearchViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;
import java.util.Objects;

import static com.example.josycom.flowoverstack.util.StringConstants.EXTRA_AVATAR_ADDRESS;
import static com.example.josycom.flowoverstack.util.StringConstants.EXTRA_QUESTION_ANSWERS_COUNT;
import static com.example.josycom.flowoverstack.util.StringConstants.EXTRA_QUESTION_DATE;
import static com.example.josycom.flowoverstack.util.StringConstants.EXTRA_QUESTION_FULL_TEXT;
import static com.example.josycom.flowoverstack.util.StringConstants.EXTRA_QUESTION_ID;
import static com.example.josycom.flowoverstack.util.StringConstants.EXTRA_QUESTION_NAME;
import static com.example.josycom.flowoverstack.util.StringConstants.EXTRA_QUESTION_OWNER_LINK;
import static com.example.josycom.flowoverstack.util.StringConstants.EXTRA_QUESTION_TITLE;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private String mSearchInput;
    private List<Question> mQuestions;
    private ProgressBar mProgressBar;
    private TextView mErrorMessageTextView;
    private TextInputEditText mTextInputEditText;
    private SearchViewModel mSearchViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mTextInputEditText = findViewById(R.id.text_input_editText);
        final MaterialButton materialButton = findViewById(R.id.search_button);
        mRecyclerView = findViewById(R.id.rv_search_results);
        mProgressBar = findViewById(R.id.search_pb_fetch_data);
        mErrorMessageTextView = findViewById(R.id.search_tv_error);
        FloatingActionButton fab = findViewById(R.id.search_scroll_up_fab);
        NestedScrollView nestedScrollView = findViewById(R.id.search_nested_scrollview);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        fab.setVisibility(View.INVISIBLE);
        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY > 0) {
                fab.setVisibility(View.VISIBLE);
            } else {
                fab.setVisibility(View.INVISIBLE);
            }
        });
        fab.setOnClickListener(v -> nestedScrollView.scrollTo(0, 0));

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
        };

        // What happens when the search button is clicked
        materialButton.setOnClickListener(v -> {
            if (Objects.requireNonNull(mTextInputEditText.getText()).toString().isEmpty()) {
                mTextInputEditText.setError("Type a search query");
            } else {
                mSearchInput = Objects.requireNonNull(mTextInputEditText.getText()).toString();
                mTextInputEditText.getText().clear();
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (inputMethodManager != null) {
                    inputMethodManager.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), 0);
                }
                onLoading();
                makeSearch();
            }
        });
        final SearchAdapter searchAdapter = new SearchAdapter();
        mSearchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);

//        mSearchViewModel.getNetworkState().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(String s) {
//                switch (s) {
//                    case StringConstants.LOADING:
//                        onLoading();
//                        break;
//                    case StringConstants.LOADED:
//                        onLoaded();
//                        break;
//                    case StringConstants.FAILED:
//                        onError();
//                        break;
//                }
//            }
//        });
        mSearchViewModel.getQuestionLiveData().observe(this, questions -> {
            mQuestions = questions;
            if (questions != null) {
                onLoaded();
            } else {
                onError();
            }
            searchAdapter.setQuestions(questions);
        });
        mRecyclerView.setAdapter(searchAdapter);
        searchAdapter.setOnClickListener(mOnClickListener);
    }

    // Gets the ViewModel, Observes the Question LiveData and delivers it to the Recyclerview
    private void makeSearch() {
        mSearchViewModel.setQuery(mSearchInput);
    }

    private void onLoaded() {
        mProgressBar.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mErrorMessageTextView.setVisibility(View.INVISIBLE);
    }

    private void onError() {
        mProgressBar.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageTextView.setVisibility(View.VISIBLE);
    }

    private void onLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageTextView.setVisibility(View.INVISIBLE);
    }

    private void onLoadingMore() {
        mProgressBar.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mErrorMessageTextView.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
