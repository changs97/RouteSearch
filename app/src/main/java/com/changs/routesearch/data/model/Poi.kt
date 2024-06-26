package com.changs.routesearch.data.model

import com.google.gson.annotations.SerializedName

data class Poi(
    @SerializedName("id")
    val id: String,
    @SerializedName("adminDongCode")
    val adminDongCode: String,
    @SerializedName("bizName")
    val bizName: String,
    @SerializedName("collectionType")
    val collectionType: String,
    @SerializedName("dataKind")
    val dataKind: String,
    @SerializedName("desc")
    val desc: String,
    @SerializedName("detailAddrName")
    val detailAddrName: String,
    @SerializedName("detailBizName")
    val detailBizName: String,
    @SerializedName("detailInfoFlag")
    val detailInfoFlag: String,
    @SerializedName("evChargers")
    val evChargers: EvChargers,
    @SerializedName("firstBuildNo")
    val firstBuildNo: String,
    @SerializedName("firstNo")
    val firstNo: String,
    @SerializedName("frontLat")
    val frontLat: String,
    @SerializedName("frontLon")
    val frontLon: String,
    @SerializedName("legalDongCode")
    val legalDongCode: String,
    @SerializedName("lowerAddrName")
    val lowerAddrName: String,
    @SerializedName("lowerBizName")
    val lowerBizName: String,
    @SerializedName("middleAddrName")
    val middleAddrName: String,
    @SerializedName("middleBizName")
    val middleBizName: String,
    @SerializedName("mlClass")
    val mlClass: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("navSeq")
    val navSeq: String,
    @SerializedName("newAddressList")
    val newAddressList: NewAddressList,
    @SerializedName("noorLat")
    val noorLat: String,
    @SerializedName("noorLon")
    val noorLon: String,
    @SerializedName("parkFlag")
    val parkFlag: String,
    @SerializedName("pkey")
    val pkey: String,
    @SerializedName("radius")
    val radius: String,
    @SerializedName("roadName")
    val roadName: String,
    @SerializedName("rpFlag")
    val rpFlag: String,
    @SerializedName("secondBuildNo")
    val secondBuildNo: String,
    @SerializedName("secondNo")
    val secondNo: String,
    @SerializedName("telNo")
    val telNo: String,
    @SerializedName("upperAddrName")
    val upperAddrName: String,
    @SerializedName("upperBizName")
    val upperBizName: String,
    @SerializedName("zipCode")
    val zipCode: String
)

fun Poi.toPoiInfo(): PoiInfo {
    return PoiInfo(
        id = this.id,
        lat = this.frontLat,
        lon = this.noorLon,
        name = this.name
    )
}
