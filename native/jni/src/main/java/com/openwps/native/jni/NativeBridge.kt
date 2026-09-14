package com.openwps.native.jni

object NativeBridge {
    init {
        System.loadLibrary("openwps-core")
    }

    external fun getEngineVersion(): String
}
