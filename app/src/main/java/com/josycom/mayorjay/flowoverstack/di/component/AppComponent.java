package com.josycom.mayorjay.flowoverstack.di.component;

import android.app.Application;

import com.josycom.mayorjay.flowoverstack.AppController;
import com.josycom.mayorjay.flowoverstack.di.module.AnswerActivityModule;
import com.josycom.mayorjay.flowoverstack.di.module.MainActivityModule;
import com.josycom.mayorjay.flowoverstack.di.module.ApiModule;
import com.josycom.mayorjay.flowoverstack.di.module.SearchViewModelFactoryModule;
import com.josycom.mayorjay.flowoverstack.di.module.SearchViewModelModule;
import com.josycom.mayorjay.flowoverstack.di.module.TagsDialogFragmentModule;
import com.josycom.mayorjay.flowoverstack.di.module.TagsDialogViewModelModule;
import com.josycom.mayorjay.flowoverstack.di.module.TagsViewModelFactoryModule;
import com.josycom.mayorjay.flowoverstack.di.module.QuestionViewModelFactoryModule;
import com.josycom.mayorjay.flowoverstack.di.module.QuestionViewModelModule;
import com.josycom.mayorjay.flowoverstack.di.module.QuestionsFragmentModule;
import com.josycom.mayorjay.flowoverstack.di.module.SearchActivityModule;


import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;

@Component(modules = {
        AndroidInjectionModule.class,
        MainActivityModule.class,
        AnswerActivityModule.class,
        SearchActivityModule.class,
        SearchViewModelModule.class,
        SearchViewModelFactoryModule.class,
        QuestionsFragmentModule.class,
        QuestionViewModelModule.class,
        QuestionViewModelFactoryModule.class,
        ApiModule.class,
        TagsDialogFragmentModule.class,
        TagsDialogViewModelModule.class,
        TagsViewModelFactoryModule.class})
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