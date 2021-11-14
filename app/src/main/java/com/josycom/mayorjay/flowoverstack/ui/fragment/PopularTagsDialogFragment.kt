package com.josycom.mayorjay.flowoverstack.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.josycom.mayorjay.flowoverstack.R
import com.josycom.mayorjay.flowoverstack.ui.adapters.PopularTagsAdapter
import com.josycom.mayorjay.flowoverstack.databinding.PopularTagsDialogFragmentBinding
import com.josycom.mayorjay.flowoverstack.ui.adapters.PopularTagLoadStateAdapter
import com.josycom.mayorjay.flowoverstack.ui.viewmodel.CustomPopularTagsViewModelFactory
import com.josycom.mayorjay.flowoverstack.ui.viewmodel.PopularTagsDialogViewModel
import com.josycom.mayorjay.flowoverstack.util.AppConstants
import dagger.android.support.AndroidSupportInjection
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class PopularTagsDialogFragment : DialogFragment() {

    private lateinit var activity: AppCompatActivity
    private lateinit var viewModel: PopularTagsDialogViewModel
    private lateinit var binding: PopularTagsDialogFragmentBinding
    @Inject
    lateinit var viewModelFactory: CustomPopularTagsViewModelFactory
    private var tagSelectionCallback: TagSelectionCallback? = null

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)

        viewModelFactory.setInputs(AppConstants.FIRST_PAGE, AppConstants.PAGE_SIZE, "", AppConstants.API_KEY)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        activity = getActivity() as AppCompatActivity
        binding = PopularTagsDialogFragmentBinding.inflate(LayoutInflater.from(activity))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, viewModelFactory).get(PopularTagsDialogViewModel::class.java)
        displayPopularTags()
    }

    private fun displayPopularTags() {
        val popularTagAdapter = PopularTagsAdapter()
        binding.rvPopularTags.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = popularTagAdapter.withLoadStateFooter(PopularTagLoadStateAdapter { popularTagAdapter.retry() })
        }

        lifecycleScope.launch {
            popularTagAdapter.loadStateFlow.collect {
                binding.pbPopularTags.visibility = if (it.source.refresh is LoadState.Loading) View.VISIBLE else View.GONE
                binding.tvError.visibility = if (it.source.refresh is LoadState.Error) View.VISIBLE else View.GONE
                binding.btRetry.visibility = if (it.source.refresh is LoadState.Error) View.VISIBLE else View.GONE
                binding.tvInfo.visibility = if (it.source.refresh !is LoadState.Loading && it.source.refresh !is LoadState.Error) View.VISIBLE else View.GONE
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.pagingDataFlow?.collectLatest {
                    popularTagAdapter.submitData(it)
                }
            }
        }

        popularTagAdapter.setOnClickListener(onClickListener)
        binding.btRetry.setOnClickListener { popularTagAdapter.retry() }
    }

    private val onClickListener = View.OnClickListener { v ->
        val holder = v?.tag as RecyclerView.ViewHolder
        val tagName = holder.itemView.findViewById<TextView>(R.id.tv_tag_name).text
        tagSelectionCallback?.onTagSelected(tagName.toString())
        dismiss()
    }

    override fun onStart() {
        super.onStart()
        val window = dialog?.window
        if (window != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            window.setLayout(width, height)
        }
    }

    fun setTagSelectionListener(tagSelectionCallback: TagSelectionCallback) {
        this.tagSelectionCallback = tagSelectionCallback
    }

    interface TagSelectionCallback {
        fun onTagSelected(tagName: String)
    }

}