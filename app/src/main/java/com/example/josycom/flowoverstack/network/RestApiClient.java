package com.example.josycom.flowoverstack.network;

import com.example.josycom.flowoverstack.util.StringConstants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestApiClient {
    private Retrofit mRetrofit;
    private static RestApiClient sInstance;

    private RestApiClient(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(StringConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();
    }

    public static synchronized RestApiClient getClientInstance(){
        if (sInstance == null){
            sInstance = new RestApiClient();
        }
        return sInstance;
    }

    public ApiService getApiService(){
        return mRetrofit.create(ApiService.class);
    }
}
