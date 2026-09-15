package com.openwps.app.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToFiles: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Home Screen")
        if (uiState.recentFiles.isEmpty()) {
            Text("No recent files.")
        }
    }
}
