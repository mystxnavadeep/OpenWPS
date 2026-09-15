package com.openwps.app.di

import android.content.Context
import com.openwps.core.filesystem.OpenWpsFileSystem
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.openwps.core.common.result.Result
import com.openwps.core.filesystem.FileMetadata
import java.io.InputStream
import java.io.OutputStream

// Dummy implementation for now to satisfy DI
class AndroidFileSystem(private val context: Context) : OpenWpsFileSystem {
    override suspend fun listDirectory(uri: String): Result<List<FileMetadata>> = Result.Success(emptyList())
    override suspend fun getMetadata(uri: String): Result<FileMetadata> = Result.Success(FileMetadata(uri, "dummy", 0, 0, false))
    override suspend fun checkExistence(uri: String): Result<Boolean> = Result.Success(true)
    override suspend fun createDirectory(parentUri: String, name: String): Result<FileMetadata> = Result.Success(FileMetadata("$parentUri/$name", name, 0, 0, true))
    override suspend fun createFile(parentUri: String, name: String, mimeType: String): Result<FileMetadata> = Result.Success(FileMetadata("$parentUri/$name", name, 0, 0, false))
    override suspend fun delete(uri: String): Result<Unit> = Result.Success(Unit)
    override suspend fun rename(uri: String, newName: String): Result<FileMetadata> = Result.Success(FileMetadata(uri, newName, 0, 0, false))
    override suspend fun move(sourceUri: String, destinationParentUri: String): Result<FileMetadata> = Result.Success(FileMetadata(destinationParentUri, "moved", 0, 0, false))
    override suspend fun openInputStream(uri: String): Result<InputStream> = Result.Error(com.openwps.core.common.result.AppError.FileNotFound())
    override suspend fun openOutputStream(uri: String): Result<OutputStream> = Result.Error(com.openwps.core.common.result.AppError.FileNotFound())
}

@Module
@InstallIn(SingletonComponent::class)
object FileSystemModule {

    @Provides
    @Singleton
    fun provideFileSystem(@ApplicationContext context: Context): OpenWpsFileSystem {
        return AndroidFileSystem(context)
    }
}
