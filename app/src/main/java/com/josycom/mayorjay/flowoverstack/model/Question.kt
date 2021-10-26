package com.josycom.mayorjay.flowoverstack.model

import com.google.gson.annotations.SerializedName

class Question {
    @SerializedName("tags")
    var tags: List<String>? = null

    @SerializedName("body")
    var body: String? = null

    @SerializedName("owner")
    var owner: Owner? = null

    @SerializedName("is_answered")
    var isAnswered: Boolean? = null

    @SerializedName("view_count")
    var viewCount: Int? = null

    @SerializedName("accepted_answer_id")
    var acceptedAnswerId: Int? = null

    @SerializedName("answer_count")
    var answerCount: Int? = null

    @SerializedName("score")
    var score: Int? = null

    @SerializedName("last_activity_date")
    var lastActivityDate: Int? = null

    @SerializedName("creation_date")
    var creationDate: Int? = null

    @SerializedName("last_edit_date")
    var lastEditDate: Int? = null

    @SerializedName("question_id")
    var questionId: Int? = null

    @SerializedName("link")
    var link: String? = null

    @SerializedName("title")
    var title: String? = null
}