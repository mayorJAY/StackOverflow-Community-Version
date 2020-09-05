package com.josycom.mayorjay.flowoverstack.ui;

import android.content.Intent;
import android.os.Bundle;

import com.josycom.mayorjay.flowoverstack.R;
import com.josycom.mayorjay.flowoverstack.databinding.ActivityMainBinding;
import com.josycom.mayorjay.flowoverstack.util.AppConstants;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class MainActivity extends AppCompatActivity {
    private FragmentTransaction mFragmentTransaction;
    private boolean isFragmentDisplayed = false;
    private boolean isFabOpen = false;
    private ActivityMainBinding mActivityMainBinding;
    private Animation fabOpen, fabClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mActivityMainBinding.getRoot());
        setSupportActionBar(mActivityMainBinding.toolbar);
        fabOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);

        mActivityMainBinding.searchFab.setOnClickListener(view -> fabAction());

        mActivityMainBinding.scanToSearch.setOnClickListener(view -> {
            startActivity(new Intent(this, OcrActivity.class));
            hideFabActions();
        });
        mActivityMainBinding.typeToSearch.setOnClickListener(view -> {
            startActivity(new Intent(this, SearchActivity.class));
            hideFabActions();
        });

        if (savedInstanceState != null) {
            isFragmentDisplayed = savedInstanceState.getBoolean(AppConstants.FRAGMENT_STATE);
        }

        if (!isFragmentDisplayed) {
            if (findViewById(R.id.fragment_container) != null) {
                mFragmentTransaction = getSupportFragmentManager().beginTransaction();
                mFragmentTransaction.add(R.id.fragment_container, new QuestionsByActivityFragment()).commit();
                isFragmentDisplayed = true;
            }
        }
    }

    private void fabAction() {
        if (isFabOpen) {
            hideFabActions();
        } else {
            showFabActions();
        }
    }

    private void hideFabActions() {
        mActivityMainBinding.searchFab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_search));
        mActivityMainBinding.scanToSearch.startAnimation(fabClose);
        mActivityMainBinding.scanToSearch.setClickable(false);
        mActivityMainBinding.scanToSearch.setVisibility(View.INVISIBLE);
        mActivityMainBinding.typeToSearch.startAnimation(fabClose);
        mActivityMainBinding.typeToSearch.setClickable(false);
        mActivityMainBinding.typeToSearch.setVisibility(View.INVISIBLE);
        isFabOpen = false;
    }

    private void showFabActions() {
        mActivityMainBinding.searchFab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_close));
        mActivityMainBinding.scanToSearch.startAnimation(fabOpen);
        mActivityMainBinding.scanToSearch.setClickable(true);
        mActivityMainBinding.scanToSearch.setVisibility(View.VISIBLE);
        mActivityMainBinding.typeToSearch.startAnimation(fabOpen);
        mActivityMainBinding.typeToSearch.setClickable(true);
        mActivityMainBinding.typeToSearch.setVisibility(View.VISIBLE);
        isFabOpen = true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_filter_by_recency) {
            if (findViewById(R.id.fragment_container) != null && item.getTitle().equals(getString(R.string.action_filter_by_recency))) {
                mFragmentTransaction = getSupportFragmentManager().beginTransaction();
                mFragmentTransaction.replace(R.id.fragment_container, new QuestionsByCreationFragment()).commit();
                item.setTitle(R.string.action_filter_by_activity);
            } else if (findViewById(R.id.fragment_container) != null && item.getTitle().equals(getString(R.string.action_filter_by_activity))) {
                mFragmentTransaction = getSupportFragmentManager().beginTransaction();
                mFragmentTransaction.replace(R.id.fragment_container, new QuestionsByActivityFragment()).commit();
                item.setTitle(R.string.action_filter_by_recency);
            }
            return true;
        } else if (id == R.id.action_filter_by_hot) {
            if (findViewById(R.id.fragment_container) != null) {
                mFragmentTransaction = getSupportFragmentManager().beginTransaction();
                mFragmentTransaction.replace(R.id.fragment_container, new QuestionsByHotFragment()).commit();
            }
        } else if (id == R.id.action_filter_by_vote) {
            if (findViewById(R.id.fragment_container) != null) {
                mFragmentTransaction = getSupportFragmentManager().beginTransaction();
                mFragmentTransaction.replace(R.id.fragment_container, new QuestionsByVoteFragment()).commit();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(AppConstants.FRAGMENT_STATE, isFragmentDisplayed);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
