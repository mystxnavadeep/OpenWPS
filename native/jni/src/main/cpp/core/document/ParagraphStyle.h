#pragma once
#include <string>
#include <optional>

namespace openwps {
namespace core {

struct ParagraphStyle {
    std::optional<std::string> alignment;
    
    std::string toJson() const;
};

}
}
