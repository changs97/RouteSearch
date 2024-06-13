package com.changs.routesearch.data.model

import com.google.gson.annotations.SerializedName

data class Pois(
    @SerializedName("poi")
    val poi: List<Poi>
)