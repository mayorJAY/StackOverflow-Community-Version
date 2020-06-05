package com.josycom.mayorjay.flowoverstack.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.josycom.mayorjay.flowoverstack.R;
import com.josycom.mayorjay.flowoverstack.adapters.AnswerAdapter;
import com.josycom.mayorjay.flowoverstack.databinding.ActivityAnswerBinding;
import com.josycom.mayorjay.flowoverstack.util.AppUtils;
import com.josycom.mayorjay.flowoverstack.util.StringConstants;
import com.josycom.mayorjay.flowoverstack.viewmodel.AnswerViewModel;
import com.josycom.mayorjay.flowoverstack.viewmodel.CustomAnswerViewModelFactory;

import org.jsoup.Jsoup;

import java.util.Objects;

public class AnswerActivity extends AppCompatActivity {

    private ActivityAnswerBinding mActivityAnswerBinding;
    private AnswerAdapter mAnswerAdapter;
    private String mOwnerQuestionLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityAnswerBinding = ActivityAnswerBinding.inflate(getLayoutInflater());
        setContentView(mActivityAnswerBinding.getRoot());

        mActivityAnswerBinding.rvAnswers.setLayoutManager(new LinearLayoutManager(this));
        mAnswerAdapter = new AnswerAdapter();

        Intent mIntent = getIntent();
        mActivityAnswerBinding.tvQuestionDetail.setText(Jsoup.parse(Objects.requireNonNull(mIntent.getStringExtra(StringConstants.EXTRA_QUESTION_TITLE))).text());
        mActivityAnswerBinding.tvFullQuestionDetail.setText(HtmlCompat.fromHtml(Objects.requireNonNull(mIntent.getStringExtra(StringConstants.EXTRA_QUESTION_FULL_TEXT)), 0));
        mActivityAnswerBinding.tvDateQuestionDetail.setText(mIntent.getStringExtra(StringConstants.EXTRA_QUESTION_DATE));
        mActivityAnswerBinding.tvNameQuestionDetail.setText(mIntent.getStringExtra(StringConstants.EXTRA_QUESTION_NAME));
        int questionId = mIntent.getIntExtra(StringConstants.EXTRA_QUESTION_ID, 0);
        String avatarAddress = mIntent.getStringExtra(StringConstants.EXTRA_AVATAR_ADDRESS);
        mOwnerQuestionLink = mIntent.getStringExtra(StringConstants.EXTRA_QUESTION_OWNER_LINK);
        Glide.with(this)
                .load(avatarAddress)
                .placeholder(R.drawable.loading)
                .into(mActivityAnswerBinding.ivAvatarQuestionDetail);

        AnswerViewModel answerViewModel = new ViewModelProvider(this,
                new CustomAnswerViewModelFactory(questionId,
                        StringConstants.ORDER_DESCENDING,
                        StringConstants.SORT_BY_ACTIVITY,
                        StringConstants.SITE,
                        StringConstants.ANSWER_FILTER,
                        StringConstants.API_KEY)).get(AnswerViewModel.class);
        answerViewModel.getAnswersLiveData().observe(this, answers -> {
            if (answers.size() == 0) {
                mActivityAnswerBinding.tvNoAnswerQuestionDetail.setVisibility(View.VISIBLE);
            } else {
                mAnswerAdapter.setAnswers(answers);
            }
        });
        mActivityAnswerBinding.rvAnswers.setAdapter(mAnswerAdapter);
    }

    public void openProfileOnWeb(View view) {
        AppUtils.directLinkToBrowser(this, mOwnerQuestionLink);
        /*Uri webPage = Uri.parse(mOwnerQuestionLink);
        Intent intent = new Intent(Intent.ACTION_VIEW, webPage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }*/
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
