package com.openwps.core.filesystem

import android.content.Context
import android.net.Uri
import android.provider.DocumentsContract
import androidx.documentfile.provider.DocumentFile
import com.openwps.core.common.error.AppError
import com.openwps.core.common.result.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.InputStream
import java.io.OutputStream

class AndroidFileSystem(private val context: Context) : OpenWpsFileSystem {

    override suspend fun listDirectory(uri: String): Result<List<FileMetadata>> = withContext(Dispatchers.IO) {
        try {
            if (uri.startsWith("content://")) {
                val parsedUri = Uri.parse(uri)
                val documentFile = DocumentFile.fromTreeUri(context, parsedUri) 
                    ?: DocumentFile.fromSingleUri(context, parsedUri)
                
                if (documentFile != null && documentFile.isDirectory) {
                    val files = documentFile.listFiles().map { doc ->
                        FileMetadata(
                            uri = doc.uri.toString(),
                            name = doc.name ?: "Unknown",
                            sizeBytes = doc.length(),
                            lastModified = doc.lastModified(),
                            isDirectory = doc.isDirectory,
                            mimeType = doc.type ?: ""
                        )
                    }
                    Result.Success(files)
                } else {
                    Result.Error(AppError.FileNotFound("Not a directory or not found: $uri"))
                }
            } else {
                val file = File(uri)
                if (file.isDirectory) {
                    val files = file.listFiles()?.map { f ->
                        FileMetadata(
                            uri = f.absolutePath,
                            name = f.name,
                            sizeBytes = f.length(),
                            lastModified = f.lastModified(),
                            isDirectory = f.isDirectory,
                            mimeType = ""
                        )
                    } ?: emptyList()
                    Result.Success(files)
                } else {
                    Result.Error(AppError.FileNotFound("Not a directory or not found: $uri"))
                }
            }
        } catch (e: Exception) {
            Result.Error(AppError.Unknown("Failed to list directory: ${e.message}", e))
        }
    }

    override suspend fun getMetadata(uri: String): Result<FileMetadata> = withContext(Dispatchers.IO) {
        try {
            if (uri.startsWith("content://")) {
                val parsedUri = Uri.parse(uri)
                val documentFile = DocumentFile.fromSingleUri(context, parsedUri)
                    ?: DocumentFile.fromTreeUri(context, parsedUri)
                    
                if (documentFile != null && documentFile.exists()) {
                    Result.Success(
                        FileMetadata(
                            uri = documentFile.uri.toString(),
                            name = documentFile.name ?: "Unknown",
                            sizeBytes = documentFile.length(),
                            lastModified = documentFile.lastModified(),
                            isDirectory = documentFile.isDirectory,
                            mimeType = documentFile.type ?: ""
                        )
                    )
                } else {
                    Result.Error(AppError.FileNotFound("File not found: $uri"))
                }
            } else {
                val file = File(uri)
                if (file.exists()) {
                    Result.Success(
                        FileMetadata(
                            uri = file.absolutePath,
                            name = file.name,
                            sizeBytes = file.length(),
                            lastModified = file.lastModified(),
                            isDirectory = file.isDirectory,
                            mimeType = ""
                        )
                    )
                } else {
                    Result.Error(AppError.FileNotFound("File not found: $uri"))
                }
            }
        } catch (e: Exception) {
            Result.Error(AppError.Unknown("Failed to get metadata: ${e.message}", e))
        }
    }

