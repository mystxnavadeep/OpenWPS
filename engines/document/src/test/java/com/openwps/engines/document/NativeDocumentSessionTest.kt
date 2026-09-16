package com.openwps.engines.document

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class NativeDocumentSessionTest {
    
    @Test
    fun testCapabilityRegistry() {
        val registry = NativeCapabilityRegistry()
        assertTrue(registry.supports("document.text.character.read"))
        assertTrue(registry.supports("document.text.word.resolve"))
        assertTrue(registry.supports("document.text.sentence.resolve"))
        assertTrue(registry.supports("document.text.search"))
        assertTrue(registry.supports("document.text.style"))
        assertEquals(false, registry.supports("document.text.replace"))
    }
}
