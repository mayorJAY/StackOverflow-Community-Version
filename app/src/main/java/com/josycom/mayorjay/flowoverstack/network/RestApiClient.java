package com.josycom.mayorjay.flowoverstack.network;

import com.josycom.mayorjay.flowoverstack.util.AppConstants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestApiClient {

    private static HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    private static OkHttpClient.Builder client = new OkHttpClient.Builder().addInterceptor(interceptor);
    private static Gson gson = new GsonBuilder()
            .setLenient()
            .create();
    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(AppConstants.BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client.build());
    private static Retrofit retrofit = builder.build();

    public static <T> T getApiService(Class<T> type) {
        return retrofit.create(type);
    }
}