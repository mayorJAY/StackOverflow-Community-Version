package com.example.josycom.flowoverstack.network;

import com.example.josycom.flowoverstack.model.ErrorModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiResponse {

    public static <T> void sendRequest(Call<T> call, final String strApiName, final ApiResponseListener apiResponseListener){
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(@NotNull Call<T> call, @NotNull Response<T> response) {
                if (response.isSuccessful() && response.code() == 200){
                    if (response.body() != null){
                        apiResponseListener.onSuccess(strApiName, response.body());
                    } else {
                            Gson gson = new GsonBuilder().create();
                            ErrorModel errorModel = new ErrorModel();
                            try{
                                if (response.errorBody() != null){
                                    errorModel = gson.fromJson(response.errorBody().string(), ErrorModel.class);
                                    apiResponseListener.onError(strApiName, errorModel);
                                }
                            } catch (IOException e){
                                errorModel.setErrorId(600);
                                errorModel.setErrorMessage("Internal Error. Please try again later");
                                errorModel.setApiName(strApiName);
                                apiResponseListener.onError(strApiName, errorModel);
                            }
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<T> call, @NotNull Throwable t) {
                String errorMessage = t.getMessage();
                apiResponseListener.onFailure(strApiName, errorMessage);
            }
        });
    }
}
