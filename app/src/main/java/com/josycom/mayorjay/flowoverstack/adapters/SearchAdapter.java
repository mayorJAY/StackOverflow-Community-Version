package com.josycom.mayorjay.flowoverstack.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.josycom.mayorjay.flowoverstack.R;
import com.josycom.mayorjay.flowoverstack.databinding.QuestionItemBinding;
import com.josycom.mayorjay.flowoverstack.model.Owner;
import com.josycom.mayorjay.flowoverstack.model.Question;
import com.josycom.mayorjay.flowoverstack.util.DateUtil;

import org.jsoup.Jsoup;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    private static View.OnClickListener mOnClickListener;
    private List<Question> mQuestions;
    private Context context;

    @NonNull
    @Override
    public SearchAdapter.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        QuestionItemBinding questionItemBinding = QuestionItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new SearchViewHolder(questionItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.SearchViewHolder holder, int position) {
        if (mQuestions != null) {
            Question currentQuestion = mQuestions.get(position);
            holder.bind(currentQuestion);
            animateView(holder.itemView, position);
        }
    }

    public void setOnClickListener(View.OnClickListener mOnClickListener) {
        SearchAdapter.mOnClickListener = mOnClickListener;
    }

    @Override
    public int getItemCount() {
        if (mQuestions != null) {
            return mQuestions.size();
        } else {
            return 0;
        }
    }

    void animateView(View view, int position) {
        Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
        animation.setStartOffset(30 * position);
        view.startAnimation(animation);
    }

    public void setQuestions(List<Question> questions) {
        mQuestions = questions;
        notifyDataSetChanged();
    }

    class SearchViewHolder extends RecyclerView.ViewHolder {

        private QuestionItemBinding mQuestionItemBinding;

        SearchViewHolder(QuestionItemBinding questionItemBinding) {
            super(questionItemBinding.getRoot());
            this.mQuestionItemBinding = questionItemBinding;

            mQuestionItemBinding.getRoot().setTag(this);
            mQuestionItemBinding.getRoot().setOnClickListener(mOnClickListener);
        }

        void bind(Question question) {
            if (question != null) {
                Owner owner = question.getOwner();
                String profileImage = owner.getProfileImage();
                List<String> tagList = question.getTags();
                Glide.with(mQuestionItemBinding.getRoot().getContext())
                        .load(profileImage)
                        .placeholder(R.drawable.loading)
                        .into(mQuestionItemBinding.ivAvatarItem);
                mQuestionItemBinding.tvQuestionItem.setText(Jsoup.parse(question.getTitle()).text());
                mQuestionItemBinding.tvViewsCountItem.setText(String.valueOf(question.getViewCount()));
                mQuestionItemBinding.tvDateItem.setText(DateUtil.toNormalDate(question.getCreationDate()));

                if (question.getIsAnswered()) {
                    mQuestionItemBinding.answered.setVisibility(View.VISIBLE);
                } else {
                    mQuestionItemBinding.answered.setVisibility(View.GONE);
                }

                int answers = question.getAnswerCount();
                Resources resources = mQuestionItemBinding.getRoot().getContext().getResources();
                String answerCount = resources.getQuantityString(R.plurals.answers, answers, answers);
                mQuestionItemBinding.tvAnswersCountItem.setText(answerCount);

                if (question.getScore() <= 0){
                    mQuestionItemBinding.tvVotesCountItem.setText(String.valueOf(question.getScore()));
                } else {
                    mQuestionItemBinding.tvVotesCountItem.setText(mQuestionItemBinding.getRoot().getContext()
                            .getString(R.string.plus_score).concat(String.valueOf(question.getScore())));
                }
                mQuestionItemBinding.tvTagsListItem.setText(updateTagsTextView(tagList));
            }
        }

        private String updateTagsTextView(List<String> tagList) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < tagList.size(); i++) {
                builder.append(tagList.get(i));
                if (i != tagList.size() - 1) {
                    builder.append(", ");
                }
            }
            return builder.toString();
        }
    }
}