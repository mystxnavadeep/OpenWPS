#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_openwps_ndk_jni_NativeBridge_getEngineVersion(
        JNIEnv* env,
        jobject /* this */) {
    std::string version = "OpenWPS Core Engine v0.1.0";
    return env->NewStringUTF(version.c_str());
}
