package com.example.josycom.flowoverstack.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.josycom.flowoverstack.R;
import com.example.josycom.flowoverstack.adapters.AnswerAdapter;
import com.example.josycom.flowoverstack.util.StringConstants;
import com.example.josycom.flowoverstack.viewmodel.AnswerViewModel;
import com.example.josycom.flowoverstack.viewmodel.CustomAnswerViewModelFactory;

import org.jsoup.Jsoup;

import java.util.Objects;

public class AnswerActivity extends AppCompatActivity {

    private AnswerAdapter mAnswerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        TextView questionTitleTextView = findViewById(R.id.tv_question_detail);
        TextView fullQuestionTextView = findViewById(R.id.tv_full_question_detail);
        TextView dateQuestionTextView = findViewById(R.id.tv_date_question_detail);
        TextView nameQuestionTextView = findViewById(R.id.tv_name_question_detail);
        ImageView avatarQuestionImageView = findViewById(R.id.iv_avatar_question_detail);
        RecyclerView answersRecyclerView = findViewById(R.id.rv_answers);

        answersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAnswerAdapter = new AnswerAdapter();

        Intent mIntent = getIntent();

        questionTitleTextView.setText(Jsoup.parse(Objects.requireNonNull(mIntent.getStringExtra(StringConstants.EXTRA_QUESTION_TITLE))).text());
        fullQuestionTextView.setText(Jsoup.parse(Objects.requireNonNull(mIntent.getStringExtra(StringConstants.EXTRA_QUESTION_FULL_TEXT))).text());
        dateQuestionTextView.setText(mIntent.getStringExtra(StringConstants.EXTRA_QUESTION_DATE));
        nameQuestionTextView.setText(mIntent.getStringExtra(StringConstants.EXTRA_QUESTION_NAME));
        int questionId = mIntent.getIntExtra(StringConstants.EXTRA_QUESTION_ID, 0);
        String avatarAddress = mIntent.getStringExtra(StringConstants.EXTRA_AVATAR_ADDRESS);
        String ownerQuestionLink = mIntent.getStringExtra(StringConstants.EXTRA_QUESTION_OWNER_LINK);
        Glide.with(this)
                .load(avatarAddress)
                .placeholder(R.drawable.loading)
                .into(avatarQuestionImageView);

        AnswerViewModel answerViewModel = new ViewModelProvider(this,
                new CustomAnswerViewModelFactory(questionId,
                        StringConstants.ORDER_DESCENDING,
                        StringConstants.SORT_BY_ACTIVITY,
                        StringConstants.SITE,
                        StringConstants.ANSWER_FILTER)).get(AnswerViewModel.class);
        answerViewModel.getAnswersLiveData().observe(this, answers -> mAnswerAdapter.setAnswers(answers));
        answersRecyclerView.setAdapter(mAnswerAdapter);
    }
}
