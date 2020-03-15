package com.example.josycom.flowoverstack.network;

import com.example.josycom.flowoverstack.model.ErrorModel;

public interface ApiResponseListener {
    void onSuccess(String strApiName, Object response);
    void onError(String strApiName, ErrorModel errorModel);
    void onFailure(String strApiName, String message);
}
