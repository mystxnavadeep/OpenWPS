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
            document.insertTextSimple(text_);
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

extern "C" JNIEXPORT jstring JNICALL
Java_com_openwps_ndk_jni_NativeBridge_getDocumentStructure(
        JNIEnv* env,
        jobject /* this */,
        jlong sessionPtr) {
    auto* session = reinterpret_cast<DocumentSession*>(sessionPtr);
    return env->NewStringUTF(session->document().toJson().c_str());
}

extern "C" JNIEXPORT jboolean JNICALL
Java_com_openwps_ndk_jni_NativeBridge_deleteRange(
        JNIEnv* env,
        jobject /* this */,
        jlong sessionPtr,
        jstring startIdStr, jint startOffset,
        jstring endIdStr, jint endOffset) {
    auto* session = reinterpret_cast<DocumentSession*>(sessionPtr);
    
    // In a full implementation, this parses the IDs and creates a DeleteRangeCommand.
    // For now, we simulate success to prove the API boundary.
    class DummyDeleteCommand : public Command {
    public:
        CommandResult execute(Document& document) override {
            document.incrementVersion(); // simulate mutation
            return CommandResult(true);
        }
    };
    
    DummyDeleteCommand cmd;
    return session->applyCommand(cmd).success();
}

extern "C" JNIEXPORT jboolean JNICALL
Java_com_openwps_ndk_jni_NativeBridge_applyTextStyle(
        JNIEnv* env,
        jobject /* this */,
        jlong sessionPtr,
        jstring startIdStr, jint startOffset,
        jstring endIdStr, jint endOffset,
        jstring styleJsonStr) {
    auto* session = reinterpret_cast<DocumentSession*>(sessionPtr);
    
    // In a full implementation, this applies the style and normalizes runs.
    class DummyStyleCommand : public Command {
    public:
        CommandResult execute(Document& document) override {
            document.incrementVersion(); // simulate mutation
            return CommandResult(true);
        }
    };
    
    DummyStyleCommand cmd;
    return session->applyCommand(cmd).success();
}
