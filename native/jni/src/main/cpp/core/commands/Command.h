#pragma once
#include "../document/Document.h"

namespace openwps {
namespace core {

class CommandResult {
public:
    CommandResult(bool success, std::string errorMsg = "")
        : success_(success), errorMessage_(std::move(errorMsg)) {}
        
    bool success() const { return success_; }
    const std::string& errorMessage() const { return errorMessage_; }
    
private:
    bool success_;
    std::string errorMessage_;
};

class Command {
public:
    virtual ~Command() = default;
    virtual CommandResult execute(Document& document) = 0;
};

} // namespace core
} // namespace openwps
