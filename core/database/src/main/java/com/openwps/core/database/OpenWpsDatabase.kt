package com.openwps.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.openwps.core.database.dao.FavoriteFileDao
import com.openwps.core.database.dao.RecentFileDao
import com.openwps.core.database.entity.FavoriteFileEntity
import com.openwps.core.database.entity.RecentFileEntity

@Database(
    entities = [
        RecentFileEntity::class,
        FavoriteFileEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class OpenWpsDatabase : RoomDatabase() {
    abstract fun recentFileDao(): RecentFileDao
    abstract fun favoriteFileDao(): FavoriteFileDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE recent_files ADD COLUMN locationId TEXT NOT NULL DEFAULT 'unknown'")
                db.execSQL("ALTER TABLE recent_files ADD COLUMN fileType TEXT NOT NULL DEFAULT 'unknown'")
                db.execSQL("ALTER TABLE recent_files ADD COLUMN sizeBytes INTEGER NOT NULL DEFAULT 0")
                
                db.execSQL("ALTER TABLE favorite_files ADD COLUMN locationId TEXT NOT NULL DEFAULT 'unknown'")
                db.execSQL("ALTER TABLE favorite_files ADD COLUMN fileType TEXT NOT NULL DEFAULT 'unknown'")
                db.execSQL("ALTER TABLE favorite_files ADD COLUMN sizeBytes INTEGER NOT NULL DEFAULT 0")
            }
        }
    }
}
