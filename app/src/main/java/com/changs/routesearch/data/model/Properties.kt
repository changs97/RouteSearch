package com.changs.routesearch.data.model

import com.google.gson.annotations.SerializedName

data class Properties(
    @SerializedName("totalDistance") val totalDistance: Int?,
    @SerializedName("totalTime") val totalTime: Int?,
    @SerializedName("index") val index: Int?,
    @SerializedName("pointIndex") val pointIndex: Int?,
    @SerializedName("name") val name: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("direction") val direction: String?,
    @SerializedName("nearPoiName") val nearPoiName: String?,
    @SerializedName("nearPoiX") val nearPoiX: String?,
    @SerializedName("nearPoiY") val nearPoiY: String?,
    @SerializedName("intersectionName") val intersectionName: String?,
    @SerializedName("facilityType") val facilityType: String?,
    @SerializedName("facilityName") val facilityName: String?,
    @SerializedName("turnType") val turnType: Int?,
    @SerializedName("pointType") val pointType: String?,
    @SerializedName("lineIndex") val lineIndex: Int?,
    @SerializedName("distance") val distance: Int?,
    @SerializedName("time") val time: Int?,
    @SerializedName("roadType") val roadType: Int?,
    @SerializedName("categoryRoadType") val categoryRoadType: Int?
)