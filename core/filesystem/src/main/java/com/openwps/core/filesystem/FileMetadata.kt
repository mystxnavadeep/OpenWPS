package com.openwps.core.filesystem

data class FileMetadata(
    val uri: String,
    val name: String,
    val sizeBytes: Long,
    val lastModified: Long,
    val isDirectory: Boolean
)
