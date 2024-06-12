package com.changs.routesearch.ui.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.fragment.app.FragmentManager
import com.changs.routesearch.databinding.RouteContainerBinding

@Composable
fun RouteScreen(
    supportFragmentManager: FragmentManager
) {
    val lifecycle = LocalLifecycleOwner.current

    AndroidViewBinding(factory = { inflater, parent, attachToParent ->
        RouteContainerBinding.inflate(inflater, parent, attachToParent)
    })
}

