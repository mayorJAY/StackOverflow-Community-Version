package com.josycom.mayorjay.flowoverstack.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.josycom.mayorjay.flowoverstack.R
import com.josycom.mayorjay.flowoverstack.databinding.PopularTagItemBinding
import com.josycom.mayorjay.flowoverstack.model.Tag

class PopularTagsAdapter: PagingDataAdapter<Tag, PopularTagsAdapter.PopularTagViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularTagViewHolder {
        val view = PopularTagItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PopularTagViewHolder(view)
    }

    override fun onBindViewHolder(holder: PopularTagViewHolder, position: Int) {
        val tag = getItem(position)
        if (tag != null) {
            holder.bind(tag)
        }
    }

    fun setOnClickListener(listener: View.OnClickListener?) {
        onClickListener = listener
    }

    companion object {
        private var onClickListener: View.OnClickListener? = null
        private val DIFF_CALLBACK = object: DiffUtil.ItemCallback<Tag>() {
            override fun areItemsTheSame(oldItem: Tag, newItem: Tag): Boolean = oldItem.name == newItem.name

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: Tag, newItem: Tag): Boolean = oldItem == newItem
        }
    }


    class PopularTagViewHolder(private val binding: PopularTagItemBinding): RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.tag = this
            binding.root.setOnClickListener(onClickListener)
        }

        fun bind(tag: Tag) {
            with(binding) {
                tvTagName.text = tag.name
                tvTagCount.text = binding.root.context.resources.getQuantityString(R.plurals.questions, tag.count!!.toInt(), tag.count!!.toInt())
            }
        }
    }
}