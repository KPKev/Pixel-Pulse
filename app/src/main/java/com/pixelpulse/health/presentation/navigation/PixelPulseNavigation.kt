package com.pixelpulse.health.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.pixelpulse.health.presentation.screens.dashboard.DashboardScreen
import com.pixelpulse.health.presentation.screens.nutrition.NutritionScreen
import com.pixelpulse.health.presentation.screens.onboarding.OnboardingScreen
import com.pixelpulse.health.presentation.screens.profile.ProfileScreen
import com.pixelpulse.health.presentation.screens.workout.WorkoutScreen
import com.pixelpulse.health.presentation.ui.components.BottomNavigationBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PixelPulseNavigation(
    navController: NavHostController,
    isAuthenticated: Boolean,
    isOnboardingCompleted: Boolean
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val startDestination = when {
        !isAuthenticated -> Screen.Onboarding.route
        !isOnboardingCompleted -> Screen.Onboarding.route
        else -> Screen.Dashboard.route
    }

    val showBottomBar = currentDestination?.route in listOf(
        Screen.Dashboard.route,
        Screen.Workout.route,
        Screen.Nutrition.route,
        Screen.Profile.route
    )

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(
                    navController = navController,
                    currentDestination = currentDestination
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Onboarding.route) {
                OnboardingScreen(
                    onOnboardingComplete = {
                        // Navigate to dashboard and clear the back stack
                        navController.navigate(Screen.Dashboard.route) {
                            popUpTo(Screen.Onboarding.route) { 
                                inclusive = true 
                            }
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(Screen.Dashboard.route) {
                DashboardScreen(
                    onNavigateToWorkout = {
                        navController.navigate(Screen.Workout.route)
                    },
                    onNavigateToNutrition = {
                        navController.navigate(Screen.Nutrition.route)
                    },
                    onNavigateToProfile = {
                        navController.navigate(Screen.Profile.route)
                    }
                )
            }

            composable(Screen.Workout.route) {
                WorkoutScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }

            composable(Screen.Nutrition.route) {
                NutritionScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }

            composable(Screen.Profile.route) {
                ProfileScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }

            // Add more screens as needed
            composable("${Screen.WorkoutDetail.route}/{workoutId}") { backStackEntry ->
                val workoutId = backStackEntry.arguments?.getString("workoutId") ?: ""
                // WorkoutDetailScreen(workoutId = workoutId)
            }

            composable("${Screen.FoodScanner.route}") {
                // FoodScannerScreen()
            }

            composable("${Screen.HealthMetrics.route}") {
                // HealthMetricsScreen()
            }

            composable("${Screen.AIChat.route}") {
                // AIChatScreen()
            }
        }
    }
}

sealed class Screen(val route: String, val title: String) {
    object Onboarding : Screen("onboarding", "Welcome")
    object Dashboard : Screen("dashboard", "Dashboard")
    object Workout : Screen("workout", "Workouts")
    object Nutrition : Screen("nutrition", "Nutrition")
    object Profile : Screen("profile", "Profile")
    object WorkoutDetail : Screen("workout_detail", "Workout Details")
    object FoodScanner : Screen("food_scanner", "Food Scanner")
    object HealthMetrics : Screen("health_metrics", "Health Metrics")
    object AIChat : Screen("ai_chat", "AI Assistant")
} 