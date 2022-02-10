package com.josycom.mayorjay.flowoverstack.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.josycom.mayorjay.flowoverstack.databinding.AnswerItemBinding
import com.josycom.mayorjay.flowoverstack.model.Answer
import com.josycom.mayorjay.flowoverstack.ui.adapters.AnswerAdapter.AnswerViewHolder
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

    fun setOnClickListener(shareOnClickListener: View.OnClickListener?) {
        shareClickListener = shareOnClickListener
    }

    companion object {
        private var shareClickListener: View.OnClickListener? = null
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Answer>() {
            override fun areItemsTheSame(oldItem: Answer, newItem: Answer): Boolean = oldItem.answerId == newItem.answerId

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: Answer, newItem: Answer): Boolean = oldItem == newItem
        }
    }

    class AnswerViewHolder(private val binding: AnswerItemBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.ivShare.setOnClickListener(shareClickListener)
        }

        fun bind(answer: Answer) {
            binding.ivShare.tag = answer
            binding.tvVotesItem.text = answer.score.toString()
            binding.tvAnswerNameItem.text = answer.owner?.displayName
            if (answer.creationDate != null) {
                binding.tvAnswerDateItem.text = AppUtils.toNormalDate(answer.creationDate?.toLong()
                        ?: 0L)
            }
            binding.tvAnswerBodyItem.text = Jsoup.parse(answer.body).text()
        }
    }
}