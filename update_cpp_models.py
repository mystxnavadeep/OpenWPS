import os

TEXTSTYLE_H = """#pragma once
#include <string>
#include <optional>

namespace openwps {
namespace core {

struct TextStyle {
    std::optional<std::string> fontFamily;
    std::optional<float> fontSize;
    std::optional<bool> isBold;
    std::optional<bool> isItalic;
    std::optional<bool> isUnderline;
    std::optional<bool> isStrikethrough;
    std::optional<bool> isSuperscript;
    std::optional<bool> isSubscript;
    std::optional<std::string> textColorHex;
    std::optional<std::string> highlightColorHex;
    
    bool operator==(const TextStyle& other) const {
        return fontFamily == other.fontFamily &&
               fontSize == other.fontSize &&
               isBold == other.isBold &&
               isItalic == other.isItalic &&
               isUnderline == other.isUnderline &&
               isStrikethrough == other.isStrikethrough &&
               isSuperscript == other.isSuperscript &&
               isSubscript == other.isSubscript &&
               textColorHex == other.textColorHex &&
               highlightColorHex == other.highlightColorHex;
    }
    bool operator!=(const TextStyle& other) const { return !(*this == other); }
    
    std::string toJson() const;
};

}
}
"""

PARAGRAPHSTYLE_H = """#pragma once
#include <string>
#include <optional>

namespace openwps {
namespace core {

struct ParagraphStyle {
    std::optional<std::string> alignment;
    std::optional<float> indentLeft;
    std::optional<float> indentRight;
    std::optional<float> indentFirstLine;
    std::optional<float> spacingBefore;
    std::optional<float> spacingAfter;
    std::optional<float> lineSpacing;
    std::optional<int> headingLevel;
    std::optional<bool> isList;
    std::optional<std::string> listId;
    std::optional<int> listLevel;
    
    std::string toJson() const;
};

}
}
"""

SECTIONPROPERTIES_H = """#pragma once
#include <string>
#include <optional>

namespace openwps {
namespace core {

struct PageSize {
    float width = 0.0f;
    float height = 0.0f;
};

struct Margins {
    float top = 0.0f;
    float bottom = 0.0f;
    float left = 0.0f;
    float right = 0.0f;
};

struct SectionProperties {
    std::optional<PageSize> pageSize;
    std::optional<std::string> orientation;
    std::optional<Margins> margins;

    std::string toJson() const;
};

}
}
"""

SECTION_H = """#pragma once
#include <string>
#include <vector>
#include <memory>
#include <optional>
#include "DocumentObjectId.h"
#include "Block.h"
#include "SectionProperties.h"

namespace openwps {
namespace core {

class Section {
public:
    Section(DocumentObjectId id, SectionProperties props = SectionProperties()) 
        : id_(std::move(id)), props_(std::move(props)) {}
    
    const DocumentObjectId& id() const { return id_; }
    const SectionProperties& properties() const { return props_; }
    void setProperties(const SectionProperties& props) { props_ = props; }

    const std::vector<std::unique_ptr<Block>>& blocks() const { return blocks_; }
    
    void addBlock(std::unique_ptr<Block> block) {
        blocks_.push_back(std::move(block));
    }
    
    std::string toJson() const;
    
private:
    DocumentObjectId id_;
    SectionProperties props_;
    std::vector<std::unique_ptr<Block>> blocks_;
};

}
}
"""

with open("native/jni/src/main/cpp/core/document/TextStyle.h", "w") as f:
    f.write(TEXTSTYLE_H)
with open("native/jni/src/main/cpp/core/document/ParagraphStyle.h", "w") as f:
    f.write(PARAGRAPHSTYLE_H)
with open("native/jni/src/main/cpp/core/document/SectionProperties.h", "w") as f:
    f.write(SECTIONPROPERTIES_H)
with open("native/jni/src/main/cpp/core/document/Section.h", "w") as f:
    f.write(SECTION_H)

print("Headers updated successfully!")
