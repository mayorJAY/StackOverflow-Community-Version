package com.example.josycom.flowoverstack.network;

public class NetworkState {

    private final Status mStatus;
    private final int mCode;
    private final String mMsg;
    private final String mApiName;

    public static final NetworkState LOADED;
    public static final NetworkState LOADING;

    public NetworkState(Status status, int code, String msg, String apiName) {
        this.mStatus = status;
        this.mCode = code;
        this.mMsg = msg;
        this.mApiName = apiName;
    }

    static {
        LOADED = new NetworkState(Status.SUCCESS, 200, "Success", "ApiService");
        LOADING = new NetworkState(Status.RUNNING, 100, "Running", "ApiService");
    }

    public Status getStatus() {
        return mStatus;
    }

    public String getMsg() {
        return mMsg;
    }

    public int getCode() {
        return mCode;
    }

    public String getApiName() {
        return mApiName;
    }

    public enum Status {
        RUNNING,
        SUCCESS,
        FAILED
    }
}
