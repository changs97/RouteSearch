package com.changs.routesearch.data.model

import com.google.gson.annotations.SerializedName

data class RoutesApiModel(
    @SerializedName("type") val type: String,
    @SerializedName("features") val features: List<Feature>
)