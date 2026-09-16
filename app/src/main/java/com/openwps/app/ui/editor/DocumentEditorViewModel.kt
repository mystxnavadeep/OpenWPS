package com.openwps.app.ui.editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openwps.engines.document.NativeCapabilityRegistry
import com.openwps.engines.document.NativeDocumentSession
import com.openwps.office.api.DocumentSession
import com.openwps.office.api.command.DocumentCommand
import com.openwps.office.model.DocumentModel
import com.openwps.office.model.DocumentObjectId
import com.openwps.office.model.DocumentRange
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

data class EditorUiState(
    val content: String = "",
    val documentModel: DocumentModel? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class DocumentEditorViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(EditorUiState())
    val uiState: StateFlow<EditorUiState> = _uiState.asStateFlow()

    private var session: DocumentSession? = null

    init {
        // Initialize an empty native session for demo
        val registry = NativeCapabilityRegistry()
        session = NativeDocumentSession(UUID.randomUUID().toString(), registry)
        refreshContent()
    }

    fun insertText(text: String) {
        viewModelScope.launch {
            val range = DocumentRange(DocumentObjectId("root"), 0, DocumentObjectId("root"), 0)
            val cmd = DocumentCommand.InsertText(range, text)
            val result = session?.applyCommand(cmd)
            if (result?.success == true) {
                refreshContent()
            } else {
                _uiState.update { it.copy(error = result?.errorMessage ?: "Unknown error") }
            }
        }
    }

    private fun refreshContent() {
        viewModelScope.launch {
            val range = DocumentRange(DocumentObjectId("root"), 0, DocumentObjectId("root"), 0)
            val text = session?.getText(range) ?: ""
            val model = session?.getDocumentStructure()
            _uiState.update { it.copy(content = text, documentModel = model) }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch {
            session?.close()
        }
    }
}
