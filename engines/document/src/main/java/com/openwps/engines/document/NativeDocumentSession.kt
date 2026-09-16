package com.openwps.engines.document

import com.openwps.office.api.CapabilityRegistry
import com.openwps.office.api.DocumentSession
import com.openwps.office.api.ErrorCode
import com.openwps.office.api.OperationResult
import com.openwps.office.api.command.DocumentCommand
import com.openwps.office.model.DocumentModel
import com.openwps.office.model.DocumentObjectId
import com.openwps.office.model.DocumentRange
import com.openwps.ndk.jni.NativeBridge
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

class NativeDocumentSession(
    override val sessionId: String,
    override val capabilityRegistry: CapabilityRegistry
) : DocumentSession {

    private var sessionPtr: Long = 0L

    init {
        sessionPtr = NativeBridge.createSession(sessionId)
    }

    private fun parseOperationResult(jsonStr: String, opId: String): OperationResult {
        return try {
            val obj = JSONObject(jsonStr)
            val success = obj.optBoolean("success", false)
            val errCodeStr = obj.optString("errorCode", "NONE")
            val errorCode = try { ErrorCode.valueOf(errCodeStr) } catch(e: Exception) { ErrorCode.UNKNOWN_ERROR }
            val errorMessage = obj.optString("errorMessage", null).takeIf { it.isNotEmpty() }
            
            var affectedRange: DocumentRange? = null
            if (obj.has("affectedRange") && !obj.isNull("affectedRange")) {
                val rangeObj = obj.getJSONObject("affectedRange")
                affectedRange = DocumentRange(
                    startObjectId = DocumentObjectId(rangeObj.getString("startObjectId")),
                    startOffset = rangeObj.getInt("startOffset"),
                    endObjectId = DocumentObjectId(rangeObj.getString("endObjectId")),
                    endOffset = rangeObj.getInt("endOffset")
                )
            }
            
            OperationResult(success, opId, affectedRange, errorCode, errorMessage)
        } catch (e: Exception) {
            OperationResult(false, opId, null, ErrorCode.UNKNOWN_ERROR, "Failed to parse result: ${e.message}")
        }
    }

    override suspend fun applyCommand(command: DocumentCommand): OperationResult {
        val opId = "op_${UUID.randomUUID()}"
        val resultJson = when (command) {
            is DocumentCommand.InsertText -> {
                val styleJson = command.style?.let { 
                    """{"isBold":${it.isBold},"isItalic":${it.isItalic},"isUnderline":${it.isUnderline},"textColorHex":${if(it.textColorHex==null) "null" else "\"${it.textColorHex}\""}}""" 
                }
                NativeBridge.insertText(sessionPtr, command.range.startObjectId.id, command.range.startOffset, command.text, styleJson)
            }
            is DocumentCommand.DeleteRange -> {
                NativeBridge.deleteRange(sessionPtr, command.range.startObjectId.id, command.range.startOffset, command.range.endObjectId.id, command.range.endOffset)
            }
            is DocumentCommand.ApplyTextStyle -> {
                val styleJson = """{"isBold":${command.style.isBold},"isItalic":${command.style.isItalic},"isUnderline":${command.style.isUnderline},"isStrikethrough":${command.style.isStrikethrough},"isSuperscript":${command.style.isSuperscript},"isSubscript":${command.style.isSubscript}}"""
                NativeBridge.applyTextStyle(sessionPtr, command.range.startObjectId.id, command.range.startOffset, command.range.endObjectId.id, command.range.endOffset, styleJson)
            }
            is DocumentCommand.ApplyParagraphStyle -> {
                val p = command.style
                val styleJson = """{"alignment":"${p.alignment}","headingLevel":${p.headingLevel},"isList":${p.isList}}"""
                NativeBridge.applyParagraphStyle(sessionPtr, command.range.startObjectId.id, styleJson)
            }
            is DocumentCommand.SetSectionProperties -> {
                val p = command.properties
                val propsJson = """{"orientation":"${p.orientation}"}"""
                NativeBridge.setSectionProperties(sessionPtr, command.targetSectionId, propsJson)
            }
            else -> return OperationResult(success = false, operationId = opId, errorCode = ErrorCode.UNSUPPORTED_OPERATION, errorMessage = "Command not yet supported by native engine")
        }
        return parseOperationResult(resultJson, opId)
    }

    override suspend fun resolveWord(paragraphId: String, wordIndex: Int): OperationResult {
        val opId = "op_${UUID.randomUUID()}"
        val json = NativeBridge.resolveWord(sessionPtr, paragraphId, wordIndex)
        return parseOperationResult(json, opId)
    }

    override suspend fun resolveSentence(paragraphId: String, sentenceIndex: Int): OperationResult {
        val opId = "op_${UUID.randomUUID()}"
        val json = NativeBridge.resolveSentence(sessionPtr, paragraphId, sentenceIndex)
        return parseOperationResult(json, opId)
    }

    override suspend fun search(query: String, matchCase: Boolean): List<DocumentRange> {
        val jsonStr = NativeBridge.search(sessionPtr, query, matchCase)
        val list = mutableListOf<DocumentRange>()
        try {
            val arr = JSONArray(jsonStr)
            for (i in 0 until arr.length()) {
                val rangeObj = arr.getJSONObject(i)
                list.add(DocumentRange(
                    startObjectId = DocumentObjectId(rangeObj.getString("startObjectId")),
                    startOffset = rangeObj.getInt("startOffset"),
                    endObjectId = DocumentObjectId(rangeObj.getString("endObjectId")),
                    endOffset = rangeObj.getInt("endOffset")
                ))
            }
        } catch (e: Exception) {
            // Log or ignore
        }
        return list
    }

    override suspend fun getText(range: DocumentRange): String {
        return NativeBridge.getTextRange(sessionPtr, range.startObjectId.id, range.startOffset, range.endObjectId.id, range.endOffset)
    }

    override suspend fun getDocumentOutline(): List<com.openwps.office.model.OutlineNode> {
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
    }

    override suspend fun getDocumentStructure(): DocumentModel {
        val jsonStr = NativeBridge.getDocumentStructure(sessionPtr)
        return DocumentStructureParser.parseDocument(jsonStr)
    }

    override suspend fun save(): OperationResult {
        return OperationResult(success = true, operationId = "save", errorCode = ErrorCode.NONE)
    }

    override suspend fun close() {
        if (sessionPtr != 0L) {
            NativeBridge.destroySession(sessionPtr)
            sessionPtr = 0L
        }
    }
}
