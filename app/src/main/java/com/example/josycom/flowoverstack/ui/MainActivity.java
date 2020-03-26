package com.example.josycom.flowoverstack.ui;

import android.os.Bundle;

import com.example.josycom.flowoverstack.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    FragmentTransaction mFragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (findViewById(R.id.fragment_container) != null){
            QuestionsByActivityFragment questionsByActivityFragment = new QuestionsByActivityFragment();
            mFragmentTransaction = getSupportFragmentManager().beginTransaction();
            mFragmentTransaction.add(R.id.fragment_container, questionsByActivityFragment).commit();
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
            return true;
        } else if (id == R.id.action_filter_by_creation){
            if (findViewById(R.id.fragment_container) != null && item.getTitle().equals("Filter by Creation")){
                QuestionsByCreationFragment questionsByCreationFragment = new QuestionsByCreationFragment();
                mFragmentTransaction = getSupportFragmentManager().beginTransaction();
                mFragmentTransaction.replace(R.id.fragment_container, questionsByCreationFragment).commit();
                item.setTitle(R.string.action_filter_by_activity);
            } else if (findViewById(R.id.fragment_container) != null && item.getTitle().equals("Filter by Activity")){
                QuestionsByActivityFragment questionsByActivityFragment = new QuestionsByActivityFragment();
                mFragmentTransaction = getSupportFragmentManager().beginTransaction();
                mFragmentTransaction.replace(R.id.fragment_container, questionsByActivityFragment).commit();
                item.setTitle(R.string.action_filter_by_creation);
            }
            return true;
        } else if (id == R.id.action_filter_by_hot){
            if (findViewById(R.id.fragment_container) != null){
                QuestionsByHotFragment questionsByHotFragment = new QuestionsByHotFragment();
                mFragmentTransaction = getSupportFragmentManager().beginTransaction();
                mFragmentTransaction.replace(R.id.fragment_container, questionsByHotFragment).commit();
            }
        } else if (id == R.id.action_filter_by_vote){
            if (findViewById(R.id.fragment_container) != null){
                QuestionsByVoteFragment questionsByVoteFragment = new QuestionsByVoteFragment();
                mFragmentTransaction = getSupportFragmentManager().beginTransaction();
                mFragmentTransaction.replace(R.id.fragment_container, questionsByVoteFragment).commit();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
