package com.josycom.mayorjay.flowoverstack.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.josycom.mayorjay.flowoverstack.R
import com.josycom.mayorjay.flowoverstack.ui.adapters.TagsAdapter
import com.josycom.mayorjay.flowoverstack.databinding.TagsDialogFragmentBinding
import com.josycom.mayorjay.flowoverstack.ui.adapters.PagingLoadStateAdapter
import com.josycom.mayorjay.flowoverstack.ui.viewmodel.CustomTagsViewModelFactory
import com.josycom.mayorjay.flowoverstack.ui.viewmodel.TagsDialogViewModel
import com.josycom.mayorjay.flowoverstack.util.AppConstants
import dagger.android.support.AndroidSupportInjection
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.jsoup.internal.StringUtil
import javax.inject.Inject

class TagsDialogFragment : DialogFragment() {

    private lateinit var activity: AppCompatActivity
    private lateinit var viewModel: TagsDialogViewModel
    private lateinit var binding: TagsDialogFragmentBinding
    @Inject
    lateinit var viewModelFactory: CustomTagsViewModelFactory
    private var tagSelectionCallback: TagSelectionCallback? = null
    private var title: String = ""
    private var isPopularTagOption: Boolean = false

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)

        viewModelFactory.setInputs(AppConstants.FIRST_PAGE, AppConstants.PAGE_SIZE, AppConstants.API_KEY)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        activity = getActivity() as AppCompatActivity
        binding = TagsDialogFragmentBinding.inflate(LayoutInflater.from(activity))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, viewModelFactory).get(TagsDialogViewModel::class.java)
        initViews()
        if (isPopularTagOption) {
            fetchAndDisplayTags("")
        }
        setupListeners()
    }

    private fun initViews() {
        binding.tvTitle.text = title
        binding.layoutSearch.visibility = if (isPopularTagOption) View.INVISIBLE else View.VISIBLE
        binding.tvInfo.visibility = if (isPopularTagOption) View.VISIBLE else View.INVISIBLE
    }

    private fun setupListeners() {
        binding.btSearch.setOnClickListener {
            val query = binding.searchTextInputEditText.text.toString().trim().toLowerCase()
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
        viewModel.fetchTags(inName)
        binding.ivLookup.visibility = View.INVISIBLE
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
                    binding.tvError.visibility = View.VISIBLE
                    binding.tvError.setText(R.string.no_matching_result_rephrase)
                } else {
                    binding.tvError.visibility = View.INVISIBLE
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