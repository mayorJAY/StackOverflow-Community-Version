package com.josycom.mayorjay.flowoverstack.data.remote.model

import com.google.gson.annotations.SerializedName

data class AnswerRemote(
    @SerializedName("owner")
    val owner: OwnerRemote? = null,

    @SerializedName("is_accepted")
    val isAccepted: Boolean? = null,

    @SerializedName("score")
    val score: Int? = null,

    @SerializedName("last_activity_date")
    val lastActivityDate: Int? = null,

    @SerializedName("last_edit_date")
    val lastEditDate: Int? = null,

    @SerializedName("creation_date")
    val creationDate: Int? = null,

    @SerializedName("answer_id")
    val answerId: Int? = null,

    @SerializedName("question_id")
    val questionId: Int? = null,

    @SerializedName("body")
    val body: String? = null
)