package com.changs.routesearch.data.model

import com.google.gson.annotations.SerializedName

data class RoutesRequestBody(
    @SerializedName("startName")
    val startName: String,
    @SerializedName("startX")
    val startX: Double,
    @SerializedName("startY")
    val startY: Double,
    @SerializedName("endName")
    val endName: String,
    @SerializedName("endX")
    val endX: Double,
    @SerializedName("endY")
    val endY: Double,
    @SerializedName("angle")
    val angle: Int? = null,
    @SerializedName("endPoiId")
    val endPoiId: String? = null,
    @SerializedName("passList")
    val passList: String? = null,
    @SerializedName("reqCoordType")
    val reqCoordType: String? = null,
    @SerializedName("resCoordType")
    val resCoordType: String? = null,
    @SerializedName("searchOption")
    val searchOption: String? = null,
    @SerializedName("sort")
    val sort: String? = null,
    @SerializedName("speed")
    val speed: Int? = null
)