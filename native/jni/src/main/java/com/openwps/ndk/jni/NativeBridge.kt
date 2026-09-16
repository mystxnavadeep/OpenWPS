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
    external fun deleteRange(sessionPtr: Long, startId: String, startOffset: Int, endId: String, endOffset: Int): Boolean
    external fun applyTextStyle(sessionPtr: Long, startId: String, startOffset: Int, endId: String, endOffset: Int, styleJson: String): Boolean
    external fun getText(sessionPtr: Long): String
    external fun getDocumentStructure(sessionPtr: Long): String
}
