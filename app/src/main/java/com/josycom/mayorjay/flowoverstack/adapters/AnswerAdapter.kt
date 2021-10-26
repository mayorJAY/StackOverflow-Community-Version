package com.josycom.mayorjay.flowoverstack.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.josycom.mayorjay.flowoverstack.adapters.AnswerAdapter.AnswerViewHolder
import com.josycom.mayorjay.flowoverstack.databinding.AnswerItemBinding
import com.josycom.mayorjay.flowoverstack.model.Answer
import com.josycom.mayorjay.flowoverstack.util.AppUtils
import org.jsoup.Jsoup

class AnswerAdapter : RecyclerView.Adapter<AnswerViewHolder>() {

    private var mAnswers: List<Answer>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerViewHolder {
        val answerItemBinding = AnswerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AnswerViewHolder(answerItemBinding)
    }

    override fun onBindViewHolder(holder: AnswerViewHolder, position: Int) {
        if (mAnswers != null) {
            val currentAnswer = mAnswers!![position]
            holder.bind(currentAnswer)
        }
    }

    fun setAnswers(answers: List<Answer>?) {
        mAnswers = answers
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return if (mAnswers != null) mAnswers!!.size else 0
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