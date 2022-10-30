package com.josycom.mayorjay.flowoverstack.data.remote.model

import com.josycom.mayorjay.flowoverstack.data.model.Question

data class SearchResponse(
    val networkState: String,
    val questions: List<Question>?
)