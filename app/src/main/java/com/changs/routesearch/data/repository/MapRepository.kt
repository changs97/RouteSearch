package com.changs.routesearch.data.repository

import com.changs.routesearch.data.model.PoiInfo
import com.changs.routesearch.data.model.toPoiInfo
import com.changs.routesearch.data.source.local.PoiInfoDao
import com.changs.routesearch.data.source.remote.MapApiService
import com.changs.routesearch.util.ApiResult
import com.changs.routesearch.util.safeFlow
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// TODO: 구글 권장 아키텍쳐 공부 후 개선
// TODO: Flow 사용 공부
// TODO: searchPois에 대한 Paging 사용 고려
// TODO: Repository Interface 생성
// TODO: DataSource 클래스 생성

class MapRepository @Inject constructor(
    private val mapApiService: MapApiService, private val poiInfoDao: PoiInfoDao
) {
    suspend fun searchPois(searchKeyword: String): Flow<ApiResult<List<PoiInfo>>> = safeFlow {
        val data = mapApiService.getPois(searchKeyword = searchKeyword)
        data.searchPoiInfo.pois.poi.map { it.toPoiInfo() }
    }

    val recentSearches: Flow<List<PoiInfo>> = poiInfoDao.getPoiInfos()

    suspend fun insertRecentPoi(poi: PoiInfo) {
        poiInfoDao.insertPoiInfo(poi)
        if (poiInfoDao.getItemCount() > 5) {
            poiInfoDao.deleteOldest()
        }
    }

}