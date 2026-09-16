import re
with open("native/jni/src/main/java/com/openwps/ndk/jni/NativeBridge.kt", "r") as f:
    content = f.read()

new_methods = """    external fun applyTextStyle(sessionPtr: Long, startId: String, startOffset: Int, endId: String, endOffset: Int, styleJson: String): String
    external fun applyParagraphStyle(sessionPtr: Long, targetId: String, styleJson: String): String
    external fun setSectionProperties(sessionPtr: Long, targetId: String, propsJson: String): String
    external fun getDocumentOutline(sessionPtr: Long): String"""

content = content.replace("    external fun applyTextStyle(sessionPtr: Long, startId: String, startOffset: Int, endId: String, endOffset: Int, styleJson: String): String", new_methods)

with open("native/jni/src/main/java/com/openwps/ndk/jni/NativeBridge.kt", "w") as f:
    f.write(content)
