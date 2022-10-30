package com.josycom.mayorjay.flowoverstack.data.model

import java.io.Serializable

class Tag(
    val name: String = "",
    val count: Long = 0L,
    val hasSynonyms: Boolean = false,
    val isModeratorOnly: Boolean = false,
    val isRequired: Boolean = false
) : Serializable