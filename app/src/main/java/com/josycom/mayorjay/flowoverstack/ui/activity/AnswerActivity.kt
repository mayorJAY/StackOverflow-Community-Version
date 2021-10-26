package com.josycom.mayorjay.flowoverstack.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.josycom.mayorjay.flowoverstack.R;
import com.josycom.mayorjay.flowoverstack.adapters.AnswerAdapter;
import com.josycom.mayorjay.flowoverstack.databinding.ActivityAnswerBinding;
import com.josycom.mayorjay.flowoverstack.model.Answer;
import com.josycom.mayorjay.flowoverstack.util.AppUtils;
import com.josycom.mayorjay.flowoverstack.util.AppConstants;
import com.josycom.mayorjay.flowoverstack.viewmodel.AnswerViewModel;
import com.josycom.mayorjay.flowoverstack.viewmodel.CustomAnswerViewModelFactory;

import org.jsoup.Jsoup;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class AnswerActivity extends AppCompatActivity {

    private ActivityAnswerBinding mActivityAnswerBinding;
    private AnswerAdapter mAnswerAdapter;
    private String mOwnerQuestionLink;
    private List<Answer> mAnswers;
    @Inject
    CustomAnswerViewModelFactory viewModelFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);

        mActivityAnswerBinding = ActivityAnswerBinding.inflate(getLayoutInflater());
        setContentView(mActivityAnswerBinding.getRoot());

        mActivityAnswerBinding.rvAnswers.setLayoutManager(new LinearLayoutManager(this));
        mAnswerAdapter = new AnswerAdapter();

        Intent mIntent = getIntent();
        mActivityAnswerBinding.tvQuestionDetail.setText(Jsoup.parse(Objects.requireNonNull(mIntent.getStringExtra(AppConstants.EXTRA_QUESTION_TITLE))).text());
        mActivityAnswerBinding.markDownView.setMarkDownText(mIntent.getStringExtra(AppConstants.EXTRA_QUESTION_FULL_TEXT));
        mActivityAnswerBinding.tvDateQuestionDetail.setText(mIntent.getStringExtra(AppConstants.EXTRA_QUESTION_DATE));
        mActivityAnswerBinding.tvNameQuestionDetail.setText(mIntent.getStringExtra(AppConstants.EXTRA_QUESTION_NAME));
        int voteCount = mIntent.getIntExtra(AppConstants.EXTRA_QUESTION_VOTES_COUNT, 0);
        if (voteCount <= 0){
            mActivityAnswerBinding.tvVotesCountItem.setText(String.valueOf(voteCount));
        } else {
            mActivityAnswerBinding.tvVotesCountItem.setText(getString(R.string.plus_score).concat(String.valueOf(voteCount)));
        }

        int questionId = mIntent.getIntExtra(AppConstants.EXTRA_QUESTION_ID, 0);
        String avatarAddress = mIntent.getStringExtra(AppConstants.EXTRA_AVATAR_ADDRESS);
        mOwnerQuestionLink = mIntent.getStringExtra(AppConstants.EXTRA_QUESTION_OWNER_LINK);
        Glide.with(this)
                .load(avatarAddress)
                .placeholder(R.drawable.loading)
                .into(mActivityAnswerBinding.ivAvatarQuestionDetail);

        viewModelFactory.setInputs(questionId,
                AppConstants.ORDER_DESCENDING,
                AppConstants.SORT_BY_ACTIVITY,
                AppConstants.SITE,
                AppConstants.ANSWER_FILTER,
                AppConstants.API_KEY);

        AnswerViewModel answerViewModel = new ViewModelProvider(this,
                viewModelFactory).get(AnswerViewModel.class);
        answerViewModel.getAnswersLiveData().observe(this, answers -> {
            mAnswers = answers;
            if (mAnswers.size() == 0) {
                mActivityAnswerBinding.tvNoAnswerQuestionDetail.setVisibility(View.VISIBLE);
            } else {
                mActivityAnswerBinding.tvNoAnswerQuestionDetail.setVisibility(View.INVISIBLE);
                mAnswerAdapter.setAnswers(mAnswers);
            }
        });
        mActivityAnswerBinding.rvAnswers.setAdapter(mAnswerAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAnswers.clear();
        mAnswerAdapter.notifyDataSetChanged();
    }

    public void openProfileOnWeb(View view) {
        AppUtils.directLinkToBrowser(this, mOwnerQuestionLink);
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
        if (mActivityAnswerBinding.markDownView.canGoBack()) {
            mActivityAnswerBinding.markDownView.goBack();
        } else {
            super.onBackPressed();
            finish();
        }
    }
}
