package com.josycom.mayorjay.flowoverstack.model

import com.google.gson.annotations.SerializedName

class QuestionsResponse {
    @SerializedName("has_more")
    var hasMore: Boolean? = null

    @SerializedName("items")
    var items: List<Question>? = null

    @SerializedName("quota_max")
    var quotaMax: Int? = null

    @SerializedName("quota_remaining")
    var quotaRemaining: Int? = null
}