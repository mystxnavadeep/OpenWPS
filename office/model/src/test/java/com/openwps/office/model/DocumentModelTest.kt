package com.openwps.office.model

import org.junit.Assert.assertEquals
import org.junit.Test

class DocumentModelTest {

    @Test
    fun testDocumentStructureConstruction() {
        val run1 = TextRun(DocumentObjectId("r1"), "Hello ", TextStyle(isBold = true))
        val run2 = TextRun(DocumentObjectId("r2"), "world!", TextStyle(isBold = false))
        
        val paragraph = Paragraph(
            id = DocumentObjectId("p1"),
            runs = listOf(run1, run2),
            style = ParagraphStyle(alignment = "center")
        )
        
        val block = Block(DocumentObjectId("b1"), paragraph)
        val section = Section(DocumentObjectId("s1"), listOf(block))
        val doc = DocumentModel(DocumentObjectId("d1"), 1, listOf(section))
        
        assertEquals("d1", doc.id.id)
        assertEquals(1, doc.version)
        assertEquals(1, doc.sections.size)
        
        val firstPar = doc.sections[0].blocks[0].paragraph!!
        assertEquals("p1", firstPar.id.id)
        assertEquals("center", firstPar.style.alignment)
        assertEquals(2, firstPar.runs.size)
        assertEquals("Hello ", firstPar.runs[0].text)
        assertEquals(true, firstPar.runs[0].style.isBold)
        assertEquals("world!", firstPar.runs[1].text)
        assertEquals(false, firstPar.runs[1].style.isBold)
    }
}
