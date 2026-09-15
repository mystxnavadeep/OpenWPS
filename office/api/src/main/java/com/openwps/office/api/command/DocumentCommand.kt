package com.openwps.office.api.command
import com.openwps.office.model.DocumentRange
import com.openwps.office.model.TextStyle

sealed interface DocumentCommand {
    data class InsertText(val range: DocumentRange, val text: String, val style: TextStyle? = null) : DocumentCommand
    data class DeleteRange(val range: DocumentRange) : DocumentCommand
    data class ApplyTextStyle(val range: DocumentRange, val style: TextStyle) : DocumentCommand
}
