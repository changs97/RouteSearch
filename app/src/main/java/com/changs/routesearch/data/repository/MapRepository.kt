package com.changs.routesearch.data.repository

import com.changs.routesearch.data.model.PoiInfo
import com.changs.routesearch.data.model.PoiInfoApiModel
import com.changs.routesearch.data.source.local.PoiInfoDao
import com.changs.routesearch.data.source.remote.MapApiService
import com.changs.routesearch.util.ApiResult
import com.changs.routesearch.util.safeFlow
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MapRepository @Inject constructor(
    private val mapApiService: MapApiService, private val poiInfoDao: PoiInfoDao
) {
    fun searchPois(searchKeyword: String): Flow<ApiResult<PoiInfoApiModel>> = safeFlow {
        mapApiService.getPois(searchKeyword = searchKeyword)
    }

    val recentSearches: Flow<List<PoiInfo>> = poiInfoDao.getPoiInfos()

    suspend fun insertRecentPoi(poi: PoiInfo) {
        poiInfoDao.insertPoiInfo(poi)
        if (poiInfoDao.getItemCount() > 5) {
            poiInfoDao.deleteOldest()
        }
    }

}