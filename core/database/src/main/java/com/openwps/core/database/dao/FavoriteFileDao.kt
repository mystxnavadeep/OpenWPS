package com.openwps.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.openwps.core.database.entity.FavoriteFileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteFileDao {
    @Query("SELECT * FROM favorite_files ORDER BY addedAt DESC")
    fun getFavoriteFiles(): Flow<List<FavoriteFileEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(favoriteFile: FavoriteFileEntity)

    @Query("DELETE FROM favorite_files WHERE uri = :uri")
    suspend fun delete(uri: String)
}
