package com.changs.routesearch.ui.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.changs.routesearch.R
import com.changs.routesearch.databinding.RouteContainerBinding
import com.changs.routesearch.ui.RouteFragment
import com.changs.routesearch.ui.SearchViewModel

@Composable
fun RouteScreen(
    supportFragmentManager: FragmentManager,
    searchViewModel: SearchViewModel,
    onBackClick: () -> Unit,
) {
    val lifecycle = LocalLifecycleOwner.current
    val currentOnBackClicked by rememberUpdatedState(newValue = onBackClick)
    val routeUiState by searchViewModel.routeUiState.collectAsState()

    AndroidViewBinding(factory = { inflater, parent, attachToParent ->
        supportFragmentManager.setFragmentResultListener(
            "routeBackRequestKey",
            lifecycle
        ) { _, _ ->
            currentOnBackClicked()
        }
        val binding = RouteContainerBinding.inflate(inflater, parent, attachToParent)
        supportFragmentManager.commit {
            val bundle = bundleOf(
                "departureLocation" to routeUiState.departureLocation,
                "destinationLocation" to routeUiState.destinationLocation
            )
            replace<RouteFragment>(
                R.id.route_fragment_container,
                args = bundle
            )
        }
        binding
    })
}

