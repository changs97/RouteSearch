package com.changs.routesearch.data.repository

import com.changs.routesearch.data.model.PoiInfo
import com.changs.routesearch.data.model.RecentSearch
import com.changs.routesearch.data.model.RoutesApiModel
import com.changs.routesearch.data.model.RoutesRequestBody
import com.changs.routesearch.data.model.toPoiInfo
import com.changs.routesearch.data.source.local.MapSearchDao
import com.changs.routesearch.data.source.remote.MapApiService
import com.changs.routesearch.util.ApiResult
import com.changs.routesearch.util.safeFlow
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MapRepository @Inject constructor(
    private val mapApiService: MapApiService, private val mapSearchDao: MapSearchDao
) {
    suspend fun searchPois(searchKeyword: String): Flow<ApiResult<List<PoiInfo>>> = safeFlow {
        val data = mapApiService.getPois(searchKeyword = searchKeyword)
        data.searchPoiInfo.pois.poi.map { it.toPoiInfo() }
    }

    suspend fun getRoutes(
        departureLocation: PoiInfo, destinationLocation: PoiInfo
    ): Flow<ApiResult<RoutesApiModel>> = safeFlow {
        val request = RoutesRequestBody(
            startName = departureLocation.name,
            startX = departureLocation.lon.toDouble(),
            startY = departureLocation.lat.toDouble(),
            endName = destinationLocation.name,
            endX = destinationLocation.lon.toDouble(),
            endY = destinationLocation.lat.toDouble()
        )

        mapApiService.postRoutes(body = request)
    }

    val recentSearches: Flow<List<RecentSearch>> = mapSearchDao.getRecentSearchs()

    suspend fun insertRecentSearchKeyword(searchKeyword: String) {
        val recentSearch =
            RecentSearch(name = searchKeyword, timestamp = System.currentTimeMillis())

        mapSearchDao.insertRecentSearchKeyword(recentSearch)
        if (mapSearchDao.getItemCount() > 5) {
            mapSearchDao.deleteOldest()
        }
    }

    suspend fun deleteSearchByName(searchKeyword: String) {
        mapSearchDao.deleteSearchByName(searchKeyword)
    }
}