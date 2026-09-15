package com.openwps.app.di

import android.content.Context
import androidx.room.Room
import com.openwps.core.database.OpenWpsDatabase
import com.openwps.core.database.dao.FavoriteFileDao
import com.openwps.core.database.dao.RecentFileDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): OpenWpsDatabase {
        return Room.databaseBuilder(
            context,
            OpenWpsDatabase::class.java,
            "openwps.db"
        ).addMigrations(OpenWpsDatabase.MIGRATION_1_2).build()
    }

    @Provides
    fun provideRecentFileDao(database: OpenWpsDatabase): RecentFileDao = database.recentFileDao()

    @Provides
    fun provideFavoriteFileDao(database: OpenWpsDatabase): FavoriteFileDao = database.favoriteFileDao()
}
