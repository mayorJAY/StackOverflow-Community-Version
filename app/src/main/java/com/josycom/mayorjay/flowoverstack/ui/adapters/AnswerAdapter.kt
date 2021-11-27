package com.josycom.mayorjay.flowoverstack.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.josycom.mayorjay.flowoverstack.ui.adapters.AnswerAdapter.AnswerViewHolder
import com.josycom.mayorjay.flowoverstack.databinding.AnswerItemBinding
import com.josycom.mayorjay.flowoverstack.model.Answer
import com.josycom.mayorjay.flowoverstack.util.AppUtils
import org.jsoup.Jsoup

class AnswerAdapter : PagingDataAdapter<Answer, AnswerViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerViewHolder {
        val answerItemBinding = AnswerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AnswerViewHolder(answerItemBinding)
    }

    override fun onBindViewHolder(holder: AnswerViewHolder, position: Int) {
        val currentAnswer = getItem(position)
        if (currentAnswer != null) {
            holder.bind(currentAnswer)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object: DiffUtil.ItemCallback<Answer>() {
            override fun areItemsTheSame(oldItem: Answer, newItem: Answer): Boolean = oldItem.answerId == newItem.answerId

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: Answer, newItem: Answer): Boolean = oldItem == newItem
        }
    }

    class AnswerViewHolder(private val mAnswerItemBinding: AnswerItemBinding) : RecyclerView.ViewHolder(mAnswerItemBinding.root) {

        fun bind(answer: Answer) {
            mAnswerItemBinding.tvVotesItem.text = answer.score.toString()
            mAnswerItemBinding.tvAnswerNameItem.text = answer.owner!!.displayName
            if (answer.creationDate != null) {
                mAnswerItemBinding.tvAnswerDateItem.text = AppUtils.toNormalDate(answer.creationDate!!.toLong())
            }
            mAnswerItemBinding.tvAnswerBodyItem.text = Jsoup.parse(answer.body).text()
        }
    }
}