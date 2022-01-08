package com.josycom.mayorjay.flowoverstack.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.josycom.mayorjay.flowoverstack.R
import com.josycom.mayorjay.flowoverstack.ui.adapters.QuestionAdapter.QuestionViewHolder
import com.josycom.mayorjay.flowoverstack.databinding.QuestionItemBinding
import com.josycom.mayorjay.flowoverstack.model.Question
import com.josycom.mayorjay.flowoverstack.util.AppUtils
import org.jsoup.Jsoup

class QuestionAdapter : PagingDataAdapter<Question, QuestionViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val questionItemBinding = QuestionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuestionViewHolder(questionItemBinding)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun setOnClickListener(onClickListener: View.OnClickListener?) {
        mOnClickListener = onClickListener
    }

    companion object {
        private var mOnClickListener: View.OnClickListener? = null
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<Question> = object : DiffUtil.ItemCallback<Question>() {
            override fun areItemsTheSame(oldItem: Question, newItem: Question): Boolean = oldItem.questionId == newItem.questionId

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: Question, newItem: Question): Boolean = oldItem == newItem
        }
    }

    class QuestionViewHolder(private val mQuestionItemBinding: QuestionItemBinding) : RecyclerView.ViewHolder(mQuestionItemBinding.root) {

        init {
            mQuestionItemBinding.root.setOnClickListener(mOnClickListener)
        }

        fun bind(question: Question?) {
            if (question != null) {
                mQuestionItemBinding.root.tag = question
                val owner = question.owner
                val profileImage = owner?.profileImage
                val tagList = question.tags
                Glide.with(mQuestionItemBinding.root.context)
                        .load(profileImage)
                        .placeholder(R.drawable.loading)
                        .into(mQuestionItemBinding.ivAvatarItem)
                mQuestionItemBinding.tvQuestionItem.text = Jsoup.parse(question.title).text()
                mQuestionItemBinding.tvViewsCountItem.text = question.viewCount.toString()
                mQuestionItemBinding.tvDateItem.text = AppUtils.toNormalDate(question.creationDate!!.toLong())
                if (question.isAnswered == true) {
                    mQuestionItemBinding.answered.visibility = View.VISIBLE
                } else {
                    mQuestionItemBinding.answered.visibility = View.GONE
                }
                val answers = question.answerCount
                val resources = mQuestionItemBinding.root.context.resources
                val answerCount = resources.getQuantityString(R.plurals.answers, answers!!, answers)
                mQuestionItemBinding.tvAnswersCountItem.text = answerCount
                if (question.score!! <= 0) {
                    mQuestionItemBinding.tvVotesCountItem.text = question.score.toString()
                } else {
                    mQuestionItemBinding.tvVotesCountItem.text = mQuestionItemBinding.root.context.getString(R.string.plus_score, question.score)
                }
                val tags = tagList ?: listOf()
                mQuestionItemBinding.tvTagsListItem.text = AppUtils.getFormattedTags(tags)
            }
        }
    }
}