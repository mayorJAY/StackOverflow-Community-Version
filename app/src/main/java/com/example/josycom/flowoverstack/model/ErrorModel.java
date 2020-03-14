package com.example.josycom.flowoverstack.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ErrorModel {

    @SerializedName("error_id")
    @Expose
    private Integer error_id;
    @SerializedName("error_message")
    @Expose
    private String error_message;
    @SerializedName("error_name")
    @Expose
    private String error_name;
    @SerializedName("api_name")
    @Expose
    private String apiName;

    public Integer getError_id() {
        return error_id;
    }

    public void setError_id(Integer error_id) {
        this.error_id = error_id;
    }

    public String getError_message() {
        return error_message;
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }

    public String getError_name() {
        return error_name;
    }

    public void setError_name(String error_name) {
        this.error_name = error_name;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }
}
