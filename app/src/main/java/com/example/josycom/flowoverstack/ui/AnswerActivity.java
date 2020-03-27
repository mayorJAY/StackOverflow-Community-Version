package com.example.josycom.flowoverstack.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
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
import com.example.josycom.flowoverstack.model.Answer;
import com.example.josycom.flowoverstack.model.AnswerResponse;
import com.example.josycom.flowoverstack.model.QuestionsResponse;
import com.example.josycom.flowoverstack.network.ApiService;
import com.example.josycom.flowoverstack.network.RestApiClient;
import com.example.josycom.flowoverstack.util.StringConstants;
import com.example.josycom.flowoverstack.viewmodel.AnswerViewModel;
import com.example.josycom.flowoverstack.viewmodel.CustomAnswerViewModelFactory;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnswerActivity extends AppCompatActivity {

    private TextView mNameQuestionTextView;
    private ImageView mAvatarQuestionImageView;
    private RecyclerView mAnswersRecyclerView;
    private TextView mAnswersCountTextView;
    private int mQuestionId;
    private String mAvatarAddress;
    private String mOwnerQuestionLink;
    private AnswerViewModel mAnswerViewModel;
    private AnswerAdapter mAnswerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        TextView questionTitleTextView = findViewById(R.id.tv_question_detail);
        TextView fullQuestionTextView = findViewById(R.id.tv_full_question_detail);
        TextView dateQuestionTextView = findViewById(R.id.tv_date_question_detail);
        mNameQuestionTextView = findViewById(R.id.tv_name_question_detail);
        mAvatarQuestionImageView = findViewById(R.id.iv_avatar_question_detail);
        mAnswersRecyclerView = findViewById(R.id.rv_answers);
        mAnswersCountTextView = findViewById(R.id.tv_answers_count_detail);

        mAnswersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAnswerAdapter = new AnswerAdapter();

        Intent intent = getIntent();

        questionTitleTextView.setText(intent.getStringExtra(StringConstants.EXTRA_QUESTION_TITLE));
        fullQuestionTextView.setText(intent.getStringExtra(StringConstants.EXTRA_QUESTION_FULL_TEXT));
        dateQuestionTextView.setText(intent.getStringExtra(StringConstants.EXTRA_QUESTION_DATE));
        mNameQuestionTextView.setText(intent.getStringExtra(StringConstants.EXTRA_QUESTION_NAME));
        mAnswersCountTextView.setText(String.valueOf(intent.getIntExtra(StringConstants.EXTRA_QUESTION_ANSWERS_COUNT, 0)));
        mQuestionId = intent.getIntExtra(StringConstants.EXTRA_QUESTION_ID, 0);
        mAvatarAddress = intent.getStringExtra(StringConstants.EXTRA_AVATAR_ADDRESS);
        mOwnerQuestionLink = intent.getStringExtra(StringConstants.EXTRA_QUESTION_OWNER_LINK);
        Glide.with(this)
                .load(mAvatarAddress)
                .placeholder(R.drawable.loading)
                .into(mAvatarQuestionImageView);
    }

    @Override
    protected void onResume() {
        mAnswerViewModel = new ViewModelProvider(this,
                new CustomAnswerViewModelFactory(mQuestionId)).get(AnswerViewModel.class);
        mAnswerViewModel.getAnswerLiveData().observe(this, new Observer<List<Answer>>() {
            @Override
            public void onChanged(List<Answer> answers) {
                mAnswerAdapter.setAnswers(answers);
            }
        });
        mAnswersRecyclerView.setAdapter(mAnswerAdapter);

        super.onResume();
    }
}
