package com.josycom.mayorjay.flowoverstack

import android.app.Application
import com.josycom.mayorjay.flowoverstack.util.AppLogger
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AppLogger.init()
    }
}
