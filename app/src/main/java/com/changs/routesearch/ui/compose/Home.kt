package com.changs.routesearch.ui.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.fragment.app.FragmentManager
import com.changs.routesearch.databinding.HomeContainerBinding

@Composable
fun Home(
    supportFragmentManager: FragmentManager, onSearchBarClicked: () -> Unit
) {
    val lifecycle = LocalLifecycleOwner.current
    val currentOnSearchBarClicked by rememberUpdatedState(newValue = onSearchBarClicked)

    AndroidViewBinding(factory = { inflater, parent, attachToParent ->
        supportFragmentManager.setFragmentResultListener(
            "homeSearchRequestKey", lifecycle
        ) { _, _ ->
            currentOnSearchBarClicked()
        }
        HomeContainerBinding.inflate(inflater, parent, attachToParent)

    })
}

