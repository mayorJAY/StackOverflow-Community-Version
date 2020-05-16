package com.example.josycom.flowoverstack.ui;

import android.content.Intent;
import android.os.Bundle;

import com.example.josycom.flowoverstack.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    private final String STATE_FRAGMENT = "state_of_fragment";
    private FragmentTransaction mFragmentTransaction;
    private boolean isFragmentDisplayed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState != null) {
            isFragmentDisplayed = savedInstanceState.getBoolean(STATE_FRAGMENT);
        }

        if (!isFragmentDisplayed) {
            if (findViewById(R.id.fragment_container) != null) {
                mFragmentTransaction = getSupportFragmentManager().beginTransaction();
                mFragmentTransaction.add(R.id.fragment_container, new QuestionsByActivityFragment()).commit();
                isFragmentDisplayed = true;
            }
        }
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

        if (id == R.id.action_search) {
            startActivity(new Intent(this, SearchActivity.class));
            overridePendingTransition(R.anim.fade_in_anim, R.anim.fade_out_anim);
            return true;
        } else if (id == R.id.action_filter_by_recency) {
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
        outState.putBoolean(STATE_FRAGMENT, isFragmentDisplayed);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in_anim, R.anim.fade_out_anim);
    }
}
