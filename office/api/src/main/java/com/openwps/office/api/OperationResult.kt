package com.openwps.office.api

import com.openwps.office.model.DocumentRange

enum class ErrorCode {
    NONE,
    INVALID_DOCUMENT,
    INVALID_SESSION,
    INVALID_OBJECT_ID,
    INVALID_RANGE,
    INVALID_WORD_INDEX,
    INVALID_SENTENCE_INDEX,
    UNSUPPORTED_OPERATION,
    CAPABILITY_UNAVAILABLE,
    INVALID_COMMAND,
    CONFLICTING_DOCUMENT_STATE,
    UNKNOWN_ERROR
}

data class OperationResult(
    val success: Boolean,
    val operationId: String,
    val affectedRange: DocumentRange? = null,
    val errorCode: ErrorCode = ErrorCode.NONE,
    val errorMessage: String? = null
)
