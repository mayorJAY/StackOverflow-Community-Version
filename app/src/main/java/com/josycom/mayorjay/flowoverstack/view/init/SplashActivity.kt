package com.josycom.mayorjay.flowoverstack.view.init

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.josycom.mayorjay.flowoverstack.util.AppConstants
import com.josycom.mayorjay.flowoverstack.view.home.QuestionActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startJob()
    }

    private fun startJob() {
        job = lifecycleScope.launch {
            delay(AppConstants.SPLASH_TIME)
            goToMainActivity()
        }
    }

    private fun goToMainActivity() {
        startActivity(Intent(this, QuestionActivity::class.java))
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }
}