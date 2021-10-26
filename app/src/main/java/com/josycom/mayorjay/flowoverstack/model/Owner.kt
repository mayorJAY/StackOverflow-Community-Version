package com.josycom.mayorjay.flowoverstack.model

import com.google.gson.annotations.SerializedName

class Owner {
    @SerializedName("reputation")
    var reputation: Int? = null

    @SerializedName("user_id")
    var userId: Int? = null

    @SerializedName("user_type")
    var userType: String? = null

    @SerializedName("accept_rate")
    var acceptRate: Int? = null

    @SerializedName("profile_image")
    var profileImage: String? = null

    @SerializedName("display_name")
    var displayName: String? = null

    @SerializedName("link")
    var link: String? = null
}