package com.openwps.app.ui.files

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun FileManagerScreen(
    viewModel: FileManagerViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("File Manager")
        Text("Current Path: ${uiState.currentPath}")
    }
}
