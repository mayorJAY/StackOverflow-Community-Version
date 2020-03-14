package com.example.josycom.flowoverstack.network;

public interface ApiResponseListener {
    void onSuccess(String strApiName, Object response);
    void onError(String strApiName);
    void onFailure(String strApiName, String message);
}
