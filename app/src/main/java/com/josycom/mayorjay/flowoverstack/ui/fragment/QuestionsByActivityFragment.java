package com.josycom.mayorjay.flowoverstack.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.josycom.mayorjay.flowoverstack.R;
import com.josycom.mayorjay.flowoverstack.adapters.QuestionAdapter;
import com.josycom.mayorjay.flowoverstack.databinding.FragmentQuestionsByActivityBinding;
import com.josycom.mayorjay.flowoverstack.model.Owner;
import com.josycom.mayorjay.flowoverstack.model.Question;
import com.josycom.mayorjay.flowoverstack.ui.activity.AnswerActivity;
import com.josycom.mayorjay.flowoverstack.util.DateUtil;
import com.josycom.mayorjay.flowoverstack.util.AppConstants;
import com.josycom.mayorjay.flowoverstack.viewmodel.CustomQuestionViewModelFactory;
import com.josycom.mayorjay.flowoverstack.viewmodel.QuestionViewModel;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

import static com.josycom.mayorjay.flowoverstack.util.AppConstants.EXTRA_AVATAR_ADDRESS;
import static com.josycom.mayorjay.flowoverstack.util.AppConstants.EXTRA_QUESTION_ANSWERS_COUNT;
import static com.josycom.mayorjay.flowoverstack.util.AppConstants.EXTRA_QUESTION_DATE;
import static com.josycom.mayorjay.flowoverstack.util.AppConstants.EXTRA_QUESTION_FULL_TEXT;
import static com.josycom.mayorjay.flowoverstack.util.AppConstants.EXTRA_QUESTION_ID;
import static com.josycom.mayorjay.flowoverstack.util.AppConstants.EXTRA_QUESTION_NAME;
import static com.josycom.mayorjay.flowoverstack.util.AppConstants.EXTRA_QUESTION_OWNER_LINK;
import static com.josycom.mayorjay.flowoverstack.util.AppConstants.EXTRA_QUESTION_TITLE;
import static com.josycom.mayorjay.flowoverstack.util.AppConstants.EXTRA_QUESTION_VOTES_COUNT;

/**
 * This fragment houses the Active Questions
 */
public class QuestionsByActivityFragment extends Fragment {

    private FragmentQuestionsByActivityBinding mFragmentQuestionsByActivityBinding;
    private PagedList<Question> mQuestions;
    private View.OnClickListener mOnClickListener;
    @Inject
    CustomQuestionViewModelFactory viewModelFactory;

    @Override
    public void onAttach(@NonNull Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);

        viewModelFactory.setInputs(AppConstants.FIRST_PAGE,
                AppConstants.PAGE_SIZE,
                AppConstants.ORDER_DESCENDING,
                AppConstants.SORT_BY_ACTIVITY,
                AppConstants.SITE,
                AppConstants.QUESTION_FILTER,
                AppConstants.API_KEY);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentQuestionsByActivityBinding = FragmentQuestionsByActivityBinding.inflate(inflater, container, false);
        mFragmentQuestionsByActivityBinding.activitySwipeContainer.setColorSchemeResources(R.color.colorPrimaryLight);
        mFragmentQuestionsByActivityBinding.activityScrollUpFab.setVisibility(View.INVISIBLE);

