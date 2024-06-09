package com.changs.routesearch.data.model

import com.google.gson.annotations.SerializedName

data class RoutesApiModel(
    @SerializedName("features")
    val features: List<Feature>,
    @SerializedName("type")
    val type: String
)