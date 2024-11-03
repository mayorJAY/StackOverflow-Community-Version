package com.josycom.mayorjay.flowoverstack.view.tag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.josycom.mayorjay.flowoverstack.R
import com.josycom.mayorjay.flowoverstack.databinding.TagsDialogFragmentBinding
import com.josycom.mayorjay.flowoverstack.view.home.PagingLoadStateAdapter
import com.josycom.mayorjay.flowoverstack.viewmodel.TagsDialogViewModel
import com.josycom.mayorjay.flowoverstack.util.AppConstants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.jsoup.internal.StringUtil
import java.util.Locale

@AndroidEntryPoint
class TagsDialogFragment : DialogFragment() {

    private lateinit var activity: AppCompatActivity
    private val viewModel: TagsDialogViewModel by viewModels()
    private lateinit var binding: TagsDialogFragmentBinding
    private var tagSelectionCallback: TagSelectionCallback? = null
    private var title: String = ""
    private var isPopularTagOption: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        activity = getActivity() as AppCompatActivity
        binding = TagsDialogFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        if (isPopularTagOption) {
            fetchAndDisplayTags("")
        }
        setupListeners()
    }

    private fun initViews() {
        binding.tvTitle.text = title
        binding.layoutSearch.isInvisible = isPopularTagOption
        binding.tvInfo.isInvisible = !isPopularTagOption
    }

    private fun setupListeners() {
        binding.btSearch.setOnClickListener {
            val query = binding.searchTextInputEditText.text.toString().trim()
                .lowercase(Locale.getDefault())
            if (StringUtil.isBlank(query)) {
                binding.searchTextInputLayout.isErrorEnabled = true
                binding.searchTextInputLayout.error = getString(R.string.type_a_search_query)
                return@setOnClickListener
            }
            binding.searchTextInputLayout.isErrorEnabled = false
            fetchAndDisplayTags(query)
        }
    }

    private fun fetchAndDisplayTags(inName: String) {
        viewModel.fetchTags(inName, AppConstants.FIRST_PAGE, AppConstants.PAGE_SIZE, AppConstants.API_KEY)
        binding.ivLookup.isInvisible = true
        val popularTagAdapter = TagsAdapter()
        binding.rvPopularTags.apply {
            layoutManager = LinearLayoutManager(activity)
            itemAnimator = DefaultItemAnimator()
            adapter = popularTagAdapter.withLoadStateFooter(PagingLoadStateAdapter { popularTagAdapter.retry() })
        }

        viewLifecycleOwner.lifecycleScope.launch {
            popularTagAdapter.loadStateFlow.collect {
                binding.pbPopularTags.isVisible = it.source.refresh is LoadState.Loading
                binding.tvError.isVisible = it.source.refresh is LoadState.Error
                binding.btRetry.isVisible = it.source.refresh is LoadState.Error
                binding.tvInfo.isVisible = it.source.refresh !is LoadState.Loading && it.source.refresh !is LoadState.Error && isPopularTagOption
                if (it.source.refresh is LoadState.NotLoading && popularTagAdapter.itemCount <= 0) {
                    binding.tvError.isVisible = true
                    binding.tvError.setText(R.string.no_matching_result_rephrase)
                } else {
                    binding.tvError.isInvisible = true
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.tagDataFlow?.collectLatest {
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

    fun setInitItems(title: String, isPopularTagOption: Boolean) {
        this.title = title
        this.isPopularTagOption = isPopularTagOption
    }

    interface TagSelectionCallback {
        fun onTagSelected(tagName: String)
    }

}