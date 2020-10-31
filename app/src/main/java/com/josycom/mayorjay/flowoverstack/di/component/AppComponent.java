package com.josycom.mayorjay.flowoverstack.di.component;

import android.app.Application;

import com.josycom.mayorjay.flowoverstack.AppController;
import com.josycom.mayorjay.flowoverstack.di.module.AnswerActivityModule;
import com.josycom.mayorjay.flowoverstack.di.module.MainActivityModule;
import com.josycom.mayorjay.flowoverstack.di.module.ApiModule;
import com.josycom.mayorjay.flowoverstack.di.module.QuestionViewModelFactoryModule;
import com.josycom.mayorjay.flowoverstack.di.module.QuestionViewModelModule;
import com.josycom.mayorjay.flowoverstack.di.module.QuestionsFragmentModule;


import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;

@Component(modules = {
        AndroidSupportInjectionModule.class,
        MainActivityModule.class,
        AnswerActivityModule.class,
        QuestionsFragmentModule.class,
        ApiModule.class,
        QuestionViewModelFactoryModule.class,
        QuestionViewModelModule.class})
@Singleton
public interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }


    /*
     * This is our custom Application class
     * */
    void inject(AppController appController);

}