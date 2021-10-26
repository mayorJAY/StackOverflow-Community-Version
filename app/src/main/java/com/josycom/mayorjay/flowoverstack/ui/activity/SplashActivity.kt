package com.josycom.mayorjay.flowoverstack.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.josycom.mayorjay.flowoverstack.util.AppConstants

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //SplashScreen handler
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({ goToMainActivity() }, AppConstants.SPLASH_TIME)
    }

    //Launch MainActivity
    private fun goToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}