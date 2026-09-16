import re

with open("engines/document/src/main/java/com/openwps/engines/document/NativeDocumentSession.kt", "r") as f:
    content = f.read()

# Replace ApplyTextStyle logic
apply_text_style_old = """is DocumentCommand.ApplyTextStyle -> {
                val styleJson = \"\"\"{"isBold":${command.style.isBold},"isItalic":${command.style.isItalic},"isUnderline":${command.style.isUnderline}}\"\"\"
                NativeBridge.applyTextStyle(sessionPtr, command.range.startObjectId.id, command.range.startOffset, command.range.endObjectId.id, command.range.endOffset, styleJson)
            }"""

apply_text_style_new = """is DocumentCommand.ApplyTextStyle -> {
                val styleJson = \"\"\"{"isBold":${command.style.isBold},"isItalic":${command.style.isItalic},"isUnderline":${command.style.isUnderline},"isStrikethrough":${command.style.isStrikethrough},"isSuperscript":${command.style.isSuperscript},"isSubscript":${command.style.isSubscript}}\"\"\"
                NativeBridge.applyTextStyle(sessionPtr, command.range.startObjectId.id, command.range.startOffset, command.range.endObjectId.id, command.range.endOffset, styleJson)
            }
            is DocumentCommand.ApplyParagraphStyle -> {
                val p = command.style
                val styleJson = \"\"\"{"alignment":"${p.alignment}","headingLevel":${p.headingLevel},"isList":${p.isList}}\"\"\"
                NativeBridge.applyParagraphStyle(sessionPtr, command.range.startObjectId.id, styleJson)
            }
            is DocumentCommand.SetSectionProperties -> {
                val p = command.properties
                val propsJson = \"\"\"{"orientation":"${p.orientation}"}\"\"\"
                NativeBridge.setSectionProperties(sessionPtr, command.targetSectionId, propsJson)
            }"""

content = content.replace(apply_text_style_old, apply_text_style_new)

# Replace InsertText styleJson
insert_text_style_old = """\"\"\"{"isBold":${it.isBold},"isItalic":${it.isItalic},"isUnderline":${it.isUnderline},"textColorHex":${if(it.textColorHex==null) "null" else "\"${it.textColorHex}\""}}\"\"\""""
insert_text_style_new = """\"\"\"{"isBold":${it.isBold},"isItalic":${it.isItalic},"isUnderline":${it.isUnderline},"isStrikethrough":${it.isStrikethrough},"isSuperscript":${it.isSuperscript},"isSubscript":${it.isSubscript},"textColorHex":${if(it.textColorHex==null) "null" else "\"${it.textColorHex}\""}}\"\"\""""

content = content.replace(insert_text_style_old, insert_text_style_new)

# Add getDocumentOutline
outline_method = """override suspend fun getDocumentOutline(): List<com.openwps.office.model.OutlineNode> {
        val jsonStr = NativeBridge.getDocumentOutline(sessionPtr)
        val list = mutableListOf<com.openwps.office.model.OutlineNode>()
        try {
            val arr = JSONArray(jsonStr)
            for (i in 0 until arr.length()) {
                val obj = arr.getJSONObject(i)
                list.add(com.openwps.office.model.OutlineNode(
                    id = DocumentObjectId(obj.getString("id")),
                    level = obj.getInt("level"),
                    text = obj.getString("text")
                ))
            }
        } catch (e: Exception) {
            // Ignore
        }
        return list
    }"""

content = content.replace("override suspend fun getDocumentStructure(): DocumentModel {", outline_method + "\n\n    override suspend fun getDocumentStructure(): DocumentModel {")

with open("engines/document/src/main/java/com/openwps/engines/document/NativeDocumentSession.kt", "w") as f:
    f.write(content)

print("NativeDocumentSession updated!")
