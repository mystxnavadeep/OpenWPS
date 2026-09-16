with open("office/model/src/main/java/com/openwps/office/model/TextStyle.kt", "w") as f:
    f.write("""package com.openwps.office.model

data class TextStyle(
    val fontFamily: String? = null,
    val fontSize: Float? = null,
    val isBold: Boolean? = null,
    val isItalic: Boolean? = null,
    val isUnderline: Boolean? = null,
    val isStrikethrough: Boolean? = null,
    val isSuperscript: Boolean? = null,
    val isSubscript: Boolean? = null,
    val textColorHex: String? = null,
    val highlightColorHex: String? = null
)
""")

with open("office/model/src/main/java/com/openwps/office/model/DocumentStructure.kt", "w") as f:
    f.write("""package com.openwps.office.model

data class TextRun(
    val id: DocumentObjectId,
    val text: String,
    val style: TextStyle
)

data class ParagraphStyle(
    val alignment: String? = null,
    val indentLeft: Float? = null,
    val indentRight: Float? = null,
    val indentFirstLine: Float? = null,
    val spacingBefore: Float? = null,
    val spacingAfter: Float? = null,
    val lineSpacing: Float? = null,
    val headingLevel: Int? = null,
    val isList: Boolean? = null,
    val listId: String? = null,
    val listLevel: Int? = null
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

data class PageSize(
    val width: Float = 0f,
    val height: Float = 0f
)

data class Margins(
    val top: Float = 0f,
    val bottom: Float = 0f,
    val left: Float = 0f,
    val right: Float = 0f
)

data class SectionProperties(
    val pageSize: PageSize? = null,
    val orientation: String? = null, // "PORTRAIT" or "LANDSCAPE"
    val margins: Margins? = null
)

data class Section(
    val id: DocumentObjectId,
    val blocks: List<Block>,
    val properties: SectionProperties? = null
)

data class DocumentModel(
    val id: DocumentObjectId,
    val version: Int,
    val sections: List<Section>
)

data class OutlineNode(
    val id: DocumentObjectId,
    val level: Int,
    val text: String
)

// Foundation for AI granular targeting

fun DocumentModel.resolveWordRange(paragraphId: DocumentObjectId, wordIndex: Int): DocumentRange? {
    return null
}

fun DocumentModel.resolveSentenceRange(paragraphId: DocumentObjectId, sentenceIndex: Int): DocumentRange? {
    return null
}
""")
