package com.changs.routesearch.ui.compose

import androidx.compose.runtime.Composable
import androidx.fragment.app.FragmentManager
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.changs.routesearch.ui.theme.RouteSearchTheme
import timber.log.Timber

@Composable
fun RouteSearchApp(fragmentManager: FragmentManager) {
    RouteSearchTheme {
        val navController = rememberNavController()

        NavHost(navController = navController, startDestination = "home") {
            composable("home") {
                Home(fragmentManager) {
                    // click event 처리
                    Timber.d("Search bar was clicked")
                }
            }
        }
    }
}