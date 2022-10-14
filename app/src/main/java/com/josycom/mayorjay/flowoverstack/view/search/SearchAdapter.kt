package com.josycom.mayorjay.flowoverstack.view.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.josycom.mayorjay.flowoverstack.R
import com.josycom.mayorjay.flowoverstack.databinding.QuestionItemBinding
import com.josycom.mayorjay.flowoverstack.data.model.Question
import com.josycom.mayorjay.flowoverstack.view.search.SearchAdapter.SearchViewHolder
import com.josycom.mayorjay.flowoverstack.util.AppUtils
import org.jsoup.Jsoup

class SearchAdapter : RecyclerView.Adapter<SearchViewHolder>() {

    private var questions: List<Question>? = null

    companion object {
        private var clickListener: View.OnClickListener? = null
        private var shareClickListener: View.OnClickListener? = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val questionItemBinding = QuestionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(questionItemBinding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        if (questions != null) {
            val currentQuestion = questions?.get(position)
            holder.bind(currentQuestion)
        }
    }

    fun setOnClickListener(onClickListener: View.OnClickListener?, shareOnClickListener: View.OnClickListener?) {
        clickListener = onClickListener
        shareClickListener = shareOnClickListener
    }

    override fun getItemCount(): Int {
        return if (questions != null) questions?.size ?: 0 else 0
    }

    fun setQuestions(questions: List<Question>?) {
        this.questions = questions
        notifyDataSetChanged()
    }

    inner class SearchViewHolder(private val binding: QuestionItemBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.tag = this
            binding.root.setOnClickListener(clickListener)
            binding.ivShare.setOnClickListener(shareClickListener)
        }

        fun bind(question: Question?) {
            if (question != null) {
                binding.ivShare.tag = question
                val owner = question.owner
                val profileImage = owner.profileImage
                val tagList = question.tags
                Glide.with(binding.root.context)
                    .load(profileImage)
                    .placeholder(R.drawable.loading)
                    .into(binding.ivAvatarItem)
                binding.tvQuestionItem.text = Jsoup.parse(question.title).text()
                binding.tvViewsCountItem.text = question.viewCount.toString()
                binding.tvDateItem.text = AppUtils.toNormalDate(question.creationDate.toLong())
                if (question.isAnswered) {
                    binding.answered.isVisible = true
                } else {
                    binding.answered.isInvisible = true
                }
                val answers = question.answerCount
                val resources = binding.root.context.resources
                val answerCount = resources.getQuantityString(R.plurals.answers, answers, answers)
                binding.tvAnswersCountItem.text = answerCount
                if (question.score <= 0) {
                    binding.tvVotesCountItem.text = question.score.toString()
                } else {
                    binding.tvVotesCountItem.text =
                        binding.root.context.getString(R.string.plus_score, question.score)
                }
                binding.tvTagsListItem.text = AppUtils.getFormattedTags(tagList)
            }
        }
    }
}