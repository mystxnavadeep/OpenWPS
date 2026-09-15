package com.openwps.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recent_files")
data class RecentFileEntity(
    @PrimaryKey val uri: String,
    val name: String,
    val lastAccessed: Long,
    val mimeType: String,
    val locationId: String = "unknown",
    val fileType: String = "unknown",
    val sizeBytes: Long = 0L
)
