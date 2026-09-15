package com.openwps.core.filesystem.domain

object FileRouter {
    fun getTargetFor(fileType: FileType): OpenTarget {
        return when (fileType) {
            FileType.Document -> OpenTarget.DocumentEditor
            FileType.Spreadsheet -> OpenTarget.SpreadsheetEditor
            FileType.Presentation -> OpenTarget.PresentationEditor
            FileType.Pdf -> OpenTarget.PdfViewer
            FileType.Image -> OpenTarget.ImageViewer
            FileType.Folder -> OpenTarget.FolderViewer
            FileType.Archive, FileType.Unknown -> OpenTarget.Unsupported
        }
    }
}
