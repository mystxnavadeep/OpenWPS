#pragma once
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
