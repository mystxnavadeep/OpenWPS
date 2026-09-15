package com.openwps.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.openwps.app.ui.home.HomeScreen
import com.openwps.app.ui.files.FileManagerScreen
import com.openwps.app.ui.settings.SettingsScreen

object Destinations {
    const val HOME = "home"
    const val FILES = "files"
    const val SETTINGS = "settings"
}

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Destinations.HOME
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Destinations.HOME) {
            HomeScreen(onNavigateToFiles = {
                navController.navigate(Destinations.FILES)
            })
        }
        composable(Destinations.FILES) {
            FileManagerScreen()
        }
        composable(Destinations.SETTINGS) {
            SettingsScreen()
        }
    }
}
