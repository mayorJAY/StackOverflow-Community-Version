package com.josycom.mayorjay.flowoverstack.network

import com.google.gson.annotations.SerializedName
import com.josycom.mayorjay.flowoverstack.model.Answer

class AnswerResponse {
    @SerializedName("has_more")
    var hasMore: Boolean? = null

    @SerializedName("items")
    var items: List<Answer> = emptyList()

    @SerializedName("quota_max")
    var quotaMax: Int? = null

    @SerializedName("quota_remaining")
    var quotaRemaining: Int? = null
}