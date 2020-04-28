package com.example.josycom.flowoverstack.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.josycom.flowoverstack.R;
import com.example.josycom.flowoverstack.model.Owner;
import com.example.josycom.flowoverstack.model.Question;
import com.example.josycom.flowoverstack.util.DateUtil;

import org.jsoup.Jsoup;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    private static View.OnClickListener mOnClickListener;
    private List<Question> mQuestions;

    @NonNull
    @Override
    public SearchAdapter.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_item, parent, false);
        return new SearchViewHolder(view);
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

        ImageView mAvatarView;
        TextView mTitleQuestionText, mViewCounterText, mDateText, mNameText, mAnswersCountText, mVotesCountText, mTagsText;

        SearchViewHolder(@NonNull View itemView) {
            super(itemView);

            mAvatarView = itemView.findViewById(R.id.iv_avatar_item);
            mTitleQuestionText = itemView.findViewById(R.id.tv_question_item);
            mViewCounterText = itemView.findViewById(R.id.tv_counter_item);
            mDateText = itemView.findViewById(R.id.tv_date_item);
            mNameText = itemView.findViewById(R.id.tv_name_item);
            mAnswersCountText = itemView.findViewById(R.id.tv_answers_count_item);
            mVotesCountText = itemView.findViewById(R.id.tv_votes_count_item);
            mTagsText = itemView.findViewById(R.id.tv_tags_list_item);

            itemView.setTag(this);
            itemView.setOnClickListener(mOnClickListener);
        }

        void bind(Question question) {
            if (question != null) {
                Owner owner = question.getOwner();
                String profileImage = owner.getProfileImage();
                List<String> tagList = question.getTags();
                Glide.with(itemView.getContext())
                        .load(profileImage)
                        .placeholder(R.drawable.loading)
                        .into(mAvatarView);
                mTitleQuestionText.setText(Jsoup.parse(question.getTitle()).text());
                mViewCounterText.setText(String.valueOf(question.getViewCount()));
                mDateText.setText(DateUtil.toNormalDate(question.getCreationDate()));
                mNameText.setText(owner.getDisplayName());
                mAnswersCountText.setText(String.valueOf(question.getAnswerCount()));
                mVotesCountText.setText(String.valueOf(question.getScore()));
                mTagsText.setText(updateTagsTextView(tagList));
            }
        }

        private String updateTagsTextView(List<String> tagList) {
            StringBuilder builder = new StringBuilder();
            builder.append("Tags: ");
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
