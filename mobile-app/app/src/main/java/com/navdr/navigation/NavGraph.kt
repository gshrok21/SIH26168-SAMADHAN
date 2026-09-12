package com.navdr.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.navdr.ui.screens.demo.DemoModeScreen
import com.navdr.ui.screens.history.HistoryScreen
import com.navdr.ui.screens.home.HomeScreen
import com.navdr.ui.screens.navigation.NavigationScreen
import com.navdr.ui.screens.onboarding.OnboardingScreen
import com.navdr.ui.screens.permissions.PermissionScreen
import com.navdr.ui.screens.routepreview.RoutePreviewScreen
import com.navdr.ui.screens.search.SearchScreen
import com.navdr.ui.screens.settings.SettingsScreen
import com.navdr.ui.screens.splash.SplashScreen
import com.navdr.ui.screens.system.SystemScreen
import com.navdr.ui.screens.tripdetails.TripDetailsScreen
import com.navdr.viewmodel.HistoryViewModel
import com.navdr.viewmodel.HomeViewModel
import com.navdr.viewmodel.NavigationViewModel
import com.navdr.viewmodel.SystemViewModel

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Routes.SPLASH,
    navigationViewModel: NavigationViewModel = viewModel(),
    homeViewModel: HomeViewModel = viewModel(),
    historyViewModel: HistoryViewModel = viewModel(),
    systemViewModel: SystemViewModel = viewModel()
) {

    val navigationUiState =
        navigationViewModel.uiState.collectAsState().value

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        // 1. Splash Screen
        composable(Routes.SPLASH) {
            SplashScreen(
                onNavigateNext = {
                    navController.navigate(Routes.ONBOARDING) {
                        popUpTo(Routes.SPLASH) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        // 2. Onboarding Screen
        composable(Routes.ONBOARDING) {
            OnboardingScreen(
                onFinishOnboarding = {
                    navController.navigate(Routes.PERMISSIONS) {
                        popUpTo(Routes.ONBOARDING) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        // 3. Permissions Screen
        composable(Routes.PERMISSIONS) {
            PermissionScreen(
                onContinueClick = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.PERMISSIONS) {
                            inclusive = true
                        }
                    }
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        // 4. Home Screen
        composable(Routes.HOME) {
            HomeScreen(
                navigationViewModel = navigationViewModel,
                homeViewModel = homeViewModel,
                onStartNavigationClick = {
                    navController.navigate(Routes.SEARCH)
                },
                onRecentTripClick = { trip ->
                    navController.navigate(
                        Routes.tripDetails(trip.id)
                    )
                },
                onSettingsClick = {
                    navController.navigate(Routes.SETTINGS)
                },
                onBottomNavigate = { route ->
                    if (route != Routes.HOME) {
                        navController.navigate(route) {
                            popUpTo(Routes.HOME) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                currentRoute = Routes.HOME
            )
        }

        // 5. Destination Search Screen
        composable(Routes.SEARCH) {
            SearchScreen(
                onSelectLocation = { location ->
                    navController.navigate(
                        "${Routes.ROUTE_PREVIEW}?name=${location.title}&area=${location.area}"
                    )
                },
                onBackClick = {
                    navController.popBackStack()
                },
                onBottomNavigate = { route ->
                    navController.navigate(route) {
                        popUpTo(Routes.HOME) {
                            saveState = true
                        }
                        launchSingleTop = true
                    }
                },
                currentRoute = Routes.SEARCH
            )
        }

        // 6. Route Preview Screen
        composable(
            route = "${Routes.ROUTE_PREVIEW}?name={name}&area={area}",
            arguments = listOf(
                navArgument("name") {
                    type = NavType.StringType
                    defaultValue = "BHU Main Gate"
                },
                navArgument("area") {
                    type = NavType.StringType
                    defaultValue = "Varanasi"
                }
            )
        ) { backStackEntry ->

            val destName =
                backStackEntry.arguments?.getString("name")
                    ?: "BHU Main Gate"

            val destArea =
                backStackEntry.arguments?.getString("area")
                    ?: "Varanasi"

            RoutePreviewScreen(
                navigationViewModel = navigationViewModel,
                destinationName = destName,
                destinationArea = destArea,
                onStartNavigation = {
                    navController.navigate(Routes.NAVIGATION)
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        // 7. Navigation Screen
        composable(Routes.NAVIGATION) {
            NavigationScreen(
                navigationViewModel = navigationViewModel,
                onExitNavigation = {
                    navController.popBackStack(
                        Routes.HOME,
                        false
                    )
                },
                onOpenDemoPanel = {
                    navController.navigate(Routes.DEMO_MODE)
                }
            )
        }

        // 8. History Screen
        composable(Routes.HISTORY) {
            HistoryScreen(
                historyViewModel = historyViewModel,
                onTripClick = { trip ->
                    navController.navigate(
                        Routes.tripDetails(trip.id)
                    )
                },
                onBottomNavigate = { route ->
                    navController.navigate(route) {
                        popUpTo(Routes.HOME) {
                            saveState = true
                        }
                        launchSingleTop = true
                    }
                },
                currentRoute = Routes.HISTORY
            )
        }

        // 9. Trip Details Screen
        composable(
            route = Routes.TRIP_DETAILS,
            arguments = listOf(
                navArgument("tripId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            val tripId =
                backStackEntry.arguments?.getString("tripId")
                    ?: ""

            val trip =
                historyViewModel.getTripById(tripId)

            TripDetailsScreen(
                trip = trip,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        // 10. System Screen
        composable(Routes.SYSTEM) {
            SystemScreen(
                systemViewModel = systemViewModel,
                navigationViewModel = navigationViewModel,
                onBottomNavigate = { route ->
                    navController.navigate(route) {
                        popUpTo(Routes.HOME) {
                            saveState = true
                        }
                        launchSingleTop = true
                    }
                },
                currentRoute = Routes.SYSTEM
            )
        }

        // 11. Settings Screen
        composable(Routes.SETTINGS) {

            SettingsScreen(
                onBackClick = {
                    navController.popBackStack()
                },

                forceDeadReckoning =
                    navigationUiState.forceDeadReckoning,

                onForceDeadReckoningChange = { enabled ->
                    navigationViewModel.setForceDeadReckoning(
                        enabled
                    )
                }
            )
        }

        // 12. Demo Mode Screen
        composable(Routes.DEMO_MODE) {
            DemoModeScreen(
                navigationViewModel = navigationViewModel,
                onLaunchNavigationWithDemo = {
                    navController.navigate(
                        Routes.NAVIGATION
                    )
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}