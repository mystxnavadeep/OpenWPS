package com.openwps.core.filesystem

import com.openwps.core.common.result.Result
import java.io.InputStream
import java.io.OutputStream

interface OpenWpsFileSystem {
    suspend fun listDirectory(uri: String): Result<List<FileMetadata>>
    suspend fun getMetadata(uri: String): Result<FileMetadata>
    suspend fun checkExistence(uri: String): Result<Boolean>
    suspend fun createDirectory(parentUri: String, name: String): Result<FileMetadata>
    suspend fun createFile(parentUri: String, name: String, mimeType: String): Result<FileMetadata>
    suspend fun delete(uri: String): Result<Unit>
    suspend fun rename(uri: String, newName: String): Result<FileMetadata>
    suspend fun move(sourceUri: String, destinationParentUri: String): Result<FileMetadata>
    
    suspend fun openInputStream(uri: String): Result<InputStream>
    suspend fun openOutputStream(uri: String): Result<OutputStream>
}
