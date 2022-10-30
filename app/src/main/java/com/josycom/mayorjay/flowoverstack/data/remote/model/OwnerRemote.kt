package com.josycom.mayorjay.flowoverstack.data.remote.model

import com.google.gson.annotations.SerializedName

data class OwnerRemote(
    @SerializedName("reputation")
    val reputation: Int? = null,

    @SerializedName("user_id")
    val userId: Int? = null,

    @SerializedName("user_type")
    val userType: String? = null,

    @SerializedName("accept_rate")
    val acceptRate: Int? = null,

    @SerializedName("profile_image")
    val profileImage: String? = null,

    @SerializedName("display_name")
    val displayName: String? = null,

    @SerializedName("link")
    val link: String? = null
)