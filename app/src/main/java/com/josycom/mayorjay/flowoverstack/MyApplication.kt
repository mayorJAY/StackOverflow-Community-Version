package com.josycom.mayorjay.flowoverstack;

import android.app.Application;

import com.josycom.mayorjay.flowoverstack.di.component.DaggerAppComponent;
import com.josycom.mayorjay.flowoverstack.util.AppLogger;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasAndroidInjector;

public class MyApplication extends Application implements HasAndroidInjector {

    @Inject
    DispatchingAndroidInjector<Object> dispatchingAndroidInjector;

    @Override
    public AndroidInjector<Object> androidInjector() {
        return dispatchingAndroidInjector;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        AppLogger.INSTANCE.init();
        DaggerAppComponent.builder().application(this).build().inject(this);
    }
}
