#pragma once
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
