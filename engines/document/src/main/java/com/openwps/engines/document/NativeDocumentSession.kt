package com.openwps.engines.document

import com.openwps.office.api.CapabilityRegistry
import com.openwps.office.api.DocumentSession
import com.openwps.office.api.OperationResult
import com.openwps.office.api.command.DocumentCommand
import com.openwps.office.model.DocumentModel
import com.openwps.office.model.DocumentRange
import com.openwps.ndk.jni.NativeBridge

class NativeDocumentSession(
    override val sessionId: String,
    override val capabilityRegistry: CapabilityRegistry
) : DocumentSession {

    private var sessionPtr: Long = 0L

    init {
        sessionPtr = NativeBridge.createSession(sessionId)
    }

    override suspend fun applyCommand(command: DocumentCommand): OperationResult {
        return when (command) {
            is DocumentCommand.InsertText -> {
                val success = NativeBridge.insertText(sessionPtr, command.text)
                OperationResult(
                    success = success,
                    operationId = "op_${System.currentTimeMillis()}",
                    errorMessage = if (success) null else "Native engine failed to insert text"
                )
            }
            is DocumentCommand.DeleteRange -> {
                val success = NativeBridge.deleteRange(sessionPtr, command.range.startObjectId.id, command.range.startOffset, command.range.endObjectId.id, command.range.endOffset)
                OperationResult(
                    success = success,
                    operationId = "op_${System.currentTimeMillis()}",
                    errorMessage = if (success) null else "Native engine failed to delete range"
                )
            }
            is DocumentCommand.ApplyTextStyle -> {
                val styleJson = """{"isBold":${command.style.isBold},"isItalic":${command.style.isItalic},"isUnderline":${command.style.isUnderline}}"""
                val success = NativeBridge.applyTextStyle(sessionPtr, command.range.startObjectId.id, command.range.startOffset, command.range.endObjectId.id, command.range.endOffset, styleJson)
                OperationResult(
                    success = success,
                    operationId = "op_${System.currentTimeMillis()}",
                    errorMessage = if (success) null else "Native engine failed to apply text style"
                )
            }
            else -> {
                OperationResult(success = false, operationId = "unknown", errorMessage = "Command not yet supported by native engine")
            }
        }
    }

    override suspend fun getText(range: DocumentRange): String {
        return NativeBridge.getText(sessionPtr)
    }

    override suspend fun getDocumentStructure(): DocumentModel {
        val jsonStr = NativeBridge.getDocumentStructure(sessionPtr)
        return DocumentStructureParser.parseDocument(jsonStr)
    }

    override suspend fun save(): OperationResult {
        return OperationResult(success = true, operationId = "save")
    }

    override suspend fun close() {
        if (sessionPtr != 0L) {
            NativeBridge.destroySession(sessionPtr)
            sessionPtr = 0L
        }
    }
}
