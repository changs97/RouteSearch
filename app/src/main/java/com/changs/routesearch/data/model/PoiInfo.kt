package com.changs.routesearch.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PoiInfo(
    val id: String,
    val lat: String,
    val lon: String,
    val name: String
) : Parcelable