package com.example.josycom.flowoverstack.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.josycom.flowoverstack.R;
import com.example.josycom.flowoverstack.model.Answer;
import com.example.josycom.flowoverstack.util.DateUtil;

import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.List;

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.AnswerViewHolder> {

    private List<Answer> mAnswers = new ArrayList<>();
    @NonNull
    @Override
    public AnswerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.answer_item, parent, false);
        return new AnswerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnswerViewHolder holder, int position) {
        Answer currentAnswer = mAnswers.get(position);

        holder.answerScore.setText(String.valueOf(currentAnswer.getScore()));
        holder.answerName.setText(currentAnswer.getOwner().getDisplayName());
        holder.answerDate.setText(DateUtil.toNormalDate(currentAnswer.getCreationDate()));
        holder.answerBody.setText(Jsoup.parse(currentAnswer.getBody()).text());
    }

    @Override
    public int getItemCount() {
        return mAnswers.size();
    }

    public void setAnswers(List<Answer> answers) {
        mAnswers = answers;
    }

    static class AnswerViewHolder extends RecyclerView.ViewHolder {
        TextView answerBody;
        TextView answerDate;
        TextView answerName;
        TextView answerScore;

        AnswerViewHolder(@NonNull View itemView) {
            super(itemView);
            answerBody = itemView.findViewById(R.id.tv_answer_body_item);
            answerDate = itemView.findViewById(R.id.tv_date_answer_item);
            answerName = itemView.findViewById(R.id.tv_name_answer_item);
            answerScore = itemView.findViewById(R.id.tv_votes_item);
        }
    }
}
