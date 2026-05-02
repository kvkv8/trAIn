package com.krist.train.ui.navigation

sealed class Routes(val path: String, val label: String) {
    data object Home : Routes("home", "Home")
    data object Coach : Routes("coach", "Coach")
    data object Activities : Routes("activities", "Activities")
    data object Plan : Routes("plan", "Plan")
    data object Settings : Routes("settings", "Settings")
}

val BottomRoutes = listOf(
    Routes.Home,
    Routes.Coach,
    Routes.Activities,
    Routes.Plan,
    Routes.Settings,
)
