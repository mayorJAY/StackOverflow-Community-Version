package com.example.josycom.flowoverstack.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.josycom.flowoverstack.R;
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

    public QuestionAdapter(){
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_item, parent, false);
            return new QuestionViewHolder(itemView);
        }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    static class QuestionViewHolder extends RecyclerView.ViewHolder{
        ImageView mAvatarView;
        TextView mTitleQuestionText, mViewCounterText, mDateText, mNameText, mAnswersCountText, mVotesCountText, mTagsText;

       QuestionViewHolder(@NonNull View itemView) {
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
            if (question != null){
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
            } else {
                Toast.makeText(itemView.getContext(), "No item found", Toast.LENGTH_SHORT).show();
            }
        }

        private String updateTagsTextView(List<String> tagList){
            StringBuilder builder = new StringBuilder();
            builder.append("Tags: ");
            for (int i = 0; i < tagList.size(); i++){
                builder.append(tagList.get(i));
                if (i != tagList.size() - 1){
                    builder.append(", ");
                }
            }
            return builder.toString();
        }
    }
}
