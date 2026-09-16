#include <jni.h>
#include <string>
#include <sstream>
#include <algorithm>
#include "../core/api/DocumentSession.h"
#include "../core/document/Document.h"
#include "../core/document/DocumentRange.h"

using namespace openwps::core;

extern "C" JNIEXPORT jstring JNICALL
Java_com_openwps_ndk_jni_NativeBridge_getEngineVersion(
        JNIEnv* env,
        jobject /* this */) {
    std::string version = "OpenWPS Core Engine v0.4.0 (Precision Text)";
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

extern "C" JNIEXPORT jstring JNICALL
Java_com_openwps_ndk_jni_NativeBridge_resolveWord(
        JNIEnv* env, jobject, jlong sessionPtr,
        jstring paragraphIdStr, jint wordIndex) {
    auto* session = reinterpret_cast<DocumentSession*>(sessionPtr);
    const char* pId = env->GetStringUTFChars(paragraphIdStr, nullptr);
    std::string paragraphId(pId);
    env->ReleaseStringUTFChars(paragraphIdStr, pId);
    
    Paragraph* p = session->document().findParagraph(paragraphId);
    std::ostringstream ss;
    if (!p) {
        ss << "{\"success\":false,\"errorCode\":\"INVALID_OBJECT_ID\"}";
    } else {
        int start = 0, end = 0;
        if (p->getWordRange(wordIndex, start, end)) {
            DocumentRange range{DocumentObjectId(paragraphId), start, DocumentObjectId(paragraphId), end};
            ss << "{\"success\":true,\"errorCode\":\"NONE\",\"affectedRange\":" << range.toJson() << "}";
        } else {
            ss << "{\"success\":false,\"errorCode\":\"INVALID_WORD_INDEX\"}";
        }
    }
    return env->NewStringUTF(ss.str().c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_openwps_ndk_jni_NativeBridge_resolveSentence(
        JNIEnv* env, jobject, jlong sessionPtr,
        jstring paragraphIdStr, jint sentenceIndex) {
    auto* session = reinterpret_cast<DocumentSession*>(sessionPtr);
    const char* pId = env->GetStringUTFChars(paragraphIdStr, nullptr);
    std::string paragraphId(pId);
    env->ReleaseStringUTFChars(paragraphIdStr, pId);
    
    Paragraph* p = session->document().findParagraph(paragraphId);
    std::ostringstream ss;
    if (!p) {
        ss << "{\"success\":false,\"errorCode\":\"INVALID_OBJECT_ID\"}";
    } else {
        int start = 0, end = 0;
        if (p->getSentenceRange(sentenceIndex, start, end)) {
            DocumentRange range{DocumentObjectId(paragraphId), start, DocumentObjectId(paragraphId), end};
            ss << "{\"success\":true,\"errorCode\":\"NONE\",\"affectedRange\":" << range.toJson() << "}";
        } else {
            ss << "{\"success\":false,\"errorCode\":\"INVALID_SENTENCE_INDEX\"}";
        }
    }
    return env->NewStringUTF(ss.str().c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_openwps_ndk_jni_NativeBridge_search(
        JNIEnv* env, jobject, jlong sessionPtr,
        jstring queryStr, jboolean matchCase) {
    auto* session = reinterpret_cast<DocumentSession*>(sessionPtr);
    const char* q = env->GetStringUTFChars(queryStr, nullptr);
    std::string query(q);
    env->ReleaseStringUTFChars(queryStr, q);
    
    std::string lowerQuery = query;
    if (!matchCase) {
        std::transform(lowerQuery.begin(), lowerQuery.end(), lowerQuery.begin(), ::tolower);
    }
    
    std::ostringstream ss;
    ss << "[";
    bool first = true;
    for (const auto& sec : session->document().sections()) {
        for (const auto& blk : sec->blocks()) {
            if (blk->paragraph()) {
                std::string pText = blk->paragraph()->text();
                std::string searchTarget = pText;
                if (!matchCase) {
                    std::transform(searchTarget.begin(), searchTarget.end(), searchTarget.begin(), ::tolower);
                }
                size_t pos = searchTarget.find(lowerQuery, 0);
                while (pos != std::string::npos) {
                    if (!first) ss << ",";
                    DocumentRange r{blk->paragraph()->id(), (int)pos, blk->paragraph()->id(), (int)(pos + query.length())};
                    ss << r.toJson();
                    first = false;
                    pos = searchTarget.find(lowerQuery, pos + query.length());
                }
            }
        }
    }
    ss << "]";
    return env->NewStringUTF(ss.str().c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_openwps_ndk_jni_NativeBridge_insertText(
        JNIEnv* env, jobject, jlong sessionPtr,
        jstring objectIdStr, jint offset, jstring textStr, jstring styleJsonStr) {
    auto* session = reinterpret_cast<DocumentSession*>(sessionPtr);
    const char* oId = env->GetStringUTFChars(objectIdStr, nullptr);
    const char* txt = env->GetStringUTFChars(textStr, nullptr);
    std::string objectId(oId);
    std::string text(txt);
    env->ReleaseStringUTFChars(objectIdStr, oId);
    env->ReleaseStringUTFChars(textStr, txt);
    
    std::optional<TextStyle> styleOpt = std::nullopt;
    if (styleJsonStr) {
        // very basic manual parsing of the provided subset
        const char* sJson = env->GetStringUTFChars(styleJsonStr, nullptr);
        std::string json(sJson);
        env->ReleaseStringUTFChars(styleJsonStr, sJson);
        
        TextStyle s;
    if (json.find("\"isBold\":true") != std::string::npos) s.isBold = true;
    else if (json.find("\"isBold\":false") != std::string::npos) s.isBold = false;
    if (json.find("\"isItalic\":true") != std::string::npos) s.isItalic = true;
    else if (json.find("\"isItalic\":false") != std::string::npos) s.isItalic = false;
    if (json.find("\"isUnderline\":true") != std::string::npos) s.isUnderline = true;
    else if (json.find("\"isUnderline\":false") != std::string::npos) s.isUnderline = false;
    if (json.find("\"isStrikethrough\":true") != std::string::npos) s.isStrikethrough = true;
    else if (json.find("\"isStrikethrough\":false") != std::string::npos) s.isStrikethrough = false;
    if (json.find("\"isSuperscript\":true") != std::string::npos) s.isSuperscript = true;
    else if (json.find("\"isSuperscript\":false") != std::string::npos) s.isSuperscript = false;
    if (json.find("\"isSubscript\":true") != std::string::npos) s.isSubscript = true;
    else if (json.find("\"isSubscript\":false") != std::string::npos) s.isSubscript = false;
        // color parsing left as an exercise for production
        styleOpt = s;
    }
    
    Paragraph* p = session->document().findParagraph(objectId);
    std::ostringstream ss;
    if (!p) {
        ss << "{\"success\":false,\"errorCode\":\"INVALID_OBJECT_ID\"}";
    } else {
        p->insertText(offset, text, styleOpt);
        session->document().incrementVersion();
        DocumentRange range{DocumentObjectId(objectId), offset, DocumentObjectId(objectId), (int)(offset + text.length())};
        ss << "{\"success\":true,\"errorCode\":\"NONE\",\"affectedRange\":" << range.toJson() << "}";
    }
    return env->NewStringUTF(ss.str().c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_openwps_ndk_jni_NativeBridge_deleteRange(
        JNIEnv* env, jobject, jlong sessionPtr,
        jstring startIdStr, jint startOffset, jstring endIdStr, jint endOffset) {
    auto* session = reinterpret_cast<DocumentSession*>(sessionPtr);
    const char* sId = env->GetStringUTFChars(startIdStr, nullptr);
    const char* eId = env->GetStringUTFChars(endIdStr, nullptr);
    std::string startId(sId);
    std::string endId(eId);
    env->ReleaseStringUTFChars(startIdStr, sId);
    env->ReleaseStringUTFChars(endIdStr, eId);
    
    std::ostringstream ss;
    if (startId != endId) {
        ss << "{\"success\":false,\"errorCode\":\"UNSUPPORTED_OPERATION\",\"errorMessage\":\"Cross-paragraph deletion not supported yet\"}";
    } else {
        Paragraph* p = session->document().findParagraph(startId);
        if (!p) {
            ss << "{\"success\":false,\"errorCode\":\"INVALID_OBJECT_ID\"}";
        } else {
            p->deleteText(startOffset, endOffset);
            session->document().incrementVersion();
            DocumentRange range{DocumentObjectId(startId), startOffset, DocumentObjectId(startId), startOffset};
            ss << "{\"success\":true,\"errorCode\":\"NONE\",\"affectedRange\":" << range.toJson() << "}";
        }
    }
    return env->NewStringUTF(ss.str().c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_openwps_ndk_jni_NativeBridge_applyTextStyle(
        JNIEnv* env, jobject, jlong sessionPtr,
        jstring startIdStr, jint startOffset, jstring endIdStr, jint endOffset, jstring styleJsonStr) {
    auto* session = reinterpret_cast<DocumentSession*>(sessionPtr);
    const char* sId = env->GetStringUTFChars(startIdStr, nullptr);
    const char* eId = env->GetStringUTFChars(endIdStr, nullptr);
    std::string startId(sId);
    std::string endId(eId);
    env->ReleaseStringUTFChars(startIdStr, sId);
    env->ReleaseStringUTFChars(endIdStr, eId);
    
    const char* sJson = env->GetStringUTFChars(styleJsonStr, nullptr);
    std::string json(sJson);
    env->ReleaseStringUTFChars(styleJsonStr, sJson);
    
    TextStyle s;
    if (json.find("\"isBold\":true") != std::string::npos) s.isBold = true;
    else if (json.find("\"isBold\":false") != std::string::npos) s.isBold = false;
    if (json.find("\"isItalic\":true") != std::string::npos) s.isItalic = true;
    else if (json.find("\"isItalic\":false") != std::string::npos) s.isItalic = false;
    if (json.find("\"isUnderline\":true") != std::string::npos) s.isUnderline = true;
    else if (json.find("\"isUnderline\":false") != std::string::npos) s.isUnderline = false;
    if (json.find("\"isStrikethrough\":true") != std::string::npos) s.isStrikethrough = true;
    else if (json.find("\"isStrikethrough\":false") != std::string::npos) s.isStrikethrough = false;
    if (json.find("\"isSuperscript\":true") != std::string::npos) s.isSuperscript = true;
    else if (json.find("\"isSuperscript\":false") != std::string::npos) s.isSuperscript = false;
    if (json.find("\"isSubscript\":true") != std::string::npos) s.isSubscript = true;
    else if (json.find("\"isSubscript\":false") != std::string::npos) s.isSubscript = false;

    std::ostringstream ss;
    if (startId != endId) {
        ss << "{\"success\":false,\"errorCode\":\"UNSUPPORTED_OPERATION\",\"errorMessage\":\"Cross-paragraph styling not supported yet\"}";
    } else {
        Paragraph* p = session->document().findParagraph(startId);
        if (!p) {
            ss << "{\"success\":false,\"errorCode\":\"INVALID_OBJECT_ID\"}";
        } else {
            p->applyStyle(startOffset, endOffset, s);
            session->document().incrementVersion();
            DocumentRange range{DocumentObjectId(startId), startOffset, DocumentObjectId(startId), endOffset};
            ss << "{\"success\":true,\"errorCode\":\"NONE\",\"affectedRange\":" << range.toJson() << "}";
        }
    }
    return env->NewStringUTF(ss.str().c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_openwps_ndk_jni_NativeBridge_getTextRange(
        JNIEnv* env, jobject, jlong sessionPtr,
        jstring startIdStr, jint startOffset, jstring endIdStr, jint endOffset) {
    auto* session = reinterpret_cast<DocumentSession*>(sessionPtr);
    const char* sId = env->GetStringUTFChars(startIdStr, nullptr);
    const char* eId = env->GetStringUTFChars(endIdStr, nullptr);
    std::string startId(sId);
    std::string endId(eId);
    env->ReleaseStringUTFChars(startIdStr, sId);
    env->ReleaseStringUTFChars(endIdStr, eId);
    
    if (startId != endId) {
        // very basic
        return env->NewStringUTF(session->document().getText().c_str());
    } else {
        Paragraph* p = session->document().findParagraph(startId);
        if (!p) return env->NewStringUTF("");
        std::string txt = p->text();
        if (startOffset < 0) startOffset = 0;
        if (endOffset > txt.length()) endOffset = txt.length();
        if (startOffset >= endOffset) return env->NewStringUTF("");
        return env->NewStringUTF(txt.substr(startOffset, endOffset - startOffset).c_str());
    }
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_openwps_ndk_jni_NativeBridge_getDocumentStructure(
        JNIEnv* env, jobject, jlong sessionPtr) {
    auto* session = reinterpret_cast<DocumentSession*>(sessionPtr);
    return env->NewStringUTF(session->document().toJson().c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_openwps_ndk_jni_NativeBridge_applyParagraphStyle(
        JNIEnv* env, jobject, jlong sessionPtr,
        jstring targetIdStr, jstring styleJsonStr) {
    auto* session = reinterpret_cast<DocumentSession*>(sessionPtr);
    const char* tId = env->GetStringUTFChars(targetIdStr, nullptr);
    std::string targetId(tId);
    env->ReleaseStringUTFChars(targetIdStr, tId);
    
    const char* sJson = env->GetStringUTFChars(styleJsonStr, nullptr);
    std::string json(sJson);
    env->ReleaseStringUTFChars(styleJsonStr, sJson);
    
    Paragraph* p = session->document().findParagraph(targetId);
    std::ostringstream ss;
    if (!p) {
        ss << "{\"success\":false,\"errorCode\":\"INVALID_OBJECT_ID\"}";
    } else {
        ParagraphStyle ps = p->style(); // copy existing
        if (json.find("\"alignment\":\"left\"") != std::string::npos) ps.alignment = "left";
        if (json.find("\"alignment\":\"center\"") != std::string::npos) ps.alignment = "center";
        if (json.find("\"alignment\":\"right\"") != std::string::npos) ps.alignment = "right";
        if (json.find("\"alignment\":\"justified\"") != std::string::npos) ps.alignment = "justified";
        if (json.find("\"isList\":true") != std::string::npos) ps.isList = true;
        if (json.find("\"isList\":false") != std::string::npos) ps.isList = false;
        
        // simple parsing for headingLevel
        size_t hlPos = json.find("\"headingLevel\":");
        if (hlPos != std::string::npos) {
            int level = json[hlPos + 15] - '0';
            if (level >= 1 && level <= 6) ps.headingLevel = level;
        }
        
        // Very basic approach. We can override p's style by accessing the non-const method if we need to.
        // Wait, Paragraph doesn't have a setStyle(). We need to add one.
        // I will add it via sed in the bash script later.
        p->setStyle(ps);
        
        session->document().incrementVersion();
        DocumentRange range{DocumentObjectId(targetId), 0, DocumentObjectId(targetId), (int)p->text().length()};
        ss << "{\"success\":true,\"errorCode\":\"NONE\",\"affectedRange\":" << range.toJson() << "}";
    }
    return env->NewStringUTF(ss.str().c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_openwps_ndk_jni_NativeBridge_setSectionProperties(
        JNIEnv* env, jobject, jlong sessionPtr,
        jstring targetIdStr, jstring propsJsonStr) {
    auto* session = reinterpret_cast<DocumentSession*>(sessionPtr);
    const char* tId = env->GetStringUTFChars(targetIdStr, nullptr);
    std::string targetId(tId);
    env->ReleaseStringUTFChars(targetIdStr, tId);
    
    const char* sJson = env->GetStringUTFChars(propsJsonStr, nullptr);
    std::string json(sJson);
    env->ReleaseStringUTFChars(propsJsonStr, sJson);
    
    std::ostringstream ss;
    bool found = false;
    for(auto& sec : session->document().sections()) {
        if(sec->id().id() == targetId) {
            SectionProperties sp = sec->properties();
            if (json.find("\"orientation\":\"PORTRAIT\"") != std::string::npos) sp.orientation = "PORTRAIT";
            if (json.find("\"orientation\":\"LANDSCAPE\"") != std::string::npos) sp.orientation = "LANDSCAPE";
            sec->setProperties(sp);
            found = true;
            break;
        }
    }
    
    if (!found) {
        ss << "{\"success\":false,\"errorCode\":\"INVALID_OBJECT_ID\"}";
    } else {
        session->document().incrementVersion();
        ss << "{\"success\":true,\"errorCode\":\"NONE\"}";
    }
    return env->NewStringUTF(ss.str().c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_openwps_ndk_jni_NativeBridge_getDocumentOutline(
        JNIEnv* env, jobject, jlong sessionPtr) {
    auto* session = reinterpret_cast<DocumentSession*>(sessionPtr);
    return env->NewStringUTF(session->document().getOutlineJson().c_str());
}
