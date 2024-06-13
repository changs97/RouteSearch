package com.changs.routesearch.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.changs.routesearch.data.model.PoiInfo
import com.changs.routesearch.data.repository.MapRepository
import com.changs.routesearch.util.ApiResult
import com.naver.maps.geometry.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RouteViewModel@Inject constructor(private val mapRepository: MapRepository): ViewModel() {
    var departureLocation: PoiInfo? = null
    var destinationLocation: PoiInfo? = null

    private val _pathPoints = MutableStateFlow<List<LatLng>>(emptyList())
    val pathPoints: StateFlow<List<LatLng>> = _pathPoints.asStateFlow()

    fun getRoutes(departureLocation: PoiInfo, destinationLocation: PoiInfo) = viewModelScope.launch {
        mapRepository.getRoutes(departureLocation, destinationLocation)
            .collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        val pathPoints = mutableListOf<LatLng>()

                        result.value.features.forEach { feature ->
                            when (feature.geometry.type) {
                                "Point" -> {
                                    val coordinates = feature.geometry.coordinates
                                    val latLng = LatLng(coordinates[1].toString().toDouble(), coordinates[0].toString().toDouble())
                                    pathPoints.add(latLng)
                                }

                                else -> {
                                    // 다른 Geometry 타입에 대한 처리
                                }
                            }
                        }
                        _pathPoints.value = pathPoints
                    }
                    is ApiResult.Empty -> {
                        _pathPoints.value = emptyList()
                        Timber.d("empty")
                    }

                    is ApiResult.Error -> {
                        // send error message
                        _pathPoints.value = emptyList()
                        Timber.d("error ${result.exception} ${result.code}")
                    }
                }
            }
    }

}