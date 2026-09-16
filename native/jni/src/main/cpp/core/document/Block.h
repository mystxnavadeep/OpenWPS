#pragma once
#include <string>
#include <memory>
#include "DocumentObjectId.h"
#include "Paragraph.h"

namespace openwps {
namespace core {

class Block {
public:
    Block(DocumentObjectId id) : id_(std::move(id)) {}
    
    const DocumentObjectId& id() const { return id_; }
    
    void setParagraph(std::unique_ptr<Paragraph> p) { paragraph_ = std::move(p); }
    Paragraph* paragraph() const { return paragraph_.get(); }
    
    std::string toJson() const;
    
private:
    DocumentObjectId id_;
    std::unique_ptr<Paragraph> paragraph_;
};

}
}
