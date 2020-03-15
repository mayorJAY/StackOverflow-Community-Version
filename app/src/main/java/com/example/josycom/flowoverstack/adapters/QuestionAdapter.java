package com.example.josycom.flowoverstack.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.example.josycom.flowoverstack.network.NetworkState;
import com.example.josycom.flowoverstack.util.DateUtil;

import java.util.List;

public class QuestionAdapter extends PagedListAdapter<Question, QuestionAdapter.QuestionViewHolder> {

    private Context mContext;
    private static final int QUESTION_ITEM_VIEW_TYPE = 1;
    private static final int LOAD_ITEM_VIEW_TYPE = 0;
    private NetworkState mNetworkState;

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

    public QuestionAdapter(Context context){
        super(DIFF_CALLBACK);
        this.mContext = context;
    }

    @Override
    public int getItemViewType(int position) {
        return (isLoadingData() && position == getItemCount() - 1) ? LOAD_ITEM_VIEW_TYPE : QUESTION_ITEM_VIEW_TYPE;
    }

    private boolean isLoadingData() {
        return (mNetworkState != null && mNetworkState != NetworkState.LOADED);
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView;
        if (viewType == QUESTION_ITEM_VIEW_TYPE){
            itemView = inflater.inflate(R.layout.question_item, parent, false);
            return new QuestionViewHolder(itemView);
        } else {
            itemView = inflater.inflate(R.layout.load_progress_item, parent, false);
            return new ProgressViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        Question question = getItem(position);
        holder.bind(question);
    }

    public void setNetworkState(NetworkState networkState){
        boolean wasLoading = isLoadingData();
        mNetworkState = networkState;
        boolean willLoad = isLoadingData();
        if (wasLoading != willLoad){
            if (wasLoading)
                notifyItemRemoved(getItemCount());
            else notifyItemInserted(getItemCount());
        }
    }

    static class QuestionViewHolder extends RecyclerView.ViewHolder{
        ImageView mAvatarView;
        TextView mTitleQuestionText, mViewCounterText, mDateText, mNameText, mAnswersCountText, mTagsText;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            mAvatarView = itemView.findViewById(R.id.iv_avatar_item);
            mTitleQuestionText = itemView.findViewById(R.id.tv_question_item);
            mViewCounterText = itemView.findViewById(R.id.tv_counter_item);
            mDateText = itemView.findViewById(R.id.tv_date_item);
            mNameText = itemView.findViewById(R.id.tv_name_item);
            mAnswersCountText = itemView.findViewById(R.id.tv_answers_count_item);
            mTagsText = itemView.findViewById(R.id.tv_tags_list_item);
        }

        public void bind(Question question) {
            if (question != null){
                Owner owner = question.getOwner();
                String profileImage = owner.getProfileImage();
                List<String> tagList = question.getTags();
                Glide.with(itemView.getContext())
                        .load(profileImage)
                        .into(mAvatarView);
                mTitleQuestionText.setText(question.getTitle());
                mViewCounterText.setText(question.getViewCount());
                mDateText.setText(DateUtil.toNormalDate(question.getCreationDate()));
                mNameText.setText(owner.getDisplayName());
                mAnswersCountText.setText(question.getAnswerCount());
                mTagsText.setText(updateTagsTextView(tagList));
            } else {
                Toast.makeText(itemView.getContext(), "No item found", Toast.LENGTH_SHORT).show();
            }
        }

        private String updateTagsTextView(List<String> tagList){
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < tagList.size(); i++){
                builder.append(tagList.get(i));
                if (i != tagList.size() - 1){
                    builder.append(", ");
                }
            }
            return builder.toString();
        }
    }

    private static class ProgressViewHolder extends QuestionViewHolder {
        public ProgressViewHolder(View itemView) {
            super(itemView);
        }
    }
}
