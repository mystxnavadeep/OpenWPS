package com.openwps.ndk.jni

object NativeBridge {
    init {
        System.loadLibrary("openwps-core")
    }

    external fun getEngineVersion(): String
}
