package com.openwps.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.openwps.app.ui.home.HomeScreen
import com.openwps.app.ui.files.FileManagerScreen
import com.openwps.app.ui.settings.SettingsScreen
import com.openwps.app.ui.editor.DocumentEditorScreen

object Destinations {
    const val HOME = "home"
    const val FILES = "files"
    const val SETTINGS = "settings"
    const val DOCUMENT_EDITOR = "document_editor"
}

@Composable
fun AppNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Destinations.HOME
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Destinations.HOME) {
            HomeScreen(onNavigateToFiles = {
                navController.navigate(Destinations.FILES)
            })
        }
        composable(Destinations.FILES) {
            // Need a way to navigate to editor from files. For now, we'll just test navigation.
            FileManagerScreen(
                onNavigateToEditor = {
                    navController.navigate(Destinations.DOCUMENT_EDITOR)
                }
            )
        }
        composable(Destinations.SETTINGS) {
            SettingsScreen()
        }
        composable(Destinations.DOCUMENT_EDITOR) {
            DocumentEditorScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
