#include <jni.h>
#include <string>
#include "../core/api/DocumentSession.h"
#include "../core/document/Document.h"

using namespace openwps::core;

extern "C" JNIEXPORT jstring JNICALL
Java_com_openwps_ndk_jni_NativeBridge_getEngineVersion(
        JNIEnv* env,
        jobject /* this */) {
    std::string version = "OpenWPS Core Engine v0.3.0";
    return env->NewStringUTF(version.c_str());
}

extern "C" JNIEXPORT jlong JNICALL
Java_com_openwps_ndk_jni_NativeBridge_createSession(
        JNIEnv* env,
        jobject /* this */,
        jstring sessionIdStr) {
    const char *sessionId = env->GetStringUTFChars(sessionIdStr, nullptr);
    auto* session = new DocumentSession(std::string(sessionId));
    env->ReleaseStringUTFChars(sessionIdStr, sessionId);
    return reinterpret_cast<jlong>(session);
}

extern "C" JNIEXPORT void JNICALL
Java_com_openwps_ndk_jni_NativeBridge_destroySession(
        JNIEnv* env,
        jobject /* this */,
        jlong sessionPtr) {
    auto* session = reinterpret_cast<DocumentSession*>(sessionPtr);
    delete session;
}

extern "C" JNIEXPORT jboolean JNICALL
Java_com_openwps_ndk_jni_NativeBridge_insertText(
        JNIEnv* env,
        jobject /* this */,
        jlong sessionPtr,
        jstring textStr) {
    auto* session = reinterpret_cast<DocumentSession*>(sessionPtr);
    const char *text = env->GetStringUTFChars(textStr, nullptr);
    
    // Very simplified insertion for Phase 3 demo
    class SimpleInsertCommand : public Command {
    public:
        SimpleInsertCommand(std::string text) : text_(std::move(text)) {}
        CommandResult execute(Document& document) override {
            // Insert at end for simplicity
            document.insertText(document.getText().length(), text_);
            return CommandResult(true);
        }
    private:
        std::string text_;
    };
    
    SimpleInsertCommand cmd(text);
    CommandResult result = session->applyCommand(cmd);
    
    env->ReleaseStringUTFChars(textStr, text);
    return result.success();
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_openwps_ndk_jni_NativeBridge_getText(
        JNIEnv* env,
        jobject /* this */,
        jlong sessionPtr) {
    auto* session = reinterpret_cast<DocumentSession*>(sessionPtr);
    std::string text = session->getText();
    return env->NewStringUTF(text.c_str());
}
