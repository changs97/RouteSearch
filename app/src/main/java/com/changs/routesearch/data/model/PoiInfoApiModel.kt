package com.changs.routesearch.data.model

import com.google.gson.annotations.SerializedName

data class PoiInfoApiModel(
    @SerializedName("searchPoiInfo")
    val searchPoiInfo: SearchPoiInfo
)