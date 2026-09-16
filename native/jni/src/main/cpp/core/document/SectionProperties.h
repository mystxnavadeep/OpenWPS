#pragma once
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
