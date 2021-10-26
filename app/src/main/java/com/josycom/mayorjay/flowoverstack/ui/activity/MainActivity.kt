package com.josycom.mayorjay.flowoverstack.ui.activity

import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
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
import com.josycom.mayorjay.flowoverstack.databinding.ActivityMainBinding
import com.josycom.mayorjay.flowoverstack.ui.fragment.QuestionsByActivityFragment
import com.josycom.mayorjay.flowoverstack.ui.fragment.QuestionsByCreationFragment
import com.josycom.mayorjay.flowoverstack.ui.fragment.QuestionsByHotFragment
import com.josycom.mayorjay.flowoverstack.ui.fragment.QuestionsByVoteFragment
import com.josycom.mayorjay.flowoverstack.util.AppConstants
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>
    private var mFragmentTransaction: FragmentTransaction? = null
    private var isFragmentDisplayed = false
    private var isFabOpen = false
    private lateinit var mActivityMainBinding: ActivityMainBinding
    private var fabOpen: Animation? = null
    private var fabClose: Animation? = null
    private var mAppUpdateManager: AppUpdateManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        mActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mActivityMainBinding.root)
        setSupportActionBar(mActivityMainBinding.toolbar)
        fabOpen = AnimationUtils.loadAnimation(this, R.anim.fab_open)
        fabClose = AnimationUtils.loadAnimation(this, R.anim.fab_close)
        checkForTablet()
        mActivityMainBinding.searchFab.setOnClickListener { fabAction() }
        mActivityMainBinding.scanToSearch.setOnClickListener {
            startActivity(Intent(this, OcrActivity::class.java))
            hideFabActions()
        }
        mActivityMainBinding.typeToSearch.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
            hideFabActions()
        }
        if (savedInstanceState != null) {
            isFragmentDisplayed = savedInstanceState.getBoolean(AppConstants.FRAGMENT_STATE)
        }
        if (!isFragmentDisplayed) {
            if (findViewById<View?>(R.id.fragment_container) != null) {
                mFragmentTransaction = supportFragmentManager.beginTransaction()
                mFragmentTransaction!!.add(R.id.fragment_container, QuestionsByActivityFragment()).commit()
                isFragmentDisplayed = true
            }
        }
    }

    override fun onResume() {
        super.onResume()
        checkForUpdate()
    }

    private fun fabAction() {
        if (isFabOpen) {
            hideFabActions()
        } else {
            showFabActions()
        }
    }

    private fun hideFabActions() {
        mActivityMainBinding.apply {
            searchFab.setImageDrawable(ContextCompat.getDrawable(this@MainActivity, R.drawable.ic_search))
            scanToSearch.startAnimation(fabClose)
            scanToSearch.isClickable = false
            scanToSearch.visibility = View.INVISIBLE
            typeToSearch.startAnimation(fabClose)
            typeToSearch.isClickable = false
            typeToSearch.visibility = View.INVISIBLE
        }
        isFabOpen = false
    }

    private fun showFabActions() {
        mActivityMainBinding.apply {
            searchFab.setImageDrawable(ContextCompat.getDrawable(this@MainActivity, R.drawable.ic_close))
            scanToSearch.startAnimation(fabOpen)
            scanToSearch.isClickable = true
            scanToSearch.visibility = View.VISIBLE
            typeToSearch.startAnimation(fabOpen)
            typeToSearch.isClickable = true
            typeToSearch.visibility = View.VISIBLE
        }
        isFabOpen = true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_filter_by_recency) {
            if (findViewById<View?>(R.id.fragment_container) != null && item.title == getString(R.string.action_filter_by_recency)) {
                mFragmentTransaction = supportFragmentManager.beginTransaction()
                mFragmentTransaction!!.replace(R.id.fragment_container, QuestionsByCreationFragment()).commit()
                item.setTitle(R.string.action_filter_by_activity)
            } else if (findViewById<View?>(R.id.fragment_container) != null && item.title == getString(R.string.action_filter_by_activity)) {
                mFragmentTransaction = supportFragmentManager.beginTransaction()
                mFragmentTransaction!!.replace(R.id.fragment_container, QuestionsByActivityFragment()).commit()
                item.setTitle(R.string.action_filter_by_recency)
            }
            return true
        } else if (id == R.id.action_filter_by_hot) {
            if (findViewById<View?>(R.id.fragment_container) != null) {
                mFragmentTransaction = supportFragmentManager.beginTransaction()
                mFragmentTransaction!!.replace(R.id.fragment_container, QuestionsByHotFragment()).commit()
            }
        } else if (id == R.id.action_filter_by_vote) {
            if (findViewById<View?>(R.id.fragment_container) != null) {
                mFragmentTransaction = supportFragmentManager.beginTransaction()
                mFragmentTransaction!!.replace(R.id.fragment_container, QuestionsByVoteFragment()).commit()
            }
        }
        return super.onOptionsItemSelected(item)
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
            mActivityMainBinding.scanToSearch.textSize = 15f
            mActivityMainBinding.typeToSearch.textSize = 15f
        }
    }

    override fun onPause() {
        super.onPause()
        if (mAppUpdateManager != null) {
            mAppUpdateManager!!.unregisterListener(installStateUpdatedListener)
        }
    }

    private val installStateUpdatedListener: InstallStateUpdatedListener = object : InstallStateUpdatedListener {
        override fun onStateUpdate(state: InstallState) {
            if (state.installStatus() == InstallStatus.DOWNLOADED) {
                popupSnackBarForCompleteUpdate()
            } else if (state.installStatus() == InstallStatus.INSTALLED) {
                if (mAppUpdateManager != null) {
                    mAppUpdateManager!!.unregisterListener(this)
                }
            } else {
                Log.i("UpdateInstaller", "InstallStateUpdatedListener >>>>> " + state.installStatus())
            }
        }
    }

    private fun checkForUpdate() {
        mAppUpdateManager = AppUpdateManagerFactory.create(this)
        mAppUpdateManager!!.registerListener(installStateUpdatedListener)
        mAppUpdateManager!!.appUpdateInfo.addOnSuccessListener { appUpdateInfo: AppUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                try {
                    mAppUpdateManager!!.startUpdateFlowForResult(
                            appUpdateInfo, AppUpdateType.FLEXIBLE, this@MainActivity, APP_UPDATE)
                } catch (e: SendIntentException) {
                    e.printStackTrace()
                }
            } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                popupSnackBarForCompleteUpdate()
            } else {
                Log.e("UpdateInstaller", "checkForAppUpdateAvailability >>>>>" + appUpdateInfo.installStatus())
            }
        }
    }

    private fun popupSnackBarForCompleteUpdate() {
        Snackbar.make(findViewById(R.id.main_root_layout), "An update has just been downloaded", Snackbar.LENGTH_INDEFINITE).apply {
            setAction("Restart") {
                if (mAppUpdateManager != null) {
                    mAppUpdateManager!!.completeUpdate()
                }
            }
            setActionTextColor(resources.getColor(R.color.colorPrimaryText))
            show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == APP_UPDATE) {
            if (resultCode != RESULT_OK) {
                checkForUpdate()
            }
        }
    }

    companion object {
        private const val APP_UPDATE = 10
    }
}