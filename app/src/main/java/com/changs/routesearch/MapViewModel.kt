package com.changs.routesearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.changs.routesearch.data.repository.MapRepository
import com.changs.routesearch.util.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(private val mapRepository: MapRepository): ViewModel() {

    fun searchPoisSafeFlow(searchKeyword: String) {
        viewModelScope.launch {
            mapRepository.searchPois(searchKeyword)
                .collectLatest { result ->
                when (result) {
                    is ApiResult.Success -> {
                        // send api result
                    }
                    is ApiResult.Empty -> {
                        // send empty message
                    }
                    is ApiResult.Error -> {
                        // send error message
                    }
                }
            }
        }
    }
}