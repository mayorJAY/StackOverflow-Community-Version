package com.josycom.mayorjay.flowoverstack.di.module;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.josycom.mayorjay.flowoverstack.BuildConfig;
import com.josycom.mayorjay.flowoverstack.network.ApiService;
import com.josycom.mayorjay.flowoverstack.util.AppConstants;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class ApiModule {

    @Provides
    @Singleton
    Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        return gsonBuilder.setLenient().create();
    }

    @Provides
    @Singleton
    HttpLoggingInterceptor provideInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        if (BuildConfig.DEBUG) {
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        }
        return interceptor;
    }

    @Provides
    @Singleton
    OkHttpClient.Builder getOkHttpClient(HttpLoggingInterceptor interceptor) {
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        okHttpClient.addInterceptor(interceptor);
        return okHttpClient;
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(Gson gson, OkHttpClient.Builder okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(AppConstants.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient.build())
                .build();
    }

    @Provides
    @Singleton
    ApiService getApiService(Retrofit retrofit) {
        return retrofit.create(ApiService.class);
    }
}
