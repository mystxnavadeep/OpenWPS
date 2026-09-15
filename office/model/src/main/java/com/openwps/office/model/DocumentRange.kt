package com.openwps.office.model
data class DocumentRange(
    val startObjectId: DocumentObjectId,
    val startOffset: Int,
    val endObjectId: DocumentObjectId,
    val endOffset: Int
)
