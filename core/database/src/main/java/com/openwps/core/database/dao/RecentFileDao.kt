package com.openwps.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.openwps.core.database.entity.RecentFileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentFileDao {
    @Query("SELECT * FROM recent_files ORDER BY lastAccessed DESC")
    fun getRecentFiles(): Flow<List<RecentFileEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(recentFile: RecentFileEntity)

    @Query("DELETE FROM recent_files WHERE uri = :uri")
    suspend fun delete(uri: String)
    
    @Query("DELETE FROM recent_files")
    suspend fun clearAll()
}
