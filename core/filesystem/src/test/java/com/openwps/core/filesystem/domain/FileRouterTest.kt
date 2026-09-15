package com.openwps.core.filesystem.domain

import org.junit.Assert.assertEquals
import org.junit.Test

class FileRouterTest {

    @Test
    fun getTargetFor_document() {
        assertEquals(OpenTarget.DocumentEditor, FileRouter.getTargetFor(FileType.Document))
    }

    @Test
    fun getTargetFor_spreadsheet() {
        assertEquals(OpenTarget.SpreadsheetEditor, FileRouter.getTargetFor(FileType.Spreadsheet))
    }

    @Test
    fun getTargetFor_presentation() {
        assertEquals(OpenTarget.PresentationEditor, FileRouter.getTargetFor(FileType.Presentation))
    }

    @Test
    fun getTargetFor_pdf() {
        assertEquals(OpenTarget.PdfViewer, FileRouter.getTargetFor(FileType.Pdf))
    }

    @Test
    fun getTargetFor_image() {
        assertEquals(OpenTarget.ImageViewer, FileRouter.getTargetFor(FileType.Image))
    }

    @Test
    fun getTargetFor_folder() {
        assertEquals(OpenTarget.FolderViewer, FileRouter.getTargetFor(FileType.Folder))
    }

    @Test
    fun getTargetFor_unsupported() {
        assertEquals(OpenTarget.Unsupported, FileRouter.getTargetFor(FileType.Archive))
        assertEquals(OpenTarget.Unsupported, FileRouter.getTargetFor(FileType.Unknown))
    }
}