    override suspend fun checkExistence(uri: String): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            if (uri.startsWith("content://")) {
                val parsedUri = Uri.parse(uri)
                val documentFile = DocumentFile.fromSingleUri(context, parsedUri)
                    ?: DocumentFile.fromTreeUri(context, parsedUri)
                Result.Success(documentFile?.exists() ?: false)
            } else {
                Result.Success(File(uri).exists())
            }
        } catch (e: Exception) {
            Result.Error(AppError.Unknown("Failed to check existence: ${e.message}", e))
        }
    }

    override suspend fun createDirectory(parentUri: String, name: String): Result<FileMetadata> = withContext(Dispatchers.IO) {
        try {
            if (parentUri.startsWith("content://")) {
                val parsedUri = Uri.parse(parentUri)
                val parentDoc = DocumentFile.fromTreeUri(context, parsedUri) 
                    ?: return@withContext Result.Error(AppError.FileNotFound("Parent not a tree: $parentUri"))
                
                val newDir = parentDoc.createDirectory(name)
                if (newDir != null) {
                    Result.Success(
                        FileMetadata(
                            uri = newDir.uri.toString(),
                            name = newDir.name ?: name,
                            sizeBytes = newDir.length(),
                            lastModified = newDir.lastModified(),
                            isDirectory = true,
                            mimeType = newDir.type ?: ""
                        )
                    )
                } else {
                    Result.Error(AppError.FileNotFound("Failed to create directory via SAF"))
                }
            } else {
                val parent = File(parentUri)
                val newDir = File(parent, name)
                if (newDir.mkdir() || newDir.exists()) {
                    Result.Success(
                        FileMetadata(
                            uri = newDir.absolutePath,
                            name = newDir.name,
                            sizeBytes = newDir.length(),
                            lastModified = newDir.lastModified(),
                            isDirectory = true,
                            mimeType = ""
                        )
                    )
                } else {
                    Result.Error(AppError.FileNotFound("Failed to create directory via File API"))
                }
            }
        } catch (e: Exception) {
            Result.Error(AppError.Unknown("Exception creating directory: ${e.message}", e))
        }
    }

    override suspend fun createFile(parentUri: String, name: String, mimeType: String): Result<FileMetadata> = withContext(Dispatchers.IO) {
        try {
            if (parentUri.startsWith("content://")) {
                val parsedUri = Uri.parse(parentUri)
                val parentDoc = DocumentFile.fromTreeUri(context, parsedUri) 
                    ?: return@withContext Result.Error(AppError.FileNotFound("Parent not a tree: $parentUri"))
                
                val newFile = parentDoc.createFile(mimeType, name)
                if (newFile != null) {
                    Result.Success(
                        FileMetadata(
                            uri = newFile.uri.toString(),
                            name = newFile.name ?: name,
                            sizeBytes = newFile.length(),
                            lastModified = newFile.lastModified(),
                            isDirectory = false,
                            mimeType = newFile.type ?: mimeType
                        )
                    )
                } else {
                    Result.Error(AppError.FileNotFound("Failed to create file via SAF"))
                }
            } else {
                val parent = File(parentUri)
                val newFile = File(parent, name)
                if (newFile.createNewFile() || newFile.exists()) {
                    Result.Success(
                        FileMetadata(
                            uri = newFile.absolutePath,
                            name = newFile.name,
                            sizeBytes = newFile.length(),
                            lastModified = newFile.lastModified(),
                            isDirectory = false,
                            mimeType = mimeType
                        )
                    )
                } else {
                    Result.Error(AppError.FileNotFound("Failed to create file via File API"))
                }
            }
        } catch (e: Exception) {
            Result.Error(AppError.Unknown("Exception creating file: ${e.message}", e))
        }
    }

    override suspend fun delete(uri: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            if (uri.startsWith("content://")) {
                val parsedUri = Uri.parse(uri)
                val documentFile = DocumentFile.fromSingleUri(context, parsedUri)
                if (documentFile != null && documentFile.delete()) {
                    Result.Success(Unit)
                } else {
                    Result.Error(AppError.FileNotFound("Failed to delete via SAF"))
                }
            } else {
                if (File(uri).delete()) {
                    Result.Success(Unit)
                } else {
                    Result.Error(AppError.FileNotFound("Failed to delete via File API"))
                }
            }
        } catch (e: Exception) {
            Result.Error(AppError.Unknown("Exception deleting: ${e.message}", e))
        }
    }

    override suspend fun rename(uri: String, newName: String): Result<FileMetadata> = withContext(Dispatchers.IO) {
        try {
            if (uri.startsWith("content://")) {
                val parsedUri = Uri.parse(uri)
                val documentFile = DocumentFile.fromSingleUri(context, parsedUri)
                if (documentFile != null && documentFile.renameTo(newName)) {
                    Result.Success(
                        FileMetadata(
                            uri = documentFile.uri.toString(),
                            name = documentFile.name ?: newName,
                            sizeBytes = documentFile.length(),
                            lastModified = documentFile.lastModified(),
                            isDirectory = documentFile.isDirectory,
                            mimeType = documentFile.type ?: ""
                        )
                    )
                } else {
                    Result.Error(AppError.FileNotFound("Failed to rename via SAF"))
                }
            } else {
                val file = File(uri)
                val newFile = File(file.parent, newName)
                if (file.renameTo(newFile)) {
                    Result.Success(
                        FileMetadata(
                            uri = newFile.absolutePath,
                            name = newFile.name,
                            sizeBytes = newFile.length(),
                            lastModified = newFile.lastModified(),
                            isDirectory = newFile.isDirectory,
                            mimeType = ""
                        )
                    )
                } else {
                    Result.Error(AppError.FileNotFound("Failed to rename via File API"))
                }
            }
        } catch (e: Exception) {
            Result.Error(AppError.Unknown("Exception renaming: ${e.message}", e))
        }
    }

    override suspend fun move(sourceUri: String, destinationParentUri: String): Result<FileMetadata> = withContext(Dispatchers.IO) {
        try {
            // Android SAF DocumentFile doesn't have a direct "move" API,
            // typically done via DocumentsContract.moveDocument
            // For File API, it's just renameTo
            if (sourceUri.startsWith("content://") && destinationParentUri.startsWith("content://")) {
                val source = Uri.parse(sourceUri)
                val destParent = Uri.parse(destinationParentUri)
                val sourceDoc = DocumentFile.fromSingleUri(context, source)
                // Needs Android 24+ for DocumentsContract.moveDocument, we are minSdk 26
                val parentDoc = DocumentFile.fromTreeUri(context, destParent)
                // A full move requires sourceParentUri, which we don't necessarily have cleanly here.
                // We'll leave it as unsupported in Phase 2 or do a copy+delete
                Result.Error(AppError.FileNotFound("Move via SAF not fully supported in this layer yet"))
            } else if (!sourceUri.startsWith("content://") && !destinationParentUri.startsWith("content://")) {
                val file = File(sourceUri)
                val destDir = File(destinationParentUri)
                val newFile = File(destDir, file.name)
                if (file.renameTo(newFile)) {
                    Result.Success(
                        FileMetadata(
                            uri = newFile.absolutePath,
                            name = newFile.name,
                            sizeBytes = newFile.length(),
                            lastModified = newFile.lastModified(),
                            isDirectory = newFile.isDirectory,
                            mimeType = ""
                        )
                    )
                } else {
                    Result.Error(AppError.FileNotFound("Failed to move via File API"))
                }
            } else {
                Result.Error(AppError.FileNotFound("Cross-filesystem move not supported"))
            }
        } catch (e: Exception) {
            Result.Error(AppError.Unknown("Exception moving: ${e.message}", e))
        }
    }

    override suspend fun openInputStream(uri: String): Result<InputStream> = withContext(Dispatchers.IO) {
        try {
            if (uri.startsWith("content://")) {
                val stream = context.contentResolver.openInputStream(Uri.parse(uri))
                if (stream != null) Result.Success(stream) else Result.Error(AppError.FileNotFound("Cannot open SAF input stream"))
            } else {
                Result.Success(File(uri).inputStream())
            }
        } catch (e: Exception) {
            Result.Error(AppError.Unknown("Exception opening InputStream: ${e.message}", e))
        }
    }

    override suspend fun openOutputStream(uri: String): Result<OutputStream> = withContext(Dispatchers.IO) {
        try {
            if (uri.startsWith("content://")) {
                val stream = context.contentResolver.openOutputStream(Uri.parse(uri))
                if (stream != null) Result.Success(stream) else Result.Error(AppError.FileNotFound("Cannot open SAF output stream"))
            } else {
                Result.Success(File(uri).outputStream())
            }
        } catch (e: Exception) {
            Result.Error(AppError.Unknown("Exception opening OutputStream: ${e.message}", e))
        }
    }
}
