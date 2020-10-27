package com.josycom.mayorjay.flowoverstack.di.module;

import com.josycom.mayorjay.flowoverstack.ui.QuestionsByActivityFragment;
import com.josycom.mayorjay.flowoverstack.ui.QuestionsByCreationFragment;
import com.josycom.mayorjay.flowoverstack.ui.QuestionsByHotFragment;
import com.josycom.mayorjay.flowoverstack.ui.QuestionsByVoteFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class QuestionsFragmentModule {

    @ContributesAndroidInjector
    abstract QuestionsByActivityFragment contributeQuestionsByActivityFragment();

    @ContributesAndroidInjector
    abstract QuestionsByCreationFragment contributeQuestionsByCreationFragment();

    @ContributesAndroidInjector
    abstract QuestionsByHotFragment contributeQuestionsByHotFragment();

    @ContributesAndroidInjector
    abstract QuestionsByVoteFragment contributeQuestionsByVoteFragment();
}
