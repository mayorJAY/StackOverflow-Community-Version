package com.josycom.mayorjay.flowoverstack.data.model

import java.io.Serializable

data class Owner(
    val reputation: Int = 0,
    val userId: Int = 0,
    val userType: String = "",
    val acceptRate: Int = 0,
    val profileImage: String = "",
    val displayName: String = "",
    val link: String = ""
) : Serializable