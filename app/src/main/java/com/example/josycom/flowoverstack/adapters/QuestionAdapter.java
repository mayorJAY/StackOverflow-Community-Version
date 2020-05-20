package com.example.josycom.flowoverstack.adapters;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.josycom.flowoverstack.R;
import com.example.josycom.flowoverstack.databinding.QuestionItemBinding;
import com.example.josycom.flowoverstack.model.Owner;
import com.example.josycom.flowoverstack.model.Question;
import com.example.josycom.flowoverstack.util.DateUtil;

import org.jsoup.Jsoup;

import java.util.List;

public class QuestionAdapter extends PagedListAdapter<Question, QuestionAdapter.QuestionViewHolder> {

    private static View.OnClickListener mOnClickListener;

    private static DiffUtil.ItemCallback<Question> DIFF_CALLBACK = new DiffUtil.ItemCallback<Question>() {
        @Override
        public boolean areItemsTheSame(@NonNull Question oldItem, @NonNull Question newItem) {
            return oldItem.getQuestionId().equals(newItem.getQuestionId());
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull Question oldItem, @NonNull Question newItem) {
            return oldItem.equals(newItem);
        }
    };

    public QuestionAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        QuestionItemBinding questionItemBinding = QuestionItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new QuestionViewHolder(questionItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    static class QuestionViewHolder extends RecyclerView.ViewHolder {
        private QuestionItemBinding mQuestionItemBinding;

        QuestionViewHolder(QuestionItemBinding questionItemBinding) {
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
            } else {
                Log.d("TAG", "No item found");
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
