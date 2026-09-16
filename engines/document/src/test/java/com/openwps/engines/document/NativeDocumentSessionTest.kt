package com.openwps.engines.document

import com.openwps.office.api.command.DocumentCommand
import com.openwps.office.model.DocumentObjectId
import com.openwps.office.model.DocumentRange
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class NativeDocumentSessionTest {
    
    @Test
    fun testCapabilityRegistry() {
        val registry = NativeCapabilityRegistry()
        assertTrue(registry.supports("document.text.read"))
        assertTrue(registry.supports("document.text.insert"))
        assertTrue(registry.supports("document.text.delete"))
        assertTrue(registry.supports("document.text.style"))
        assertTrue(registry.supports("document.structure.read"))
        assertEquals(false, registry.supports("document.paragraph.edit"))
    }
}
