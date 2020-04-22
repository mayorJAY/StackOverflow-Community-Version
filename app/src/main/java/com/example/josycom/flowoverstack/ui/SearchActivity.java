package com.example.josycom.flowoverstack.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.josycom.flowoverstack.R;
import com.example.josycom.flowoverstack.adapters.SearchAdapter;
import com.example.josycom.flowoverstack.model.Owner;
import com.example.josycom.flowoverstack.model.Question;
import com.example.josycom.flowoverstack.repository.SearchRepository;
import com.example.josycom.flowoverstack.util.DateUtil;
import com.example.josycom.flowoverstack.viewmodel.CustomSearchViewModelFactory;
import com.example.josycom.flowoverstack.viewmodel.SearchViewModel;
import com.google.android.material.button.MaterialButton;
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
    private View.OnClickListener mOnClickListener;
    private List<Question> mQuestions;
    private ProgressBar mProgressBar;
    private TextView mErrorMessageTextView;
    private boolean isRecyclerviewDisplayed = false;
    private final String STATE_RECYCLERVIEW = "state_of_recyclerview";
    private TextInputEditText mTextInputEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mTextInputEditText = findViewById(R.id.text_input_editText);
        final MaterialButton materialButton = findViewById(R.id.search_button);
        mRecyclerView = findViewById(R.id.rv_search_results);
        mProgressBar = findViewById(R.id.pb_fetch_data);
        mErrorMessageTextView = findViewById(R.id.tv_error);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        };

        if (savedInstanceState != null) {
            isRecyclerviewDisplayed = savedInstanceState.getBoolean(STATE_RECYCLERVIEW);
        }

        // What happens when the search button is clicked
        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Objects.requireNonNull(mTextInputEditText.getText()).toString().isEmpty()) {
                    mTextInputEditText.setError("Type a search query");
                } else {
                    mSearchInput = Objects.requireNonNull(mTextInputEditText.getText()).toString();
                    mTextInputEditText.setText("");
                    makeSearch();
                }
            }
        });
    }

    // Gets the ViewModel, Observes the Question LiveData and delivers it to the Recyclerview
    private void makeSearch() {
        final SearchAdapter searchAdapter = new SearchAdapter();
        SearchViewModel mSearchViewModel = new ViewModelProvider(this,
                new CustomSearchViewModelFactory(new SearchRepository())).get(SearchViewModel.class);
        mSearchViewModel.setQuery(mSearchInput);
        mSearchViewModel.getQuestionLiveData().observe(this, new Observer<List<Question>>() {
                @Override
                public void onChanged(List<Question> questions) {
                        mQuestions = questions;
                        searchAdapter.setQuestions(questions);
                }
            });
            mRecyclerView.setAdapter(searchAdapter);
            searchAdapter.setOnClickListener(mOnClickListener);
    }

    public void showData() {
        mProgressBar.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mErrorMessageTextView.setVisibility(View.INVISIBLE);
    }

    public void showError() {
        mProgressBar.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageTextView.setVisibility(View.VISIBLE);
    }

    public void hideData() {
        mRecyclerView.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_RECYCLERVIEW, isRecyclerviewDisplayed);
    }
}
