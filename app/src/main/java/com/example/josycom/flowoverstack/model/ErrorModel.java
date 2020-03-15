package com.example.josycom.flowoverstack.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ErrorModel {

    @SerializedName("error_id")
    @Expose
    private Integer errorId;
    @SerializedName("error_message")
    @Expose
    private String errorMessage;
    @SerializedName("error_name")
    @Expose
    private String errorName;
    @SerializedName("api_name")
    @Expose
    private String apiName;

    public Integer getErrorId() {
        return errorId;
    }

    public void setErrorId(Integer errorId) {
        this.errorId = errorId;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorName() {
        return errorName;
    }

    public void setErrorName(String errorName) {
        this.errorName = errorName;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }
}
