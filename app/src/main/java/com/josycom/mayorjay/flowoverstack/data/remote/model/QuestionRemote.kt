package com.josycom.mayorjay.flowoverstack.data.remote.model

import com.google.gson.annotations.SerializedName

data class QuestionRemote(
    @SerializedName("tags")
    val tags: List<String>? = null,

    @SerializedName("body")
    val body: String? = null,

    @SerializedName("owner")
    val owner: OwnerRemote? = null,

    @SerializedName("is_answered")
    val isAnswered: Boolean? = null,

    @SerializedName("view_count")
    val viewCount: Int? = null,

    @SerializedName("accepted_answer_id")
    val acceptedAnswerId: Int? = null,

    @SerializedName("answer_count")
    val answerCount: Int? = null,

    @SerializedName("score")
    val score: Int? = null,

    @SerializedName("last_activity_date")
    val lastActivityDate: Int? = null,

    @SerializedName("creation_date")
    val creationDate: Int? = null,

    @SerializedName("last_edit_date")
    val lastEditDate: Int? = null,

    @SerializedName("question_id")
    val questionId: Int? = null,

    @SerializedName("link")
    val link: String? = null,

    @SerializedName("title")
    val title: String? = null
)