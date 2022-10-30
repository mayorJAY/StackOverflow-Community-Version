package com.josycom.mayorjay.flowoverstack.data.model

import java.io.Serializable

data class Answer(
    val owner: Owner = Owner(),
    val isAccepted: Boolean = false,
    val score: Int = 0,
    val lastActivityDate: Int = 0,
    val lastEditDate: Int = 0,
    val creationDate: Int = 0,
    val answerId: Int = 0,
    val questionId: Int = 0,
    val body: String = ""
) : Serializable