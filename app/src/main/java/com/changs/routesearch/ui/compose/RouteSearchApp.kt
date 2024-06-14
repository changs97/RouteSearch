package com.changs.routesearch.ui.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.fragment.app.FragmentManager
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.changs.routesearch.ui.SearchViewModel
import com.changs.routesearch.ui.theme.RouteSearchTheme
import timber.log.Timber

@Composable
fun RouteSearchApp(fragmentManager: FragmentManager) {
    RouteSearchTheme {
        val navController = rememberNavController()

        NavHost(navController = navController, startDestination = "home") {
            composable("home") {
                HomeScreen(supportFragmentManager = fragmentManager) {
                    navController.navigate("search")
                }
            }

            composable("search") { backStackEntry ->
                val searchViewModel: SearchViewModel = hiltViewModel(backStackEntry)

                SearchScreen(searchViewModel, onBackClick = {
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
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("search")
                }
                val searchViewModel: SearchViewModel = hiltViewModel(parentEntry)
                val type = backStackEntry.arguments?.getString("type") ?: "departures"

                SearchDetailScreen(searchViewModel, type) {
                    navController.popBackStack()
                }
            }

            composable("route") { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("search")
                }
                val searchViewModel: SearchViewModel = hiltViewModel(parentEntry)

                RouteScreen(
                    supportFragmentManager = fragmentManager,
                    searchViewModel = searchViewModel
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