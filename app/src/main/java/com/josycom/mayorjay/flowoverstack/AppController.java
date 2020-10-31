package com.josycom.mayorjay.flowoverstack;

import android.app.Application;

import com.josycom.mayorjay.flowoverstack.di.component.DaggerAppComponent;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasAndroidInjector;

public class AppController extends Application implements HasAndroidInjector {

    @Inject DispatchingAndroidInjector<Object> dispatchingAndroidInjector;

    @Override
    public AndroidInjector<Object> androidInjector() {
        return dispatchingAndroidInjector;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        DaggerAppComponent.builder().application(this).build().inject(this);
    }
}
