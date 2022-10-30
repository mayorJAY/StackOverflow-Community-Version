package com.josycom.mayorjay.flowoverstack.data.remote.model

import com.google.gson.annotations.SerializedName

data class TagRemote(
    @SerializedName("name")
    val name: String? = null,

    @SerializedName("count")
    val count: Long? = null,

    @SerializedName("has_synonyms")
    val hasSynonyms: Boolean? = null,

    @SerializedName("is_moderator_only")
    val isModeratorOnly: Boolean? = null,

    @SerializedName("is_required")
    val isRequired: Boolean? = null
)