package com.openwps.app.ui.files

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.openwps.core.filesystem.domain.FileType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FileManagerScreen(
    viewModel: FileManagerViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    var showCreateDialog by remember { mutableStateOf(false) }
    var folderName by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.currentPath) },
                navigationIcon = {
                    if (uiState.currentPath != "/") {
                        IconButton(onClick = { viewModel.navigateUp() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    }
                },
                actions = {
                    TextButton(onClick = { 
                        viewModel.setViewMode(if (uiState.viewMode == "LIST") "GRID" else "LIST") 
                    }) {
                        Text(if (uiState.viewMode == "LIST") "GRID" else "LIST")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showCreateDialog = true }) {
                Icon(Icons.Default.Create, contentDescription = "New Folder")
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            } else if (uiState.error != null) {
                Text(text = "Error: ${uiState.error}", color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(16.dp))
            } else if (uiState.files.isEmpty()) {
                Text("Folder is empty", modifier = Modifier.padding(16.dp))
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(uiState.files) { file ->
                        FileItemRow(
                            file = file, 
                            onClick = { 
                                if (file.isDirectory) {
                                    viewModel.loadDirectory(file.uri)
                                }
                            },
                            onDelete = { viewModel.deleteFile(file.uri) }
                        )
                    }
                }
            }
        }

        if (showCreateDialog) {
            AlertDialog(
                onDismissRequest = { showCreateDialog = false },
                title = { Text("New Folder") },
                text = {
                    OutlinedTextField(
                        value = folderName,
                        onValueChange = { folderName = it },
                        label = { Text("Folder Name") }
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        if (folderName.isNotBlank()) {
                            viewModel.createFolder(folderName)
                        }
                        showCreateDialog = false
                        folderName = ""
                    }) {
                        Text("Create")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showCreateDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
fun FileItemRow(file: FileItemUiModel, onClick: () -> Unit, onDelete: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = file.name, style = MaterialTheme.typography.bodyLarge)
            Text(
                text = if (file.isDirectory) "Folder" else "${file.sizeBytes} bytes", 
                style = MaterialTheme.typography.bodyMedium
            )
        }
        IconButton(onClick = onDelete) {
            Icon(Icons.Default.Delete, contentDescription = "Delete")
        }
    }
}
