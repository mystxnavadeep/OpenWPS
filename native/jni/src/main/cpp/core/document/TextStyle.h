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
    std::optional<std::string> textColorHex;
    
    bool operator==(const TextStyle& other) const {
        return fontFamily == other.fontFamily &&
               fontSize == other.fontSize &&
               isBold == other.isBold &&
               isItalic == other.isItalic &&
               isUnderline == other.isUnderline &&
               textColorHex == other.textColorHex;
    }
    bool operator!=(const TextStyle& other) const { return !(*this == other); }
    
    std::string toJson() const;
};

}
}
