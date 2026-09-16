#pragma once
#include <string>
#include <vector>
#include <memory>
#include "DocumentObjectId.h"
#include "Block.h"

namespace openwps {
namespace core {

class Section {
public:
    Section(DocumentObjectId id) : id_(std::move(id)) {}
    
    const DocumentObjectId& id() const { return id_; }
    const std::vector<std::unique_ptr<Block>>& blocks() const { return blocks_; }
    
    void addBlock(std::unique_ptr<Block> block) {
        blocks_.push_back(std::move(block));
    }
    
    std::string toJson() const;
    
private:
    DocumentObjectId id_;
    std::vector<std::unique_ptr<Block>> blocks_;
};

}
}
