package com.changs.routesearch.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "poi")
data class PoiInfo(
    @PrimaryKey
    val id: String,
    val lat: String,
    val lon: String,
    val name: String,
    val timestamp: Long
)