        mFragmentQuestionsByActivityBinding.activityRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    mFragmentQuestionsByActivityBinding.activityScrollUpFab.setVisibility(View.VISIBLE);
                } else {
                    mFragmentQuestionsByActivityBinding.activityScrollUpFab.setVisibility(View.INVISIBLE);
                }
            }
        });
        mFragmentQuestionsByActivityBinding.activityScrollUpFab.setOnClickListener(v ->
                mFragmentQuestionsByActivityBinding.activityRecyclerView.scrollToPosition(0));

        mOnClickListener = v -> {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) v.getTag();
            int position = viewHolder.getAdapterPosition();
            Intent answerActivityIntent = new Intent(getContext(), AnswerActivity.class);
            Question currentQuestion = mQuestions.get(position);
            assert currentQuestion != null;
            Owner questionOwner = currentQuestion.getOwner();

            answerActivityIntent.putExtra(EXTRA_QUESTION_TITLE, currentQuestion.getTitle());
            answerActivityIntent.putExtra(EXTRA_QUESTION_NAME, questionOwner.getDisplayName());
            answerActivityIntent.putExtra(EXTRA_QUESTION_DATE,
                    DateUtil.toNormalDate(currentQuestion.getCreationDate()));
            answerActivityIntent.putExtra(EXTRA_QUESTION_FULL_TEXT, currentQuestion.getBody());
            answerActivityIntent.putExtra(EXTRA_AVATAR_ADDRESS, questionOwner.getProfileImage());
            answerActivityIntent.putExtra(EXTRA_QUESTION_ANSWERS_COUNT, currentQuestion.getAnswerCount());
            answerActivityIntent.putExtra(EXTRA_QUESTION_ID, currentQuestion.getQuestionId());
            answerActivityIntent.putExtra(EXTRA_QUESTION_VOTES_COUNT, currentQuestion.getScore());
            answerActivityIntent.putExtra(EXTRA_QUESTION_OWNER_LINK, questionOwner.getLink());

            startActivity(answerActivityIntent);
        };
        handleRecyclerView();
        return mFragmentQuestionsByActivityBinding.getRoot();
    }

    private void handleRecyclerView() {
        final QuestionAdapter questionAdapter = new QuestionAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mFragmentQuestionsByActivityBinding.activityRecyclerView.setLayoutManager(linearLayoutManager);
        mFragmentQuestionsByActivityBinding.activityRecyclerView.setItemAnimator(new DefaultItemAnimator());

        QuestionViewModel questionViewModel = new ViewModelProvider(this, viewModelFactory).get(QuestionViewModel.class);

        questionViewModel.getNetworkState().observe(getViewLifecycleOwner(), s -> {
            switch (s) {
                case AppConstants.LOADING:
                    onLoading();
                    break;
                case AppConstants.LOADED:
                    onLoaded();
                    break;
                case AppConstants.FAILED:
                    onError();
                    break;
            }
        });
        questionViewModel.getQuestionPagedList().observe(getViewLifecycleOwner(), questions -> {
            mQuestions = questions;
            questionAdapter.submitList(questions);
        });
        mFragmentQuestionsByActivityBinding.activityRecyclerView.setAdapter(questionAdapter);
        questionAdapter.setOnClickListener(mOnClickListener);
        mFragmentQuestionsByActivityBinding.activitySwipeContainer.setOnRefreshListener(() -> {
            questionViewModel.refresh();
            mFragmentQuestionsByActivityBinding.activitySwipeContainer.setRefreshing(false);
        });
    }

    private void onLoaded() {
        mFragmentQuestionsByActivityBinding.activityPbFetchData.setVisibility(View.INVISIBLE);
        mFragmentQuestionsByActivityBinding.activityRecyclerView.setVisibility(View.VISIBLE);
        mFragmentQuestionsByActivityBinding.activityTvError.setVisibility(View.INVISIBLE);
    }

    private void onError() {
        mFragmentQuestionsByActivityBinding.activityPbFetchData.setVisibility(View.INVISIBLE);
        mFragmentQuestionsByActivityBinding.activityRecyclerView.setVisibility(View.INVISIBLE);
        mFragmentQuestionsByActivityBinding.activityTvError.setVisibility(View.VISIBLE);
    }

    private void onLoading() {
        mFragmentQuestionsByActivityBinding.activityPbFetchData.setVisibility(View.VISIBLE);
        mFragmentQuestionsByActivityBinding.activityRecyclerView.setVisibility(View.VISIBLE);
        mFragmentQuestionsByActivityBinding.activityTvError.setVisibility(View.INVISIBLE);
    }
}