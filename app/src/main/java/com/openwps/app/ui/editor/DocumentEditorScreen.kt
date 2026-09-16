package com.openwps.app.ui.editor

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.openwps.office.model.DocumentModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentEditorScreen(
    viewModel: DocumentEditorViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var inputText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Document Editor (Structured Native Engine)") },
                navigationIcon = {
                    TextButton(onClick = onNavigateBack) {
                        Text("Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp)) {
            if (uiState.error != null) {
                Text("Error: ${uiState.error}", color = MaterialTheme.colorScheme.error)
            }
            
            Text("Engine Document Content (Flat):", style = MaterialTheme.typography.titleMedium)
            Surface(
                modifier = Modifier.fillMaxWidth().weight(0.3f).padding(vertical = 8.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Text(
                    text = uiState.content.ifEmpty { "Document is empty." },
                    modifier = Modifier.padding(8.dp)
                )
            }
            
            Text("Structured Model:", style = MaterialTheme.typography.titleMedium)
            Surface(
                modifier = Modifier.fillMaxWidth().weight(0.7f).padding(vertical = 8.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                uiState.documentModel?.let { model ->
                    LazyColumn(modifier = Modifier.fillMaxSize().padding(8.dp)) {
                        item { Text("Document ID: ${model.id.id} (v${model.version})", style = MaterialTheme.typography.labelSmall) }
                        items(model.sections) { sec ->
                            Column {
                                Text(" Section: ${sec.id.id}", style = MaterialTheme.typography.labelSmall)
                                sec.blocks.forEach { blk ->
                                    Text("  Block: ${blk.id.id}", style = MaterialTheme.typography.labelSmall)
                                    blk.paragraph?.let { par ->
                                        Text("   Paragraph: ${par.id.id}", style = MaterialTheme.typography.labelSmall)
                                        par.runs.forEach { run ->
                                            Text("    Run: [${run.text}]", style = MaterialTheme.typography.bodyMedium)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Type text to insert...") }
                )
                Button(onClick = {
                    if (inputText.isNotBlank()) {
                        viewModel.insertText(inputText)
                        inputText = ""
                    }
                }) {
                    Text("Insert")
                }
            }
        }
    }
}
