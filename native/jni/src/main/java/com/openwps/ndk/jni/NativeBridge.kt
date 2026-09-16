package com.openwps.ndk.jni

object NativeBridge {
    init {
        System.loadLibrary("openwps-core")
    }

    external fun getEngineVersion(): String
    
    // Core Engine DocumentSession API
    external fun createSession(sessionId: String): Long
    external fun destroySession(sessionPtr: Long)
    
    // Precision Text & Range Resolution
    external fun resolveWord(sessionPtr: Long, paragraphId: String, wordIndex: Int): String
    external fun resolveSentence(sessionPtr: Long, paragraphId: String, sentenceIndex: Int): String
    external fun search(sessionPtr: Long, query: String, matchCase: Boolean): String
    
    // Mutations (returning OperationResult JSON)
    external fun insertText(sessionPtr: Long, objectId: String, offset: Int, text: String, styleJson: String?): String
    external fun deleteRange(sessionPtr: Long, startId: String, startOffset: Int, endId: String, endOffset: Int): String
    external fun applyTextStyle(sessionPtr: Long, startId: String, startOffset: Int, endId: String, endOffset: Int, styleJson: String): String
    
    // Reads
    external fun getTextRange(sessionPtr: Long, startId: String, startOffset: Int, endId: String, endOffset: Int): String
    external fun getDocumentStructure(sessionPtr: Long): String
}
