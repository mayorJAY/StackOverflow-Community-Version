package com.josycom.mayorjay.flowoverstack.data.remote.model

import com.google.gson.annotations.SerializedName

data class QuestionsResponse(
    @SerializedName("has_more")
    val hasMore: Boolean? = null,

    @SerializedName("items")
    val items: List<QuestionRemote> = emptyList(),

    @SerializedName("quota_max")
    val quotaMax: Int? = null,

    @SerializedName("quota_remaining")
    val quotaRemaining: Int? = null
)