package com.openwps.office.api.io
import com.openwps.office.api.DocumentSession
import java.io.InputStream

interface DocumentImporter {
    suspend fun importDocument(inputStream: InputStream): DocumentSession
}
