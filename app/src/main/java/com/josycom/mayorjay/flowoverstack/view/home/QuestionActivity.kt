package com.josycom.mayorjay.flowoverstack.view.home

import android.content.DialogInterface
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.josycom.mayorjay.flowoverstack.R
import com.josycom.mayorjay.flowoverstack.data.repository.PreferenceRepositoryImpl
import com.josycom.mayorjay.flowoverstack.data.repository.dataStore
import com.josycom.mayorjay.flowoverstack.databinding.ActivityQuestionBinding
import com.josycom.mayorjay.flowoverstack.databinding.LayoutInfoDialogBinding
import com.josycom.mayorjay.flowoverstack.util.AppConstants
import com.josycom.mayorjay.flowoverstack.util.AppUtils
import com.josycom.mayorjay.flowoverstack.view.ocr.OcrActivity
import com.josycom.mayorjay.flowoverstack.view.search.SearchActivity
import com.josycom.mayorjay.flowoverstack.view.tag.TagsDialogFragment
import com.josycom.mayorjay.flowoverstack.viewmodel.QuestionActivityViewModel
import com.josycom.mayorjay.flowoverstack.viewmodel.ViewModelProviderFactory
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class QuestionActivity : AppCompatActivity(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>
    private var fragmentTransaction: FragmentTransaction? = null
    private var isFragmentDisplayed = false
    private var isFabOpen = false
    private lateinit var binding: ActivityQuestionBinding
    private var fabOpen: Animation? = null
    private var fabClose: Animation? = null
    private var appUpdateManager: AppUpdateManager? = null
    private var job: Job? = null
    private val viewModel: QuestionActivityViewModel by viewModels {
        ViewModelProviderFactory(PreferenceRepositoryImpl(applicationContext.dataStore))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        initViews(savedInstanceState)
        getAppOpenCounts()
        observeAppOpenCount()
        setupListeners()
        startJob()
    }

    private fun initViews(savedInstanceState: Bundle?) {
        checkForTablet()
        fabOpen = AnimationUtils.loadAnimation(this, R.anim.fab_open)
        fabClose = AnimationUtils.loadAnimation(this, R.anim.fab_close)
        savedInstanceState?.let { isFragmentDisplayed = it.getBoolean(AppConstants.FRAGMENT_STATE) }
        if (!isFragmentDisplayed) {
            fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction?.add(R.id.fragment_container, QuestionsFragment())?.commit()
            QuestionsFragment.sortCondition = AppConstants.SORT_BY_ACTIVITY
            QuestionsFragment.title = getString(R.string.active_questions)
            isFragmentDisplayed = true
        }
    }

    private fun setupListeners() {
        binding.searchFab.setOnClickListener { fabAction() }
        binding.scanToSearch.setOnClickListener {
            startActivity(Intent(this, OcrActivity::class.java))
            hideFabActions()
        }
        binding.typeToSearch.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
            hideFabActions()
        }
    }

    private fun startJob() {
        job = lifecycleScope.launch {
            delay(500)
            displayAppRatingPrompt()
        }
    }

    private fun getAppOpenCounts() {
        viewModel.getAppOpenCountPref(AppConstants.APP_OPEN_COUNT_PREF_KEY)
    }

    private fun observeAppOpenCount() {
        viewModel.appOpenCountLiveData?.observe(this) { value ->
            viewModel.appOpenCount = value ?: 0
        }
    }

    override fun onResume() {
        super.onResume()
        checkForUpdate()
    }

    private fun fabAction() {
        if (isFabOpen)
            hideFabActions()
        else
            showFabActions()
    }

    private fun hideFabActions() {
        binding.apply {
            searchFab.setImageDrawable(
                ContextCompat.getDrawable(
                    this@QuestionActivity,
                    R.drawable.ic_search
                )
            )
            scanToSearch.startAnimation(fabClose)
            scanToSearch.isClickable = false
            scanToSearch.isInvisible = true
            typeToSearch.startAnimation(fabClose)
            typeToSearch.isClickable = false
            typeToSearch.isInvisible = true
        }
        isFabOpen = false
    }

    private fun showFabActions() {
        binding.apply {
            searchFab.setImageDrawable(
                ContextCompat.getDrawable(
                    this@QuestionActivity,
                    R.drawable.ic_close
                )
            )
            scanToSearch.startAnimation(fabOpen)
            scanToSearch.isClickable = true
            scanToSearch.isVisible = true
            typeToSearch.startAnimation(fabOpen)
            typeToSearch.isClickable = true
            typeToSearch.isVisible = true
        }
        isFabOpen = true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_filter_by_recency -> {
                if (item.title == getString(R.string.action_filter_by_recency)) {
                    switchView(getString(R.string.recent_questions), AppConstants.SORT_BY_CREATION)
                    item.setTitle(R.string.action_filter_by_activity)
                } else if (item.title == getString(R.string.action_filter_by_activity)) {
                    switchView(getString(R.string.active_questions), AppConstants.SORT_BY_ACTIVITY)
                    item.setTitle(R.string.action_filter_by_recency)
                }
                return true
            }
            R.id.action_filter_by_hot -> {
                switchView(getString(R.string.hot_questions), AppConstants.SORT_BY_HOT)
                return true
            }
            R.id.action_filter_by_vote -> {
                switchView(getString(R.string.voted_questions), AppConstants.SORT_BY_VOTES)
                return true
            }
            R.id.action_filter_by_popular_tags -> {
                switchView(getString(R.string.popular_tags), true)
                return true
            }
            R.id.action_filter_by_tag_name -> {
                switchView(getString(R.string.search_tag_name), false)
                return true
            }
            R.id.action_info -> {
                displayInfoDialog()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun switchView(title: String, sortCondition: String, tagName: String = "") {
        fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction?.replace(R.id.fragment_container, QuestionsFragment())?.commit()
        QuestionsFragment.title = title
        QuestionsFragment.sortCondition = sortCondition
        QuestionsFragment.tagName = tagName
    }

    private fun switchView(title: String, isPopularTagOption: Boolean) {
        TagsDialogFragment().apply {
            setTagSelectionListener(tagSelectionListener)
            setInitItems(title, isPopularTagOption)
            show(supportFragmentManager, "")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(AppConstants.FRAGMENT_STATE, isFragmentDisplayed)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return dispatchingAndroidInjector
    }

    private fun checkForTablet() {
        val isTablet = resources.getBoolean(R.bool.isTablet)
        if (isTablet) {
            binding.scanToSearch.textSize = 14f
            binding.typeToSearch.textSize = 14f
        }
    }

    override fun onPause() {
        super.onPause()
        appUpdateManager?.unregisterListener(installStateUpdatedListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }

    private val installStateUpdatedListener: InstallStateUpdatedListener =
        object : InstallStateUpdatedListener {
            override fun onStateUpdate(state: InstallState) {
                when {
                    state.installStatus() == InstallStatus.DOWNLOADED -> {
                        popupSnackBarForCompleteUpdate()
                    }
                    state.installStatus() == InstallStatus.INSTALLED -> {
                        appUpdateManager?.unregisterListener(this)
                    }
                else -> {
                    Timber.i("UpdateInstaller >>> InstallStateUpdatedListener >>>>> " + state.installStatus())
                }
            }
        }
    }

    private fun checkForUpdate() {
        appUpdateManager = AppUpdateManagerFactory.create(this)
        appUpdateManager?.registerListener(installStateUpdatedListener)
        appUpdateManager?.appUpdateInfo?.addOnSuccessListener { appUpdateInfo: AppUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                try {
                    appUpdateManager?.startUpdateFlowForResult(
                        appUpdateInfo,
                        AppUpdateType.FLEXIBLE,
                        this@QuestionActivity,
                        APP_UPDATE
                    )
                } catch (e: SendIntentException) {
                    Timber.e(e)
                }
            } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                popupSnackBarForCompleteUpdate()
            } else {
                Timber.i("UpdateInstaller >>> checkForAppUpdateAvailability >>>>>" + appUpdateInfo.installStatus())
            }
        }
    }

    private fun popupSnackBarForCompleteUpdate() {
        Snackbar.make(
            findViewById(R.id.main_root_layout),
            getString(R.string.update_downloaded),
            Snackbar.LENGTH_INDEFINITE
        ).apply {
            setAction(getString(R.string.restart)) {
                appUpdateManager?.completeUpdate()
            }
            setActionTextColor(resources.getColor(R.color.colorPrimaryText))
            show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == APP_UPDATE && resultCode != RESULT_OK) {
            checkForUpdate()
        }
    }

    companion object {
        private const val APP_UPDATE = 10
    }

    private val tagSelectionListener = object : TagsDialogFragment.TagSelectionCallback {
        override fun onTagSelected(tagName: String) {
            switchView(
                getString(R.string.questions, tagName),
                AppConstants.SORT_BY_ACTIVITY,
                tagName
            )
        }
    }

    private fun displayInfoDialog() {
        val binding = LayoutInfoDialogBinding.inflate(layoutInflater)
        AlertDialog.Builder(this).apply {
            setView(binding.root)
            binding.tvAppVersion.text = getString(
                R.string.version,
                packageManager.getPackageInfo(packageName, 0).versionName
            )
            binding.ivShare.setOnClickListener {
                AppUtils.shareContent(
                    getString(
                        R.string.share_app,
                        AppConstants.PLAY_STORE_URL
                    ), this@QuestionActivity
                )
            }
            binding.tvShare.setOnClickListener {
                AppUtils.shareContent(
                    getString(
                        R.string.share_app,
                        AppConstants.PLAY_STORE_URL
                    ), this@QuestionActivity
                )
            }
            binding.ivConnect.setOnClickListener {
                AppUtils.launchViewIntent(
                    this@QuestionActivity,
                    AppConstants.TWITTER_URL
                )
            }
            binding.tvConnect.setOnClickListener {
                AppUtils.launchViewIntent(
                    this@QuestionActivity,
                    AppConstants.TWITTER_URL
                )
            }
            binding.ivContact.setOnClickListener {
                AppUtils.launchEmailIntent(
                    this@QuestionActivity,
                    AppConstants.EMAIL_ADDRESS
                )
            }
            binding.tvContact.setOnClickListener {
                AppUtils.launchEmailIntent(
                    this@QuestionActivity,
                    AppConstants.EMAIL_ADDRESS
                )
            }
            show()
        }
    }

    private fun displayAppRatingPrompt() {
        val currentAppOpenCount = viewModel.appOpenCount.plus(1)
        viewModel.saveAppOpenCounts(AppConstants.APP_OPEN_COUNT_PREF_KEY, currentAppOpenCount)
        if (currentAppOpenCount == 5 || currentAppOpenCount == 10) {
            AlertDialog.Builder(this).apply {
                setCancelable(false)
                setTitle(getString(R.string.app_rating))
                setMessage(getString(R.string.app_rating_msg))
                setNegativeButton(getString(R.string.i_am_good)) { _: DialogInterface, _: Int -> }
                setPositiveButton(getString(R.string.i_will_rate)) { _: DialogInterface, _: Int ->
                    AppUtils.launchViewIntent(
                        this@QuestionActivity,
                        AppConstants.PLAY_STORE_URL
                    )
                }
                show()
            }
        }
    }
}