package com.example.josycom.flowoverstack.network;

public class NetworkState {

    private final Status status;
    private final int code;
    private final String msg;
    private final String apiName;

    public static final NetworkState LOADED;
    public static final NetworkState LOADING;

    public NetworkState(Status status, int code, String msg, String apiName) {
        this.status = status;
        this.code = code;
        this.msg = msg;
        this.apiName = apiName;
    }

    static {
        LOADED = new NetworkState(Status.SUCCESS, 200, "Success", "ApiService");
        LOADING = new NetworkState(Status.RUNNING, 100, "Running", "ApiService");
    }

    public Status getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public int getCode() {
        return code;
    }

    public String getApiName() {
        return apiName;
    }

    public enum Status {
        RUNNING,
        SUCCESS,
        FAILED
    }
}
