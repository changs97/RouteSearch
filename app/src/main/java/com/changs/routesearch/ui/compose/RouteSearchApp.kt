package com.changs.routesearch.ui.compose

import androidx.compose.runtime.Composable
import androidx.fragment.app.FragmentManager
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.changs.routesearch.MapViewModel
import com.changs.routesearch.ui.theme.RouteSearchTheme

@Composable
fun RouteSearchApp(fragmentManager: FragmentManager) {
    RouteSearchTheme {
        val navController = rememberNavController()
        val mapViewModel: MapViewModel = hiltViewModel()

        NavHost(navController = navController, startDestination = "home") {
            composable("home") {
                HomeScreen(supportFragmentManager = fragmentManager) {
                    navController.navigate("search")
                }
            }

            composable("search") {
                SearchScreen(mapViewModel, onBackClick = {
                    navController.popBackStack()
                }, onDeparturesClick = {
                    navController.navigate("detail/departures")
                }, onArrivalsClick = {
                    navController.navigate("detail/arrivals")
                }, onCompleteClick = {
                    navController.navigate("route")
                })
            }

            composable(
                "detail/{type}",
                arguments = listOf(navArgument("type") { type = NavType.StringType })
            ) { backStackEntry ->
                val type = backStackEntry.arguments?.getString("type") ?: "departures"
                SearchDetailScreen(mapViewModel, type) {
                    navController.popBackStack()
                }
            }

            composable("route") {
                RouteScreen(
                    supportFragmentManager = fragmentManager,
                    mapViewModel
                ) {
                    navController.navigate("home") {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                }
            }
        }
    }
}