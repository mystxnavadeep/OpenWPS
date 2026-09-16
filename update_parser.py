with open("engines/document/src/main/java/com/openwps/engines/document/DocumentStructureParser.kt", "r") as f:
    content = f.read()

new_parse_section = """    private fun parseSection(obj: JSONObject): Section {
        val id = obj.getString("id")
        val blocks = mutableListOf<Block>()
        val blocksArr = obj.getJSONArray("blocks")
        for (i in 0 until blocksArr.length()) {
            blocks.add(parseBlock(blocksArr.getJSONObject(i)))
        }
        
        var props: SectionProperties? = null
        if (obj.has("properties")) {
            val pObj = obj.getJSONObject("properties")
            
            var pageSize: PageSize? = null
            if (pObj.has("pageSize")) {
                val sz = pObj.getJSONObject("pageSize")
                pageSize = PageSize(sz.getDouble("width").toFloat(), sz.getDouble("height").toFloat())
            }
            
            var margins: Margins? = null
            if (pObj.has("margins")) {
                val m = pObj.getJSONObject("margins")
                margins = Margins(m.getDouble("top").toFloat(), m.getDouble("bottom").toFloat(), m.getDouble("left").toFloat(), m.getDouble("right").toFloat())
            }
            
            val orientation = if (pObj.has("orientation")) pObj.getString("orientation") else null
            
            props = SectionProperties(pageSize, orientation, margins)
        }
        
        return Section(DocumentObjectId(id), blocks, props)
    }"""

content = content.replace("    private fun parseSection(obj: JSONObject): Section {", new_parse_section.split("\n", 1)[0])
content = content.replace("        return Section(DocumentObjectId(id), blocks)\n    }", "        return Section(DocumentObjectId(id), blocks, props)\n    }")

# Replace parseSection completely to be safe
import re
content = re.sub(r'    private fun parseSection\(obj: JSONObject\): Section \{.*?\n    \}', new_parse_section, content, flags=re.DOTALL)


new_parse_para_style = """    private fun parseParagraphStyle(obj: JSONObject): ParagraphStyle {
        return ParagraphStyle(
            alignment = if (obj.has("alignment")) obj.getString("alignment") else null,
            indentLeft = if (obj.has("indentLeft")) obj.getDouble("indentLeft").toFloat() else null,
            indentRight = if (obj.has("indentRight")) obj.getDouble("indentRight").toFloat() else null,
            indentFirstLine = if (obj.has("indentFirstLine")) obj.getDouble("indentFirstLine").toFloat() else null,
            spacingBefore = if (obj.has("spacingBefore")) obj.getDouble("spacingBefore").toFloat() else null,
            spacingAfter = if (obj.has("spacingAfter")) obj.getDouble("spacingAfter").toFloat() else null,
            lineSpacing = if (obj.has("lineSpacing")) obj.getDouble("lineSpacing").toFloat() else null,
            headingLevel = if (obj.has("headingLevel")) obj.getInt("headingLevel") else null,
            isList = if (obj.has("isList")) obj.getBoolean("isList") else null,
            listId = if (obj.has("listId")) obj.getString("listId") else null,
            listLevel = if (obj.has("listLevel")) obj.getInt("listLevel") else null
        )
    }"""

content = re.sub(r'    private fun parseParagraphStyle\(obj: JSONObject\): ParagraphStyle \{.*?\n    \}', new_parse_para_style, content, flags=re.DOTALL)

new_parse_text_style = """    private fun parseTextStyle(obj: JSONObject): TextStyle {
        return TextStyle(
            fontFamily = if (obj.has("fontFamily")) obj.getString("fontFamily") else null,
            fontSize = if (obj.has("fontSize")) obj.getDouble("fontSize").toFloat() else null,
            isBold = if (obj.has("isBold")) obj.getBoolean("isBold") else null,
            isItalic = if (obj.has("isItalic")) obj.getBoolean("isItalic") else null,
            isUnderline = if (obj.has("isUnderline")) obj.getBoolean("isUnderline") else null,
            isStrikethrough = if (obj.has("isStrikethrough")) obj.getBoolean("isStrikethrough") else null,
            isSuperscript = if (obj.has("isSuperscript")) obj.getBoolean("isSuperscript") else null,
            isSubscript = if (obj.has("isSubscript")) obj.getBoolean("isSubscript") else null,
            textColorHex = if (obj.has("textColorHex")) obj.getString("textColorHex") else null,
            highlightColorHex = if (obj.has("highlightColorHex")) obj.getString("highlightColorHex") else null
        )
    }"""

content = re.sub(r'    private fun parseTextStyle\(obj: JSONObject\): TextStyle \{.*?\n    \}', new_parse_text_style, content, flags=re.DOTALL)

with open("engines/document/src/main/java/com/openwps/engines/document/DocumentStructureParser.kt", "w") as f:
    f.write(content)

print("DocumentStructureParser updated!")
