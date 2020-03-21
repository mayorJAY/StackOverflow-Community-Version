package com.example.josycom.flowoverstack.network;

import com.example.josycom.flowoverstack.util.StringConstants;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestApiClient {

    private static HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    private static OkHttpClient.Builder client = new OkHttpClient.Builder().addInterceptor(interceptor);
    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(StringConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client.build());
    private static Retrofit retrofit = builder.build();

    public static <T> T getApiService(Class<T> type){
        return retrofit.create(type);
    }
}
