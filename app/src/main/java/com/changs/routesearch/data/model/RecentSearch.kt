package com.changs.routesearch.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recent_search")
data class RecentSearch(
    @PrimaryKey
    val name: String,
    val timestamp: Long
)