package com.josycom.mayorjay.flowoverstack.util

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.josycom.mayorjay.flowoverstack.BuildConfig
import timber.log.Timber

object AppLogger {

    fun init() {
        Timber.plant(if (BuildConfig.DEBUG) Timber.DebugTree() else ReleaseTree())
    }

    class ReleaseTree : Timber.Tree() {

        companion object {
            const val PRIORITY = "Priority"
            const val TAG = "Tag"
        }

        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG || priority == Log.INFO) {
                return
            }

            FirebaseCrashlytics.getInstance().apply {
                this.setCustomKey(PRIORITY, priority)
                tag?.let { this.setCustomKey(TAG, it) }
                this.log(message)
                t?.let { this.recordException(it) }
            }.sendUnsentReports()
        }
    }
}