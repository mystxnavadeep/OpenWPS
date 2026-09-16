import re

with open("native/jni/src/main/cpp/jni/jni_bridge.cpp", "r") as f:
    content = f.read()

# 1. Expand TextStyle parser inside applyTextStyle & insertText
style_parser = """TextStyle s;
    if (json.find("\\"isBold\\":true") != std::string::npos) s.isBold = true;
    else if (json.find("\\"isBold\\":false") != std::string::npos) s.isBold = false;
    if (json.find("\\"isItalic\\":true") != std::string::npos) s.isItalic = true;
    else if (json.find("\\"isItalic\\":false") != std::string::npos) s.isItalic = false;
    if (json.find("\\"isUnderline\\":true") != std::string::npos) s.isUnderline = true;
    else if (json.find("\\"isUnderline\\":false") != std::string::npos) s.isUnderline = false;
    if (json.find("\\"isStrikethrough\\":true") != std::string::npos) s.isStrikethrough = true;
    else if (json.find("\\"isStrikethrough\\":false") != std::string::npos) s.isStrikethrough = false;
    if (json.find("\\"isSuperscript\\":true") != std::string::npos) s.isSuperscript = true;
    else if (json.find("\\"isSuperscript\\":false") != std::string::npos) s.isSuperscript = false;
    if (json.find("\\"isSubscript\\":true") != std::string::npos) s.isSubscript = true;
    else if (json.find("\\"isSubscript\\":false") != std::string::npos) s.isSubscript = false;"""

content = re.sub(r'TextStyle s;\n.*?isUnderline = false;', style_parser, content, flags=re.DOTALL)

# 2. Add ApplyParagraphStyle function
apply_para_style = """extern "C" JNIEXPORT jstring JNICALL
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
        ss << "{\\"success\\":false,\\"errorCode\\":\\"INVALID_OBJECT_ID\\"}";
    } else {
        ParagraphStyle ps = p->style(); // copy existing
        if (json.find("\\"alignment\\":\\"left\\"") != std::string::npos) ps.alignment = "left";
        if (json.find("\\"alignment\\":\\"center\\"") != std::string::npos) ps.alignment = "center";
        if (json.find("\\"alignment\\":\\"right\\"") != std::string::npos) ps.alignment = "right";
        if (json.find("\\"alignment\\":\\"justified\\"") != std::string::npos) ps.alignment = "justified";
        if (json.find("\\"isList\\":true") != std::string::npos) ps.isList = true;
        if (json.find("\\"isList\\":false") != std::string::npos) ps.isList = false;
        
        // simple parsing for headingLevel
        size_t hlPos = json.find("\\"headingLevel\\":");
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
        ss << "{\\"success\\":true,\\"errorCode\\":\\"NONE\\",\\"affectedRange\\":" << range.toJson() << "}";
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
            if (json.find("\\"orientation\\":\\"PORTRAIT\\"") != std::string::npos) sp.orientation = "PORTRAIT";
            if (json.find("\\"orientation\\":\\"LANDSCAPE\\"") != std::string::npos) sp.orientation = "LANDSCAPE";
            sec->setProperties(sp);
            found = true;
            break;
        }
    }
    
    if (!found) {
        ss << "{\\"success\\":false,\\"errorCode\\":\\"INVALID_OBJECT_ID\\"}";
    } else {
        session->document().incrementVersion();
        ss << "{\\"success\\":true,\\"errorCode\\":\\"NONE\\"}";
    }
    return env->NewStringUTF(ss.str().c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_openwps_ndk_jni_NativeBridge_getDocumentOutline(
        JNIEnv* env, jobject, jlong sessionPtr) {
    auto* session = reinterpret_cast<DocumentSession*>(sessionPtr);
    return env->NewStringUTF(session->document().getOutlineJson().c_str());
}
"""

content += "\n" + apply_para_style

with open("native/jni/src/main/cpp/jni/jni_bridge.cpp", "w") as f:
    f.write(content)

print("jni_bridge.cpp updated!")
