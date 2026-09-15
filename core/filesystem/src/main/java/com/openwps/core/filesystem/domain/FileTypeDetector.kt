package com.openwps.core.filesystem.domain

object FileTypeDetector {
    fun detectFromExtension(fileName: String): FileType {
        val extension = fileName.substringAfterLast('.', "").lowercase()
        return when (extension) {
            "doc", "docx", "odt", "txt", "rtf" -> FileType.Document
            "xls", "xlsx", "csv", "ods" -> FileType.Spreadsheet
            "ppt", "pptx", "odp" -> FileType.Presentation
            "pdf" -> FileType.Pdf
            "png", "jpg", "jpeg", "webp", "gif", "svg" -> FileType.Image
            "zip", "rar", "tar", "gz", "7z" -> FileType.Archive
            else -> FileType.Unknown
        }
    }

    fun detectFromMimeType(mimeType: String): FileType {
        val lowerMimeType = mimeType.lowercase()
        return when {
            lowerMimeType.contains("word") || lowerMimeType.contains("text/plain") || lowerMimeType.contains("rtf") || lowerMimeType.contains("opendocument.text") -> FileType.Document
            lowerMimeType.contains("excel") || lowerMimeType.contains("spreadsheet") || lowerMimeType.contains("csv") -> FileType.Spreadsheet
            lowerMimeType.contains("powerpoint") || lowerMimeType.contains("presentation") -> FileType.Presentation
            lowerMimeType == "application/pdf" -> FileType.Pdf
            lowerMimeType.startsWith("image/") -> FileType.Image
            lowerMimeType.contains("zip") || lowerMimeType.contains("tar") || lowerMimeType.contains("rar") || lowerMimeType.contains("7z") -> FileType.Archive
            lowerMimeType == "vnd.android.document/directory" -> FileType.Folder
            else -> FileType.Unknown
        }
    }
}
