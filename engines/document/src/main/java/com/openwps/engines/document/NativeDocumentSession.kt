package com.openwps.engines.document

import com.openwps.office.api.CapabilityRegistry
import com.openwps.office.api.DocumentSession
import com.openwps.office.api.OperationResult
import com.openwps.office.api.command.DocumentCommand
import com.openwps.office.model.DocumentRange
import com.openwps.ndk.jni.NativeBridge

class NativeDocumentSession(
    override val sessionId: String,
    override val capabilityRegistry: CapabilityRegistry
) : DocumentSession {

    private var sessionPtr: Long = 0L

    init {
        sessionPtr = NativeBridge.instance.createSession(sessionId)
    }

    override suspend fun applyCommand(command: DocumentCommand): OperationResult {
        return when (command) {
            is DocumentCommand.InsertText -> {
                val success = NativeBridge.instance.insertText(sessionPtr, command.text)
                OperationResult(
                    success = success,
                    operationId = "op_${System.currentTimeMillis()}",
                    errorMessage = if (success) null else "Native engine failed to insert text"
                )
            }
            else -> {
                OperationResult(success = false, operationId = "unknown", errorMessage = "Command not yet supported by native engine")
            }
        }
    }

    override suspend fun getText(range: DocumentRange): String {
        return NativeBridge.instance.getText(sessionPtr)
    }

    override suspend fun save(): OperationResult {
        return OperationResult(success = true, operationId = "save")
    }

    override suspend fun close() {
        if (sessionPtr != 0L) {
            NativeBridge.instance.destroySession(sessionPtr)
            sessionPtr = 0L
        }
    }
}
