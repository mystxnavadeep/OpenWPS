package com.openwps.app.di

import android.content.Context
import com.openwps.core.filesystem.OpenWpsFileSystem
import com.openwps.core.filesystem.AndroidFileSystem
import com.openwps.core.filesystem.preferences.FileManagerPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FileSystemModule {

    @Provides
    @Singleton
    fun provideFileSystem(@ApplicationContext context: Context): OpenWpsFileSystem {
        return AndroidFileSystem(context)
    }

    @Provides
    @Singleton
    fun provideFileManagerPreferences(@ApplicationContext context: Context): FileManagerPreferences {
        return FileManagerPreferences(context)
    }
}
