package com.openwps.core.filesystem.domain

import org.junit.Assert.assertEquals
import org.junit.Test

class FileTypeDetectorTest {

    @Test
    fun detectFromExtension_document() {
        assertEquals(FileType.Document, FileTypeDetector.detectFromExtension("test.doc"))
        assertEquals(FileType.Document, FileTypeDetector.detectFromExtension("test.docx"))
        assertEquals(FileType.Document, FileTypeDetector.detectFromExtension("test.txt"))
    }

    @Test
    fun detectFromExtension_spreadsheet() {
        assertEquals(FileType.Spreadsheet, FileTypeDetector.detectFromExtension("test.xls"))
        assertEquals(FileType.Spreadsheet, FileTypeDetector.detectFromExtension("test.csv"))
    }

    @Test
    fun detectFromExtension_presentation() {
        assertEquals(FileType.Presentation, FileTypeDetector.detectFromExtension("test.pptx"))
    }

    @Test
    fun detectFromExtension_pdf() {
        assertEquals(FileType.Pdf, FileTypeDetector.detectFromExtension("test.pdf"))
    }

    @Test
    fun detectFromExtension_image() {
        assertEquals(FileType.Image, FileTypeDetector.detectFromExtension("test.png"))
        assertEquals(FileType.Image, FileTypeDetector.detectFromExtension("test.jpg"))
    }

    @Test
    fun detectFromExtension_archive() {
        assertEquals(FileType.Archive, FileTypeDetector.detectFromExtension("test.zip"))
    }

    @Test
    fun detectFromExtension_unknown() {
        assertEquals(FileType.Unknown, FileTypeDetector.detectFromExtension("test.unknown"))
        assertEquals(FileType.Unknown, FileTypeDetector.detectFromExtension("test"))
    }

    @Test
    fun detectFromMimeType_document() {
        assertEquals(FileType.Document, FileTypeDetector.detectFromMimeType("text/plain"))
        assertEquals(FileType.Document, FileTypeDetector.detectFromMimeType("application/msword"))
    }

    @Test
    fun detectFromMimeType_image() {
        assertEquals(FileType.Image, FileTypeDetector.detectFromMimeType("image/jpeg"))
        assertEquals(FileType.Image, FileTypeDetector.detectFromMimeType("image/png"))
    }
}
