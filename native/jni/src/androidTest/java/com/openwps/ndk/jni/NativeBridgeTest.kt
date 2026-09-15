package com.openwps.ndk.jni

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NativeBridgeTest {
    @Test
    fun testGetEngineVersion() {
        val version = NativeBridge.getEngineVersion()
        assertEquals("OpenWPS Core Engine v0.1.0", version)
    }
}
