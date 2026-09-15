package com.openwps.engines.common

import com.openwps.core.common.result.Result

data class NativeEngineInfo(
    val version: String,
    val features: List<String>
)

interface NativeEngine {
    fun getInfo(): Result<NativeEngineInfo>
    fun initialize(): Result<Unit>
    fun shutdown(): Result<Unit>
}
