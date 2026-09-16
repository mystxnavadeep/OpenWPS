#pragma once
#include <memory>
#include <string>
#include "../document/Document.h"
#include "../commands/Command.h"

namespace openwps {
namespace core {

class DocumentSession {
public:
    DocumentSession(std::string sessionId)
        : sessionId_(std::move(sessionId)), document_(std::make_unique<Document>()) {}

    const std::string& getSessionId() const { return sessionId_; }
    
    CommandResult applyCommand(Command& command) {
        return command.execute(*document_);
    }
    
    const Document& document() const { return *document_; }
    Document& document() { return *document_; }

    std::string getText() const {
        return document_->getText();
    }

private:
    std::string sessionId_;
    std::unique_ptr<Document> document_;
};

} // namespace core
} // namespace openwps
