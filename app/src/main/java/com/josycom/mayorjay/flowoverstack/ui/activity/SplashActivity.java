package com.josycom.mayorjay.flowoverstack.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.josycom.mayorjay.flowoverstack.util.AppConstants;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //SplashScreen handler
        Handler handler = new Handler();
        handler.postDelayed(this::goToMainActivity, AppConstants.SPLASH_TIME);
    }

    //Launch MainActivity
    private void goToMainActivity() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}
