#pragma once
#include <string>
#include <vector>
#include <memory>
#include <optional>
#include "DocumentObjectId.h"
#include "Block.h"
#include "SectionProperties.h"

namespace openwps {
namespace core {

class Section {
public:
    Section(DocumentObjectId id, SectionProperties props = SectionProperties()) 
        : id_(std::move(id)), props_(std::move(props)) {}
    
    const DocumentObjectId& id() const { return id_; }
    const SectionProperties& properties() const { return props_; }
    void setProperties(const SectionProperties& props) { props_ = props; }

    const std::vector<std::unique_ptr<Block>>& blocks() const { return blocks_; }
    
    void addBlock(std::unique_ptr<Block> block) {
        blocks_.push_back(std::move(block));
    }
    
    std::string toJson() const;
    
private:
    DocumentObjectId id_;
    SectionProperties props_;
    std::vector<std::unique_ptr<Block>> blocks_;
};

}
}
