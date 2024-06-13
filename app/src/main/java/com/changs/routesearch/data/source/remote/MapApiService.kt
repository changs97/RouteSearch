package com.changs.routesearch.data.source.remote

import com.changs.routesearch.data.model.PoiInfoApiModel
import com.changs.routesearch.data.model.RoutesApiModel
import com.changs.routesearch.data.model.RoutesRequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MapApiService {
    @GET("/tmap/pois")
    suspend fun getPois(
        @Query("version") version: Int = 1,
        @Query("searchType") searchType: String = "all",
        @Query("areaLLCode") areaLLCode: String? = null,
        @Query("areaLMCode") areaLMCode: String? = null,
        @Query("searchtypCd") searchtypCd: String = "A",
        @Query("centerLon") centerLon: Float? = null,
        @Query("centerLat") centerLat: Float? = null,
        @Query("reqCoordType") reqCoordType: String = "WGS84GEO",
        @Query("resCoordType") resCoordType: String = "WGS84GEO",
        @Query("radius") radius: Int? = null,
        @Query("page") page: Int = 1,
        @Query("count") count: Int = 10,
        @Query("multiPoint") multiPoint: String = "Y",
        @Query("poiGroupYn") poiGroupYn: String = "N",
        @Query("searchKeyword") searchKeyword: String,
    ): PoiInfoApiModel

    @POST("/tmap/routes/pedestrian")
    suspend fun postRoutes(
        @Query("version") version: Int = 1, @Body body: RoutesRequestBody
    ): RoutesApiModel
}