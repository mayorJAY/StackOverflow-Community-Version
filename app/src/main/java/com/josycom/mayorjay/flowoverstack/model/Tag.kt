package com.josycom.mayorjay.flowoverstack.model

import com.google.gson.annotations.SerializedName

class Tag {
    @SerializedName("name")
    var name: String? = null

    @SerializedName("count")
    var count: Long? = null

    @SerializedName("has_synonyms")
    var hasSynonyms: Boolean? = null

    @SerializedName("is_moderator_only")
    var isModeratorOnly: Boolean? = null

    @SerializedName("is_required")
    var isRequired: Boolean? = null
}