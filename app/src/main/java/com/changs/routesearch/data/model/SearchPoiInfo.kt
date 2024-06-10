package com.changs.routesearch.data.model

data class SearchPoiInfo(
    val count: String,
    val page: String,
    val pois: Pois,
    val totalCount: String
)