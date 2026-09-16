package com.openwps.office.api

import com.openwps.office.api.command.DocumentCommand
import com.openwps.office.model.DocumentModel
import com.openwps.office.model.DocumentRange

interface DocumentSession {
    val sessionId: String
    val capabilityRegistry: CapabilityRegistry
    
    suspend fun applyCommand(command: DocumentCommand): OperationResult
    suspend fun getText(range: DocumentRange): String
    suspend fun getDocumentStructure(): DocumentModel
    suspend fun save(): OperationResult
    suspend fun close()

    // Precision target resolution & search
    suspend fun resolveWord(paragraphId: String, wordIndex: Int): OperationResult
    suspend fun resolveSentence(paragraphId: String, sentenceIndex: Int): OperationResult
    suspend fun search(query: String, matchCase: Boolean): List<DocumentRange>
}
