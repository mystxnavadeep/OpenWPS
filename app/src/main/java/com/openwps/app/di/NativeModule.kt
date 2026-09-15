package com.openwps.app.di

import com.openwps.core.common.result.Result
import com.openwps.engines.common.NativeEngine
import com.openwps.engines.common.NativeEngineInfo
import com.openwps.ndk.jni.NativeBridge
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

class OpenWpsNativeEngine : NativeEngine {
    override fun getInfo(): Result<NativeEngineInfo> {
        return try {
            val version = NativeBridge.getEngineVersion()
            Result.Success(NativeEngineInfo(version, listOf("core")))
        } catch (e: Exception) {
            Result.Error(com.openwps.core.common.result.AppError.NativeEngineError("Failed to get native info", e))
        }
    }
    
    override fun initialize(): Result<Unit> = Result.Success(Unit)
    override fun shutdown(): Result<Unit> = Result.Success(Unit)
}

@Module
@InstallIn(SingletonComponent::class)
object NativeModule {

    @Provides
    @Singleton
    fun provideNativeEngine(): NativeEngine {
        return OpenWpsNativeEngine()
    }
}
