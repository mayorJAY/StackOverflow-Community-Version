package com.josycom.mayorjay.flowoverstack.model

import com.google.gson.annotations.SerializedName

class Answer {
    @SerializedName("owner")
    var owner: Owner? = null

    @SerializedName("is_accepted")
    var isAccepted: Boolean? = null

    @SerializedName("score")
    var score: Int? = null

    @SerializedName("last_activity_date")
    var lastActivityDate: Int? = null

    @SerializedName("last_edit_date")
    var lastEditDate: Int? = null

    @SerializedName("creation_date")
    var creationDate: Int? = null

    @SerializedName("answer_id")
    var answerId: Int? = null

    @SerializedName("question_id")
    var questionId: Int? = null

    @SerializedName("body")
    var body: String? = null
}