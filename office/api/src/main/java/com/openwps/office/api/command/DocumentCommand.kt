package com.openwps.office.api.command

import com.openwps.office.model.DocumentRange
import com.openwps.office.model.TextStyle
import com.openwps.office.model.ParagraphStyle
import com.openwps.office.model.SectionProperties

sealed interface DocumentCommand {
    data class InsertText(val range: DocumentRange, val text: String, val style: TextStyle? = null) : DocumentCommand
    data class DeleteRange(val range: DocumentRange) : DocumentCommand
    data class ApplyTextStyle(val range: DocumentRange, val style: TextStyle) : DocumentCommand
    data class ApplyParagraphStyle(val range: DocumentRange, val style: ParagraphStyle) : DocumentCommand
    data class SetSectionProperties(val targetSectionId: String, val properties: SectionProperties) : DocumentCommand
}
