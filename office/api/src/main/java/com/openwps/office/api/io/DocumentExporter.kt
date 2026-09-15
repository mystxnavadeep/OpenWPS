package com.openwps.office.api.io
import com.openwps.office.api.DocumentSession
import java.io.OutputStream

interface DocumentExporter {
    suspend fun exportDocument(session: DocumentSession, outputStream: OutputStream)
}
