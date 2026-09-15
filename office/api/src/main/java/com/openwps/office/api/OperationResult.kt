package com.openwps.office.api
import com.openwps.office.model.DocumentRange
data class OperationResult(
    val success: Boolean,
    val operationId: String,
    val affectedRange: DocumentRange? = null,
    val errorMessage: String? = null
)
