package com.example.josycom.flowoverstack.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.josycom.flowoverstack.databinding.AnswerItemBinding;
import com.example.josycom.flowoverstack.model.Answer;
import com.example.josycom.flowoverstack.util.DateUtil;

import org.jsoup.Jsoup;

import java.util.List;

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.AnswerViewHolder> {

    private List<Answer> mAnswers;

    @NonNull
    @Override
    public AnswerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AnswerItemBinding answerItemBinding = AnswerItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new AnswerViewHolder(answerItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull AnswerViewHolder holder, int position) {
        if (mAnswers != null) {
            Answer currentAnswer = mAnswers.get(position);
            holder.bind(currentAnswer);
        }
    }

    public void setAnswers(List<Answer> answers) {
        mAnswers = answers;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mAnswers != null) {
            return mAnswers.size();
        } else {
            return 0;
        }
    }

    static class AnswerViewHolder extends RecyclerView.ViewHolder {
        private AnswerItemBinding mAnswerItemBinding;

        AnswerViewHolder(AnswerItemBinding answerItemBinding) {
            super(answerItemBinding.getRoot());
            this.mAnswerItemBinding = answerItemBinding;
        }

        void bind(Answer answer) {
            mAnswerItemBinding.tvVotesItem.setText(String.valueOf(answer.getScore()));
            mAnswerItemBinding.tvAnswerNameItem.setText(answer.getOwner().getDisplayName());
            mAnswerItemBinding.tvAnswerDateItem.setText(DateUtil.toNormalDate(answer.getCreationDate()));
            mAnswerItemBinding.tvAnswerBodyItem.setText(Jsoup.parse(answer.getBody()).text());
        }
    }
}
