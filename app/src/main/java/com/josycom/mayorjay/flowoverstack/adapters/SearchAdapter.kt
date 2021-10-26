package com.josycom.mayorjay.flowoverstack.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.josycom.mayorjay.flowoverstack.R
import com.josycom.mayorjay.flowoverstack.adapters.SearchAdapter.SearchViewHolder
import com.josycom.mayorjay.flowoverstack.databinding.QuestionItemBinding
import com.josycom.mayorjay.flowoverstack.model.Question
import com.josycom.mayorjay.flowoverstack.util.AppUtils
import org.jsoup.Jsoup

class SearchAdapter : RecyclerView.Adapter<SearchViewHolder>() {

    private var mQuestions: List<Question>? = null

    companion object {
        private var mOnClickListener: View.OnClickListener? = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val questionItemBinding = QuestionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(questionItemBinding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        if (mQuestions != null) {
            val currentQuestion = mQuestions!![position]
            holder.bind(currentQuestion)
        }
    }

    fun setOnClickListener(onClickListener: View.OnClickListener?) {
        mOnClickListener = onClickListener
    }

    override fun getItemCount(): Int {
        return if (mQuestions != null) mQuestions!!.size else 0
    }

    fun setQuestions(questions: List<Question>?) {
        mQuestions = questions
        notifyDataSetChanged()
    }

    inner class SearchViewHolder(private val mQuestionItemBinding: QuestionItemBinding) : RecyclerView.ViewHolder(mQuestionItemBinding.root) {

        init {
            mQuestionItemBinding.root.tag = this
            mQuestionItemBinding.root.setOnClickListener(mOnClickListener)
        }

        fun bind(question: Question?) {
            if (question != null) {
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
                mQuestionItemBinding.tvTagsListItem.text = AppUtils.getFormattedTags(tagList!!)
            }
        }
    }
}