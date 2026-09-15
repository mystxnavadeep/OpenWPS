package com.openwps.engines.document

import com.openwps.office.api.command.DocumentCommand
import com.openwps.office.model.DocumentObjectId
import com.openwps.office.model.DocumentRange
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.UUID

class NativeDocumentSessionTest {
    // Note: Since this tests a class that loads a native library (JNI),
    // it will fail in standard local JVM tests unless the native lib is built and linked for the host OS.
    // However, the test acts as a structured contract for CI if instrumented or appropriately mocked.
    
    @Test
    fun testCapabilityRegistry() {
        val registry = NativeCapabilityRegistry()
        assertTrue(registry.supports("document.text.read"))
        assertTrue(registry.supports("document.text.insert"))
        assertEquals(false, registry.supports("document.text.delete"))
    }
}
