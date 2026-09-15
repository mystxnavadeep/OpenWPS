package com.openwps.ndk.jni

object NativeBridge {
    init {
        System.loadLibrary("openwps-core")
    }

    external fun getEngineVersion(): String
    
    // Core Engine DocumentSession API
    external fun createSession(sessionId: String): Long
    external fun destroySession(sessionPtr: Long)
    external fun insertText(sessionPtr: Long, text: String): Boolean
    external fun getText(sessionPtr: Long): String
}
