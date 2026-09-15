package com.openwps.core.filesystem.domain

sealed interface OpenTarget {
    data object DocumentEditor : OpenTarget
    data object SpreadsheetEditor : OpenTarget
    data object PresentationEditor : OpenTarget
    data object PdfViewer : OpenTarget
    data object ImageViewer : OpenTarget
    data object FolderViewer : OpenTarget
    data object Unsupported : OpenTarget
}
