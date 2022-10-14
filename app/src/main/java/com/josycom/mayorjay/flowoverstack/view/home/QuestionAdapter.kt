package com.josycom.mayorjay.flowoverstack.view.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.josycom.mayorjay.flowoverstack.R
import com.josycom.mayorjay.flowoverstack.data.model.Question
import com.josycom.mayorjay.flowoverstack.databinding.QuestionItemBinding
import com.josycom.mayorjay.flowoverstack.util.AppUtils
import com.josycom.mayorjay.flowoverstack.view.home.QuestionAdapter.QuestionViewHolder
import org.jsoup.Jsoup

class QuestionAdapter : PagingDataAdapter<Question, QuestionViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val questionItemBinding = QuestionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuestionViewHolder(questionItemBinding)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    fun setOnClickListeners(onClickListener: View.OnClickListener?, shareOnClickListener: View.OnClickListener?) {
        viewHolderClickListener = onClickListener
        shareClickListener = shareOnClickListener
    }

    companion object {
        private var viewHolderClickListener: View.OnClickListener? = null
        private var shareClickListener: View.OnClickListener? = null
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<Question> = object : DiffUtil.ItemCallback<Question>() {
            override fun areItemsTheSame(oldItem: Question, newItem: Question): Boolean = oldItem.questionId == newItem.questionId

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: Question, newItem: Question): Boolean = oldItem == newItem
        }
    }

    class QuestionViewHolder(private val binding: QuestionItemBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener(viewHolderClickListener)
            binding.ivShare.setOnClickListener(shareClickListener)
        }

        fun bind(question: Question) {
            binding.root.tag = question
            binding.ivShare.tag = question
            Glide.with(binding.root.context)
                .load(question.owner.profileImage)
                .placeholder(R.drawable.loading)
                .into(binding.ivAvatarItem)
            binding.tvQuestionItem.text = Jsoup.parse(question.title).text()
            binding.tvViewsCountItem.text = question.viewCount.toString()
            binding.tvDateItem.text = AppUtils.toNormalDate(question.creationDate.toLong())
            binding.answered.isVisible = question.isAnswered
            val answers = question.answerCount
            val answerCount = binding.root.context.resources.getQuantityString(
                R.plurals.answers,
                answers,
                answers
            )
            binding.tvAnswersCountItem.text = answerCount
            if (question.score <= 0) {
                binding.tvVotesCountItem.text = question.score.toString()
            } else {
                binding.tvVotesCountItem.text =
                    binding.root.context.getString(R.string.plus_score, question.score)
            }
            binding.tvTagsListItem.text = AppUtils.getFormattedTags(question.tags)
        }
    }
}