package com.example.josycom.flowoverstack.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QuestionsResponse {

    @SerializedName("has_more")
    @Expose
    private Boolean has_more;
    @SerializedName("items")
    @Expose
    private List<Question> items;
    @SerializedName("quota_max")
    @Expose
    private Integer quota_max;
    @SerializedName("quota_remaining")
    @Expose
    private Integer quota_remaining;

    public Boolean getHas_more() {
        return has_more;
    }

    public void setHas_more(Boolean has_more) {
        this.has_more = has_more;
    }

    public List<Question> getItems() {
        return items;
    }

    public void setItems(List<Question> items) {
        this.items = items;
    }

    public Integer getQuota_max() {
        return quota_max;
    }

    public void setQuota_max(Integer quota_max) {
        this.quota_max = quota_max;
    }

    public Integer getQuota_remaining() {
        return quota_remaining;
    }

    public void setQuota_remaining(Integer quota_remaining) {
        this.quota_remaining = quota_remaining;
    }
}
