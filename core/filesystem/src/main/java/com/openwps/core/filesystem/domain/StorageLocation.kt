package com.openwps.core.filesystem.domain

sealed interface StorageLocation {
    val id: String
    val displayName: String
    
    data class Internal(override val id: String = "internal", override val displayName: String = "Internal Storage") : StorageLocation
    data class Saf(override val id: String, override val displayName: String, val rootUri: String) : StorageLocation
    data class Recent(override val id: String = "recent", override val displayName: String = "Recent Files") : StorageLocation
    data class Favorites(override val id: String = "favorites", override val displayName: String = "Favorites") : StorageLocation
}
