package com.changs.routesearch.ui

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.changs.routesearch.data.model.PoiInfo
import com.changs.routesearch.data.model.RecentSearch
import com.changs.routesearch.data.repository.MapRepository
import com.changs.routesearch.util.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RouteUiState(
    val departureLocation: PoiInfo? = null,
    val destinationLocation: PoiInfo? = null
)

data class SearchUiState(
    val searchText: TextFieldValue = TextFieldValue(""),
    val regionSearchs: List<PoiInfo> = emptyList(),
    val recentSearchs: List<RecentSearch> = emptyList(),
)

@HiltViewModel
class SearchViewModel @Inject constructor(private val mapRepository: MapRepository) : ViewModel() {
    private val _departureLocation = MutableStateFlow<PoiInfo?>(null)
    private val _destinationLocation = MutableStateFlow<PoiInfo?>(null)

    val routeUiState = combine(_departureLocation, _destinationLocation) { flowArr ->
        RouteUiState(
            departureLocation = flowArr[0],
            destinationLocation = flowArr[1],
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = RouteUiState()
    )

    fun updateDepartureLocation(poiInfo: PoiInfo) {
        _departureLocation.value = poiInfo
    }

    fun updateDestinationLocation(poiInfo: PoiInfo) {
        _destinationLocation.value = poiInfo
    }

    private val _searchText = MutableStateFlow(TextFieldValue(""))
    private val _regionSearch = MutableStateFlow(listOf<PoiInfo>())
    private val _recentSearchs = MutableStateFlow(emptyList<RecentSearch>())

    @Suppress("UNCHECKED_CAST")
    val searchUiState = combine(
        _searchText, _regionSearch, _recentSearchs
    ) { flowArr ->
        SearchUiState(
            searchText = flowArr[0] as TextFieldValue,
            regionSearchs = flowArr[1] as List<PoiInfo>,
            recentSearchs = flowArr[2] as List<RecentSearch>
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SearchUiState()
    )

    init {
        viewModelScope.launch {
            mapRepository.recentSearches.collectLatest { recentList ->
                _recentSearchs.value = recentList
            }
        }
    }

    fun updateSearchText(searchText: TextFieldValue) {
        _searchText.value = searchText
    }

    private var searchJob: Job? = null


    fun searchRegions() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(200)
            if (_searchText.value.text.isNotBlank()) {
                mapRepository.searchPois(
                    _searchText.value.text,
                ).collect { result ->
                    when (result) {
                        is ApiResult.Success -> {
                            _regionSearch.value = result.value
                        }

                        is ApiResult.Empty -> {
                            _regionSearch.value = emptyList()
                        }

                        is ApiResult.Error -> {
                            // send error message
                            _regionSearch.value = emptyList()
                        }
                    }
                }
            }
        }
    }

    fun addRecentSearch() = viewModelScope.launch {
        mapRepository.insertRecentSearchKeyword(_searchText.value.text)
    }

    fun deleteRecentSearch(searchKeyword: String) = viewModelScope.launch {
        mapRepository.deleteSearchByName(searchKeyword)
    }

}