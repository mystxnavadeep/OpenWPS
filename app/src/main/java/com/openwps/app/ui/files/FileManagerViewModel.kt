package com.openwps.app.ui.files

import androidx.lifecycle.ViewModel
import com.openwps.core.filesystem.OpenWpsFileSystem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class FileManagerUiState(
    val currentPath: String = "/",
    val files: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class FileManagerViewModel @Inject constructor(
    private val fileSystem: OpenWpsFileSystem
) : ViewModel() {
    private val _uiState = MutableStateFlow(FileManagerUiState())
    val uiState: StateFlow<FileManagerUiState> = _uiState.asStateFlow()
}
