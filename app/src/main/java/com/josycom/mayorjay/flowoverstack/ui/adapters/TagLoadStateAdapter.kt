package com.josycom.mayorjay.flowoverstack.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.josycom.mayorjay.flowoverstack.databinding.TagLoadStateFooterViewItemBinding

class TagLoadStateAdapter(private val retry: () -> Unit) : LoadStateAdapter<TagLoadStateAdapter.PopularTagLoadStateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): PopularTagLoadStateViewHolder {
        val view = TagLoadStateFooterViewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PopularTagLoadStateViewHolder(view, retry)
    }

    override fun onBindViewHolder(holder: PopularTagLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    class PopularTagLoadStateViewHolder(private val binding: TagLoadStateFooterViewItemBinding, private val retry: () -> Unit) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.btRetry.setOnClickListener { retry.invoke() }
        }

        fun bind(loadState: LoadState) {
            binding.pbFooter.visibility = if (loadState is LoadState.Loading) View.VISIBLE else View.GONE
            binding.btRetry.visibility = if (loadState is LoadState.Error) View.VISIBLE else View.GONE
            binding.tvErrorMsg.visibility = if (loadState is LoadState.Error) View.VISIBLE else View.GONE
        }
    }
}