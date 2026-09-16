import re

with open("native/jni/src/main/cpp/core/document/DocumentStructure.cpp", "r") as f:
    content = f.read()

# 1. Update TextStyle::toJson()
new_textstyle_tojson = """std::string TextStyle::toJson() const {
    std::ostringstream ss;
    ss << "{";
    bool first = true;
    auto addComma = [&]() { if (!first) ss << ","; first = false; };
    
    if (fontFamily) { addComma(); ss << "\\"fontFamily\\":\\"" << JsonBuilder::escape(*fontFamily) << "\\""; }
    if (fontSize) { addComma(); ss << "\\"fontSize\\":" << *fontSize; }
    if (isBold) { addComma(); ss << "\\"isBold\\":" << (*isBold ? "true" : "false"); }
    if (isItalic) { addComma(); ss << "\\"isItalic\\":" << (*isItalic ? "true" : "false"); }
    if (isUnderline) { addComma(); ss << "\\"isUnderline\\":" << (*isUnderline ? "true" : "false"); }
    if (isStrikethrough) { addComma(); ss << "\\"isStrikethrough\\":" << (*isStrikethrough ? "true" : "false"); }
    if (isSuperscript) { addComma(); ss << "\\"isSuperscript\\":" << (*isSuperscript ? "true" : "false"); }
    if (isSubscript) { addComma(); ss << "\\"isSubscript\\":" << (*isSubscript ? "true" : "false"); }
    if (textColorHex) { addComma(); ss << "\\"textColorHex\\":\\"" << JsonBuilder::escape(*textColorHex) << "\\""; }
    if (highlightColorHex) { addComma(); ss << "\\"highlightColorHex\\":\\"" << JsonBuilder::escape(*highlightColorHex) << "\\""; }
    ss << "}";
    return ss.str();
}"""

content = re.sub(r'std::string TextStyle::toJson\(\) const \{.*?return ss\.str\(\);\n\}', new_textstyle_tojson, content, flags=re.DOTALL)

# 2. Update ParagraphStyle::toJson()
new_parastyle_tojson = """std::string ParagraphStyle::toJson() const {
    std::ostringstream ss;
    ss << "{";
    bool first = true;
    auto addComma = [&]() { if (!first) ss << ","; first = false; };
    
    if (alignment) { addComma(); ss << "\\"alignment\\":\\"" << JsonBuilder::escape(*alignment) << "\\""; }
    if (indentLeft) { addComma(); ss << "\\"indentLeft\\":" << *indentLeft; }
    if (indentRight) { addComma(); ss << "\\"indentRight\\":" << *indentRight; }
    if (indentFirstLine) { addComma(); ss << "\\"indentFirstLine\\":" << *indentFirstLine; }
    if (spacingBefore) { addComma(); ss << "\\"spacingBefore\\":" << *spacingBefore; }
    if (spacingAfter) { addComma(); ss << "\\"spacingAfter\\":" << *spacingAfter; }
    if (lineSpacing) { addComma(); ss << "\\"lineSpacing\\":" << *lineSpacing; }
    if (headingLevel) { addComma(); ss << "\\"headingLevel\\":" << *headingLevel; }
    if (isList) { addComma(); ss << "\\"isList\\":" << (*isList ? "true" : "false"); }
    if (listId) { addComma(); ss << "\\"listId\\":\\"" << JsonBuilder::escape(*listId) << "\\""; }
    if (listLevel) { addComma(); ss << "\\"listLevel\\":" << *listLevel; }
    
    ss << "}";
    return ss.str();
}"""

content = re.sub(r'std::string ParagraphStyle::toJson\(\) const \{.*?return ss\.str\(\);\n\}', new_parastyle_tojson, content, flags=re.DOTALL)

# 3. Add SectionProperties::toJson() and Update Section::toJson()
new_sectionprops_tojson = """std::string SectionProperties::toJson() const {
    std::ostringstream ss;
    ss << "{";
    bool first = true;
    auto addComma = [&]() { if (!first) ss << ","; first = false; };
    
    if (pageSize) {
        addComma();
        ss << "\\"pageSize\\":{\\"width\\":" << pageSize->width << ",\\"height\\":" << pageSize->height << "}";
    }
    if (orientation) {
        addComma();
        ss << "\\"orientation\\":\\"" << JsonBuilder::escape(*orientation) << "\\"";
    }
    if (margins) {
        addComma();
        ss << "\\"margins\\":{\\"top\\":" << margins->top << ",\\"bottom\\":" << margins->bottom << ",\\"left\\":" << margins->left << ",\\"right\\":" << margins->right << "}";
    }
    
    ss << "}";
    return ss.str();
}

std::string Section::toJson() const {
    std::ostringstream ss;
    ss << "{";
    ss << "\\"id\\":\\"" << JsonBuilder::escape(id_.id()) << "\\",";
    ss << "\\"properties\\":" << props_.toJson() << ",";
    ss << "\\"blocks\\":[";
    for (size_t i = 0; i < blocks_.size(); ++i) {
        if (i > 0) ss << ",";
        ss << blocks_[i]->toJson();
    }
    ss << "]";
    ss << "}";
    return ss.str();
}"""

content = re.sub(r'std::string Section::toJson\(\) const \{.*?return ss\.str\(\);\n\}', new_sectionprops_tojson, content, flags=re.DOTALL)

with open("native/jni/src/main/cpp/core/document/DocumentStructure.cpp", "w") as f:
    f.write(content)

print("DocumentStructure.cpp updated!")
