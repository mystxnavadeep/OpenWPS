package com.openwps.core.common.result

sealed class AppError(open val message: String, open val cause: Throwable? = null) {
    data class FileNotFound(override val message: String = "File not found", override val cause: Throwable? = null) : AppError(message, cause)
    data class PermissionDenied(override val message: String = "Permission denied", override val cause: Throwable? = null) : AppError(message, cause)
    data class InvalidFile(override val message: String = "Invalid file", override val cause: Throwable? = null) : AppError(message, cause)
    data class UnsupportedFormat(override val message: String = "Unsupported format", override val cause: Throwable? = null) : AppError(message, cause)
    data class DatabaseError(override val message: String = "Database error", override val cause: Throwable? = null) : AppError(message, cause)
    data class NetworkError(override val message: String = "Network error", override val cause: Throwable? = null) : AppError(message, cause)
    data class NativeEngineError(override val message: String = "Native engine error", override val cause: Throwable? = null) : AppError(message, cause)
    data class Cancelled(override val message: String = "Operation cancelled", override val cause: Throwable? = null) : AppError(message, cause)
    data class Unknown(override val message: String = "Unknown error", override val cause: Throwable? = null) : AppError(message, cause)
}
