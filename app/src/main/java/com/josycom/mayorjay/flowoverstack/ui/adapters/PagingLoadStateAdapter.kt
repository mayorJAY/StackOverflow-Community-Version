package com.josycom.mayorjay.flowoverstack.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.josycom.mayorjay.flowoverstack.databinding.PagingLoadStateFooterViewItemBinding

class PagingLoadStateAdapter(private val retry: () -> Unit) : LoadStateAdapter<PagingLoadStateAdapter.PagingLoadStateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): PagingLoadStateViewHolder {
        val view = PagingLoadStateFooterViewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PagingLoadStateViewHolder(view, retry)
    }

    override fun onBindViewHolder(holder: PagingLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    class PagingLoadStateViewHolder(private val binding: PagingLoadStateFooterViewItemBinding, private val retry: () -> Unit) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.btRetry.setOnClickListener { retry.invoke() }
        }

        fun bind(loadState: LoadState) {
            binding.pbFooter.isVisible = loadState is LoadState.Loading
            binding.btRetry.isVisible = loadState is LoadState.Error
            binding.tvErrorMsg.isVisible = loadState is LoadState.Error
        }
    }
}