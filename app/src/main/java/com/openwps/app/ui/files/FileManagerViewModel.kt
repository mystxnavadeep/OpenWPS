package com.openwps.app.ui.files

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openwps.core.filesystem.OpenWpsFileSystem
import com.openwps.core.filesystem.FileMetadata
import com.openwps.core.filesystem.domain.FileTypeDetector
import com.openwps.core.filesystem.domain.FileType
import com.openwps.core.filesystem.preferences.FileManagerPreferences
import com.openwps.core.database.dao.RecentFileDao
import com.openwps.core.database.dao.FavoriteFileDao
import com.openwps.core.common.result.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import java.io.File

data class FileManagerUiState(
    val currentPath: String = "/",
    val files: List<FileItemUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val viewMode: String = "LIST",
    val sortMode: String = "NAME_ASC",
    val searchQuery: String = ""
)

data class FileItemUiModel(
    val uri: String,
    val name: String,
    val sizeBytes: Long,
    val lastModified: Long,
    val isDirectory: Boolean,
    val type: FileType,
    val isFavorite: Boolean = false
)

@HiltViewModel
class FileManagerViewModel @Inject constructor(
    private val fileSystem: OpenWpsFileSystem,
    private val preferences: FileManagerPreferences,
    private val recentFileDao: RecentFileDao,
    private val favoriteFileDao: FavoriteFileDao
) : ViewModel() {

    private val _uiState = MutableStateFlow(FileManagerUiState())
    val uiState: StateFlow<FileManagerUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(preferences.viewMode, preferences.sortMode) { view, sort ->
                Pair(view, sort)
            }.collect { (view, sort) ->
                _uiState.update { it.copy(viewMode = view, sortMode = sort) }
                refresh()
            }
        }
        
        // Initial load for root if internal storage
        // Actually, we need to load a safe directory. 
        // For Phase 2, we just load "/" or a predefined path if it exists.
        loadDirectory("/")
    }

    fun loadDirectory(uri: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, currentPath = uri) }
            when (val result = fileSystem.listDirectory(uri)) {
                is Result.Success -> {
                    val fileModels = result.data.map { meta ->
                        FileItemUiModel(
                            uri = meta.uri,
                            name = meta.name,
                            sizeBytes = meta.sizeBytes,
                            lastModified = meta.lastModified,
                            isDirectory = meta.isDirectory,
                            type = if (meta.isDirectory) FileType.Folder else FileTypeDetector.detectFromExtension(meta.name)
                        )
                    }
                    _uiState.update { it.copy(files = applySortAndFilter(fileModels, it.sortMode, it.searchQuery), isLoading = false) }
                }
                is Result.Error -> {
                    _uiState.update { it.copy(isLoading = false, error = result.error.message) }
                }
            }
        }
    }

    fun refresh() {
        loadDirectory(_uiState.value.currentPath)
    }

    fun navigateUp() {
        val current = _uiState.value.currentPath
        if (current != "/") {
            val parent = File(current).parent ?: "/"
            loadDirectory(parent)
        }
    }

    fun setSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        refresh() // In real app, we'd cache files and filter in memory, but this works for Phase 2 demo.
    }

    fun setSortMode(mode: String) {
        viewModelScope.launch { preferences.setSortMode(mode) }
    }

    fun setViewMode(mode: String) {
        viewModelScope.launch { preferences.setViewMode(mode) }
    }
    
    fun createFolder(name: String) {
        viewModelScope.launch {
            fileSystem.createDirectory(_uiState.value.currentPath, name)
            refresh()
        }
    }

    fun deleteFile(uri: String) {
        viewModelScope.launch {
            fileSystem.delete(uri)
            favoriteFileDao.delete(uri)
            recentFileDao.delete(uri)
            refresh()
        }
    }
    
    fun renameFile(uri: String, newName: String) {
        viewModelScope.launch {
            fileSystem.rename(uri, newName)
            refresh()
        }
    }

    private fun applySortAndFilter(files: List<FileItemUiModel>, sortMode: String, query: String): List<FileItemUiModel> {
        val filtered = if (query.isBlank()) files else files.filter { it.name.contains(query, ignoreCase = true) }
        return when (sortMode) {
            "NAME_ASC" -> filtered.sortedWith(compareBy({ !it.isDirectory }, { it.name.lowercase() }))
            "NAME_DESC" -> filtered.sortedWith(compareBy<FileItemUiModel> { !it.isDirectory }.thenByDescending { it.name.lowercase() })
            "DATE_DESC" -> filtered.sortedByDescending { it.lastModified }
            "SIZE_DESC" -> filtered.sortedByDescending { it.sizeBytes }
            else -> filtered.sortedBy { it.name.lowercase() }
        }
    }
}
