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
    private static RestApiClient mInstance;

    private RestApiClient(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
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
        if (mInstance == null){
            mInstance = new RestApiClient();
        }
        return mInstance;
    }

    public ApiService getApiService(){
        return mRetrofit.create(ApiService.class);
    }
}
