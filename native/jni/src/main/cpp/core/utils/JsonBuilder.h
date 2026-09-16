#pragma once
#include <string>
#include <vector>
#include <sstream>

namespace openwps {
namespace core {

class JsonBuilder {
public:
    static std::string escape(const std::string& str) {
        std::ostringstream ss;
        for (char c : str) {
            if (c == '"') ss << "\\\"";
            else if (c == '\\') ss << "\\\\";
            else if (c == '\b') ss << "\\b";
            else if (c == '\f') ss << "\\f";
            else if (c == '\n') ss << "\\n";
            else if (c == '\r') ss << "\\r";
            else if (c == '\t') ss << "\\t";
            else ss << c;
        }
        return ss.str();
    }
};

}
}
