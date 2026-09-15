package com.openwps.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_files")
data class FavoriteFileEntity(
    @PrimaryKey val uri: String,
    val name: String,
    val addedAt: Long,
    val mimeType: String,
    val locationId: String = "unknown",
    val fileType: String = "unknown",
    val sizeBytes: Long = 0L
)
