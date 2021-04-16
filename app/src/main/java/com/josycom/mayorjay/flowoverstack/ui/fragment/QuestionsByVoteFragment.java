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
import com.josycom.mayorjay.flowoverstack.databinding.FragmentQuestionsByVoteBinding;
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
 * This fragment houses the Voted Questions
 */
public class QuestionsByVoteFragment extends Fragment {

    private FragmentQuestionsByVoteBinding mFragmentQuestionsByVoteBinding;
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
                AppConstants.SORT_BY_VOTES,
                AppConstants.SITE,
                AppConstants.QUESTION_FILTER,
                AppConstants.API_KEY);
    }



    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentQuestionsByVoteBinding = FragmentQuestionsByVoteBinding.inflate(inflater, container, false);
        mFragmentQuestionsByVoteBinding.voteSwipeContainer.setColorSchemeResources(R.color.colorPrimaryLight);
        mFragmentQuestionsByVoteBinding.voteScrollUpFab.setVisibility(View.INVISIBLE);

        mFragmentQuestionsByVoteBinding.voteRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    mFragmentQuestionsByVoteBinding.voteScrollUpFab.setVisibility(View.VISIBLE);
                } else {
                    mFragmentQuestionsByVoteBinding.voteScrollUpFab.setVisibility(View.INVISIBLE);
                }
            }
        });
        mFragmentQuestionsByVoteBinding.voteScrollUpFab.setOnClickListener(v ->
                mFragmentQuestionsByVoteBinding.voteRecyclerView.scrollToPosition(0));

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
        return mFragmentQuestionsByVoteBinding.getRoot();
    }

    private void handleRecyclerView() {
        final QuestionAdapter questionAdapter = new QuestionAdapter();
        mFragmentQuestionsByVoteBinding.voteRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mFragmentQuestionsByVoteBinding.voteRecyclerView.setItemAnimator(new DefaultItemAnimator());

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
        mFragmentQuestionsByVoteBinding.voteRecyclerView.setAdapter(questionAdapter);
        questionAdapter.setOnClickListener(mOnClickListener);
        mFragmentQuestionsByVoteBinding.voteSwipeContainer.setOnRefreshListener(() -> {
            questionViewModel.refresh();
            mFragmentQuestionsByVoteBinding.voteSwipeContainer.setRefreshing(false);
        });
    }

    private void onLoaded() {
        mFragmentQuestionsByVoteBinding.votePbFetchData.setVisibility(View.INVISIBLE);
        mFragmentQuestionsByVoteBinding.voteRecyclerView.setVisibility(View.VISIBLE);
        mFragmentQuestionsByVoteBinding.voteTvError.setVisibility(View.INVISIBLE);
    }

    private void onError() {
        mFragmentQuestionsByVoteBinding.votePbFetchData.setVisibility(View.INVISIBLE);
        mFragmentQuestionsByVoteBinding.voteRecyclerView.setVisibility(View.INVISIBLE);
        mFragmentQuestionsByVoteBinding.voteTvError.setVisibility(View.VISIBLE);
    }

    private void onLoading() {
        mFragmentQuestionsByVoteBinding.votePbFetchData.setVisibility(View.VISIBLE);
        mFragmentQuestionsByVoteBinding.voteRecyclerView.setVisibility(View.INVISIBLE);
        mFragmentQuestionsByVoteBinding.voteTvError.setVisibility(View.INVISIBLE);
    }
}