package com.josycom.mayorjay.flowoverstack.network

import com.google.gson.annotations.SerializedName
import com.josycom.mayorjay.flowoverstack.model.Tag

class TagsResponse {
    @SerializedName("items")
    var items: List<Tag> = emptyList()

    @SerializedName("has_more")
    var hasMore: Boolean? = null

    @SerializedName("quota_max")
    var quotaMax: Int? = null

    @SerializedName("quota_remaining")
    var quotaRemaining: Int? = null
}