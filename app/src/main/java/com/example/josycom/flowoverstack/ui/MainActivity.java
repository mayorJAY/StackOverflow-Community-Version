package com.example.josycom.flowoverstack.ui;

import android.content.Intent;
import android.os.Bundle;

import com.example.josycom.flowoverstack.R;
import com.example.josycom.flowoverstack.adapters.QuestionAdapter;
import com.example.josycom.flowoverstack.model.Question;
import com.example.josycom.flowoverstack.network.ApiService;
import com.example.josycom.flowoverstack.network.NetworkState;
import com.example.josycom.flowoverstack.network.RestApiClient;
import com.example.josycom.flowoverstack.util.PreferenceHelper;
import com.example.josycom.flowoverstack.util.StringConstants;
import com.example.josycom.flowoverstack.viewmodel.CustomViewModelFactory;
import com.example.josycom.flowoverstack.viewmodel.QuestionViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    private ApiService mApiService;
    private String mAccessToken;
    private PreferenceHelper mPreferenceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mApiService = RestApiClient.getClientInstance().getApiService();
        mPreferenceHelper = PreferenceHelper.getInstance(getApplicationContext());
        mAccessToken = mPreferenceHelper.getString(StringConstants.ACCESS_TOKEN);

        RecyclerView recyclerView = findViewById(R.id.rv_questions);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        QuestionViewModel questionViewModel = new ViewModelProvider(this,
                new CustomViewModelFactory(mApiService, mAccessToken, StringConstants.QUESTIONS_BY_ACTIVITY_API_SERVICE,
                        StringConstants.SORT_BY_ACTIVITY)).get(QuestionViewModel.class);
        final QuestionAdapter questionAdapter = new QuestionAdapter();

        questionViewModel.getQuestionPagedList().observe(this, new Observer<PagedList<Question>>() {
            @Override
            public void onChanged(PagedList<Question> questions) {
                questionAdapter.submitList(questions);
            }
        });

        questionViewModel.getNetworkStateLiveData().observe(this, new Observer<NetworkState>() {
            @Override
            public void onChanged(NetworkState networkState) {
                questionAdapter.setNetworkState(networkState);
            }
        });
        recyclerView.setAdapter(questionAdapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AnswerActivity.class));
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        } else if (id == R.id.action_filter){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
