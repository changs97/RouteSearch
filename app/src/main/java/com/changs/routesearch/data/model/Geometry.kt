package com.changs.routesearch.data.model

import com.google.gson.annotations.SerializedName

data class Geometry(
    @SerializedName("type") val type: String,
    @SerializedName("coordinates") val coordinates: List<Any>
)