package com.josycom.mayorjay.flowoverstack.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.josycom.mayorjay.flowoverstack.model.Answer;
import com.josycom.mayorjay.flowoverstack.repository.AnswerRepository;

import java.util.List;

public class AnswerViewModel extends ViewModel {

    private LiveData<List<Answer>> mAnswersLiveData;

    AnswerViewModel(int questionId, String order, String sortCondition, String site, String filter) {
        AnswerRepository answerRepository = new AnswerRepository(questionId, order, sortCondition, site, filter);
        mAnswersLiveData = answerRepository.getAnswers();
    }

    public LiveData<List<Answer>> getAnswersLiveData() {
        return mAnswersLiveData;
    }
}
