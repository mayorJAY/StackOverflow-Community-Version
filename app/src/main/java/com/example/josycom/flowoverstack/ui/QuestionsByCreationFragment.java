package com.example.josycom.flowoverstack.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.josycom.flowoverstack.R;
import com.example.josycom.flowoverstack.adapters.QuestionAdapter;
import com.example.josycom.flowoverstack.model.Question;
import com.example.josycom.flowoverstack.util.StringConstants;
import com.example.josycom.flowoverstack.viewmodel.CustomQuestionViewModelFactory;
import com.example.josycom.flowoverstack.viewmodel.QuestionViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuestionsByCreationFragment extends Fragment {

    private RecyclerView mRecyclerView;

    public QuestionsByCreationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_questions_by_creation, container, false);
        mRecyclerView = view.findViewById(R.id.creation_recycler_view);

        handleRecyclerView();
        return view;
    }

    private void handleRecyclerView(){
        final QuestionAdapter questionAdapter = new QuestionAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        QuestionViewModel questionViewModel = new ViewModelProvider(this, new CustomQuestionViewModelFactory(StringConstants.FIRST_PAGE,
                StringConstants.PAGE_SIZE,
                StringConstants.ORDER_DESCENDING,
                StringConstants.SORT_BY_CREATION,
                StringConstants.SITE,
                StringConstants.FILTER)).get(QuestionViewModel.class);
        questionViewModel.getQuestionPagedList().observe(getViewLifecycleOwner(), new Observer<PagedList<Question>>() {
            @Override
            public void onChanged(PagedList<Question> questions) {
                questionAdapter.submitList(questions);
            }
        });
        mRecyclerView.setAdapter(questionAdapter);
    }
}
