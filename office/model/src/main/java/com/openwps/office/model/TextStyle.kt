package com.openwps.office.model

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
