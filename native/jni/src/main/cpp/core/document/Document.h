#pragma once
#include <string>

namespace openwps {
namespace core {

class Document {
public:
    Document() : version_(1) {}
    
    // Very basic internal representation for Phase 3
    std::string getText() const { return content_; }
    void insertText(int offset, const std::string& text) {
        if (offset <= content_.length()) {
            content_.insert(offset, text);
        }
    }
    
    int getVersion() const { return version_; }

private:
    std::string content_;
    int version_;
};

} // namespace core
} // namespace openwps
