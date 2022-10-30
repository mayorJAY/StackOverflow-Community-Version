package com.josycom.mayorjay.flowoverstack.data.model

import java.io.Serializable

data class Question(
    val tags: List<String> = emptyList(),
    val body: String = "",
    val owner: Owner = Owner(),
    val isAnswered: Boolean = false,
    val viewCount: Int = 0,
    val acceptedAnswerId: Int = 0,
    val answerCount: Int = 0,
    val score: Int = 0,
    val lastActivityDate: Int = 0,
    val creationDate: Int = 0,
    val lastEditDate: Int = 0,
    val questionId: Int = 0,
    val link: String = "",
    val title: String = ""
) : Serializable