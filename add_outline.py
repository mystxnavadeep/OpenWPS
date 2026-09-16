import re

with open("native/jni/src/main/cpp/core/document/Document.h", "r") as f:
    content = f.read()

if "std::string getOutlineJson() const;" not in content:
    content = content.replace("std::string toJson() const {", """std::string getOutlineJson() const {
        std::ostringstream ss;
        ss << "[";
        bool first = true;
        for (const auto& sec : sections_) {
            for (const auto& blk : sec->blocks()) {
                if (blk->paragraph() && blk->paragraph()->style().headingLevel.has_value()) {
                    if (!first) ss << ",";
                    first = false;
                    ss << "{";
                    ss << "\\"id\\":\\"" << JsonBuilder::escape(blk->paragraph()->id().id()) << "\\",";
                    ss << "\\"level\\":" << blk->paragraph()->style().headingLevel.value() << ",";
                    ss << "\\"text\\":\\"" << JsonBuilder::escape(blk->paragraph()->text()) << "\\"";
                    ss << "}";
                }
            }
        }
        ss << "]";
        return ss.str();
    }

    std::string toJson() const {""")
    with open("native/jni/src/main/cpp/core/document/Document.h", "w") as f:
        f.write(content)
    print("Added getOutlineJson to Document.h")
