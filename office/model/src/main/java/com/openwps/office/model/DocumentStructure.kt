package com.openwps.office.model

data class TextRun(
    val id: DocumentObjectId,
    val text: String,
    val style: TextStyle
)

data class ParagraphStyle(
    val alignment: String? = null
)

data class Paragraph(
    val id: DocumentObjectId,
    val runs: List<TextRun>,
    val style: ParagraphStyle
)

data class Block(
    val id: DocumentObjectId,
    val paragraph: Paragraph? = null
)

data class Section(
    val id: DocumentObjectId,
    val blocks: List<Block>
)

data class DocumentModel(
    val id: DocumentObjectId,
    val version: Int,
    val sections: List<Section>
)

// Foundation for AI granular targeting

fun DocumentModel.resolveWordRange(paragraphId: DocumentObjectId, wordIndex: Int): DocumentRange? {
    // Conceptual foundation: find paragraph, iterate through runs, extract the Nth word's range.
    return null
}

fun DocumentModel.resolveSentenceRange(paragraphId: DocumentObjectId, sentenceIndex: Int): DocumentRange? {
    // Conceptual foundation: find paragraph, iterate runs, identify sentence boundaries based on punctuation.
    return null
}
