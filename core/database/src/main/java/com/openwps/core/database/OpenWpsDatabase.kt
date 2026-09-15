package com.openwps.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.openwps.core.database.dao.FavoriteFileDao
import com.openwps.core.database.dao.RecentFileDao
import com.openwps.core.database.entity.FavoriteFileEntity
import com.openwps.core.database.entity.RecentFileEntity

@Database(
    entities = [
        RecentFileEntity::class,
        FavoriteFileEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class OpenWpsDatabase : RoomDatabase() {
    abstract fun recentFileDao(): RecentFileDao
    abstract fun favoriteFileDao(): FavoriteFileDao
}
