package com.josycom.mayorjay.flowoverstack.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    @NonNull
    @Override
    public SearchAdapter.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        QuestionItemBinding questionItemBinding = QuestionItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new SearchViewHolder(questionItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.SearchViewHolder holder, int position) {
        if (mQuestions != null) {
            Question currentQuestion = mQuestions.get(position);
            holder.bind(currentQuestion);
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

    public void setQuestions(List<Question> questions) {
        mQuestions = questions;
        notifyDataSetChanged();
    }

    static class SearchViewHolder extends RecyclerView.ViewHolder {

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
                mQuestionItemBinding.tvAnswersCountItem.setText(String.valueOf(question.getAnswerCount()));
                mQuestionItemBinding.tvVotesCountItem.setText(String.valueOf(question.getScore()));
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
