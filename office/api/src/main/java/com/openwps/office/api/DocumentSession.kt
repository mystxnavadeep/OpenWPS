package com.openwps.office.api
import com.openwps.office.api.command.DocumentCommand
import com.openwps.office.model.DocumentRange

interface DocumentSession {
    val sessionId: String
    val capabilityRegistry: CapabilityRegistry
    
    suspend fun applyCommand(command: DocumentCommand): OperationResult
    suspend fun getText(range: DocumentRange): String
    suspend fun save(): OperationResult
    suspend fun close()
}
