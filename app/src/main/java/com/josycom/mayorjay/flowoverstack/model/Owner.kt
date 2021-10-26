package com.josycom.mayorjay.flowoverstack.model;

import com.google.gson.annotations.SerializedName;

public class Owner {

    @SerializedName("reputation")
    private Integer reputation;
    @SerializedName("user_id")
    private Integer userId;
    @SerializedName("user_type")
    private String userType;
    @SerializedName("accept_rate")
    private Integer acceptRate;
    @SerializedName("profile_image")
    private String profileImage;
    @SerializedName("display_name")
    private String displayName;
    @SerializedName("link")
    private String link;

    public Integer getReputation() {
        return reputation;
    }

    public void setReputation(Integer reputation) {
        this.reputation = reputation;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Integer getAcceptRate() {
        return acceptRate;
    }

    public void setAcceptRate(Integer acceptRate) {
        this.acceptRate = acceptRate;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
