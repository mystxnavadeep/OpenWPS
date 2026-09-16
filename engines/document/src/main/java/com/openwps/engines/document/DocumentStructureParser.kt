package com.openwps.engines.document

import com.openwps.office.model.*
import org.json.JSONObject
import org.json.JSONArray

object DocumentStructureParser {
    fun parseDocument(jsonStr: String): DocumentModel {
        val root = JSONObject(jsonStr)
        val id = root.getString("id")
        val version = root.getInt("version")
        val sections = mutableListOf<Section>()
        
        val sectionsArr = root.getJSONArray("sections")
        for (i in 0 until sectionsArr.length()) {
            sections.add(parseSection(sectionsArr.getJSONObject(i)))
        }
        
        return DocumentModel(DocumentObjectId(id), version, sections)
    }
    
    private fun parseSection(obj: JSONObject): Section {
        val id = obj.getString("id")
        val blocks = mutableListOf<Block>()
        val blocksArr = obj.getJSONArray("blocks")
        for (i in 0 until blocksArr.length()) {
            blocks.add(parseBlock(blocksArr.getJSONObject(i)))
        }
        return Section(DocumentObjectId(id), blocks)
    }
    
    private fun parseBlock(obj: JSONObject): Block {
        val id = obj.getString("id")
        var paragraph: Paragraph? = null
        if (obj.has("paragraph")) {
            paragraph = parseParagraph(obj.getJSONObject("paragraph"))
        }
        return Block(DocumentObjectId(id), paragraph)
    }
    
    private fun parseParagraph(obj: JSONObject): Paragraph {
        val id = obj.getString("id")
        val runs = mutableListOf<TextRun>()
        val runsArr = obj.getJSONArray("runs")
        for (i in 0 until runsArr.length()) {
            runs.add(parseTextRun(runsArr.getJSONObject(i)))
        }
        val style = parseParagraphStyle(obj.getJSONObject("style"))
        return Paragraph(DocumentObjectId(id), runs, style)
    }
    
    private fun parseTextRun(obj: JSONObject): TextRun {
        val id = obj.getString("id")
        val text = obj.getString("text")
        val style = parseTextStyle(obj.getJSONObject("style"))
        return TextRun(DocumentObjectId(id), text, style)
    }
    
    private fun parseParagraphStyle(obj: JSONObject): ParagraphStyle {
        return ParagraphStyle(
            alignment = if (obj.has("alignment")) obj.getString("alignment") else null
        )
    }
    
    private fun parseTextStyle(obj: JSONObject): TextStyle {
        return TextStyle(
            fontFamily = if (obj.has("fontFamily")) obj.getString("fontFamily") else null,
            fontSize = if (obj.has("fontSize")) obj.getDouble("fontSize").toFloat() else null,
            isBold = if (obj.has("isBold")) obj.getBoolean("isBold") else null,
            isItalic = if (obj.has("isItalic")) obj.getBoolean("isItalic") else null,
            isUnderline = if (obj.has("isUnderline")) obj.getBoolean("isUnderline") else null,
            textColorHex = if (obj.has("textColorHex")) obj.getString("textColorHex") else null
        )
    }
}
