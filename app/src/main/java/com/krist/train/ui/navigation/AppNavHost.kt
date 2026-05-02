package com.krist.train.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.krist.train.AppContainer
import com.krist.train.ui.screen.activities.ActivitiesScreen
import com.krist.train.ui.screen.activities.ActivitiesViewModel
import com.krist.train.ui.screen.goal.CoachScreen
import com.krist.train.ui.screen.goal.GoalSetupViewModel
import com.krist.train.ui.screen.home.HomeScreen
import com.krist.train.ui.screen.home.HomeViewModel
import com.krist.train.ui.screen.insights.InsightsViewModel
import com.krist.train.ui.screen.plan.TrainingPlanScreen
import com.krist.train.ui.screen.plan.TrainingPlanViewModel
import com.krist.train.ui.screen.settings.SettingsScreen
import com.krist.train.ui.screen.settings.SettingsViewModel
import com.krist.train.ui.viewModelFactory

@Composable
fun AppNavHost(container: AppContainer) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route ?: Routes.Home.path

    Scaffold(
        bottomBar = {
            NavigationBar {
                BottomRoutes.forEach { route ->
                    NavigationBarItem(
                        selected = currentRoute == route.path,
                        onClick = {
                            if (route == Routes.Home) {
                                navController.popBackStack(Routes.Home.path, inclusive = false)
                            } else {
                                navController.navigate(route.path) {
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        icon = { Text(route.label.take(1)) },
                        label = { Text(route.label) },
                    )
                }
            }
        },
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Routes.Home.path,
            modifier = Modifier.padding(padding),
        ) {
            composable(Routes.Home.path) {
                HomeScreen(
                    viewModel = viewModel(factory = viewModelFactory { HomeViewModel(container) }),
                    onOpenCoach = { navController.navigate(Routes.Coach.path) },
                    onOpenPlan = { navController.navigate(Routes.Plan.path) },
                    onOpenSettings = { navController.navigate(Routes.Settings.path) },
                )
            }
            composable(Routes.Activities.path) {
                ActivitiesScreen(viewModel(factory = viewModelFactory { ActivitiesViewModel(container) }))
            }
            composable(Routes.Coach.path) {
                CoachScreen(
                    insightsViewModel = viewModel(factory = viewModelFactory { InsightsViewModel(container) }),
                    goalViewModel = viewModel(factory = viewModelFactory { GoalSetupViewModel(container) }),
                )
            }
            composable(Routes.Plan.path) {
                TrainingPlanScreen(viewModel(factory = viewModelFactory { TrainingPlanViewModel(container) }))
            }
            composable(Routes.Settings.path) {
                SettingsScreen(viewModel(factory = viewModelFactory { SettingsViewModel(container) }))
            }
        }
    }
}
