package com.example.josycom.flowoverstack.viewmodel;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.example.josycom.flowoverstack.model.Question;
import com.example.josycom.flowoverstack.model.QuestionDataSource;
import com.example.josycom.flowoverstack.model.QuestionDataSourceFactory;
import com.example.josycom.flowoverstack.network.ApiService;
import com.example.josycom.flowoverstack.network.NetworkState;
import com.example.josycom.flowoverstack.util.StringConstants;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class QuestionViewModel extends ViewModel {
    private LiveData<PagedList<Question>> mQuestionPagedList;
    private LiveData<NetworkState> mNetworkStateLiveData;
    private LiveData<QuestionDataSource> mDataSource;
    private Executor mExecutor;


    public QuestionViewModel(ApiService apiService, String accessToken, String serviceName, String sortType) {

        mExecutor = Executors.newFixedThreadPool(5);

        QuestionDataSourceFactory factory = new QuestionDataSourceFactory(apiService, accessToken, serviceName, sortType, mExecutor);
        mDataSource = factory.getMutableLiveData();

        mNetworkStateLiveData = Transformations.switchMap(mDataSource, new Function<QuestionDataSource, LiveData<NetworkState>>() {
            @Override
            public LiveData<NetworkState> apply(QuestionDataSource source) {
                return source.getNetworkState();
            }
        });

        PagedList.Config pageConfig = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(StringConstants.PAGE_SIZE)
                .build();

        mQuestionPagedList = (new LivePagedListBuilder<Integer, Question>(factory, pageConfig))
                .setFetchExecutor(mExecutor)
                .build();
    }

    public LiveData<PagedList<Question>> getQuestionPagedList(){
        return mQuestionPagedList;
    }

    public LiveData<NetworkState> getNetworkStateLiveData(){
        return mNetworkStateLiveData;
    }
}
