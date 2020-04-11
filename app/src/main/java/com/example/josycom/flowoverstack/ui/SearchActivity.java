package com.example.josycom.flowoverstack.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.josycom.flowoverstack.R;
import com.example.josycom.flowoverstack.adapters.SearchAdapter;
import com.example.josycom.flowoverstack.model.Question;
import com.example.josycom.flowoverstack.viewmodel.CustomSearchViewModelFactory;
import com.example.josycom.flowoverstack.viewmodel.SearchViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;
import java.util.Objects;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private String mSearchInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        final TextInputEditText textInputEditText = findViewById(R.id.text_input_editText);
        final MaterialButton materialButton = findViewById(R.id.search_button);
        mRecyclerView = findViewById(R.id.rv_search_results);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Objects.requireNonNull(textInputEditText.getText()).toString().isEmpty()) {
                    textInputEditText.setError("Type a search query");
                } else {
                    mSearchInput = Objects.requireNonNull(textInputEditText.getText()).toString();
                    textInputEditText.setText("");
                    makeSearch();
                }
            }
        });
    }

    private void makeSearch() {
        final SearchAdapter searchAdapter = new SearchAdapter();
        SearchViewModel searchViewModel = new ViewModelProvider(this, new CustomSearchViewModelFactory(mSearchInput)).get(SearchViewModel.class);
        searchViewModel.getQuestionLiveData().observe(this, new Observer<List<Question>>() {
            @Override
            public void onChanged(List<Question> questions) {
                searchAdapter.setQuestions(questions);
            }
        });
        mRecyclerView.setAdapter(searchAdapter);
    }
}
