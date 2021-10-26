package com.josycom.mayorjay.flowoverstack.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QuestionsResponse {

    @SerializedName("has_more")
    private Boolean hasMore;
    @SerializedName("items")
    private List<Question> items;
    @SerializedName("quota_max")
    private Integer quotaMax;
    @SerializedName("quota_remaining")
    private Integer quotaRemaining;

    public Boolean getHasMore() {
        return hasMore;
    }

    public void setHasMore(Boolean hasMore) {
        this.hasMore = hasMore;
    }

    public List<Question> getItems() {
        return items;
    }

    public void setItems(List<Question> items) {
        this.items = items;
    }

    public Integer getQuotaMax() {
        return quotaMax;
    }

    public void setQuotaMax(Integer quotaMax) {
        this.quotaMax = quotaMax;
    }

    public Integer getQuotaRemaining() {
        return quotaRemaining;
    }

    public void setQuotaRemaining(Integer quotaRemaining) {
        this.quotaRemaining = quotaRemaining;
    }
}
