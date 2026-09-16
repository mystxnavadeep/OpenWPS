#pragma once
#include <string>
#include <vector>
#include <memory>
#include <sstream>
#include "DocumentObjectId.h"
#include "Section.h"
#include "../utils/JsonBuilder.h"

namespace openwps {
namespace core {

class Document {
public:
    Document() : version_(1), id_("doc_" + std::to_string(reinterpret_cast<uintptr_t>(this))) {
        auto initialSection = std::make_unique<Section>(DocumentObjectId("sec_initial"));
        auto initialBlock = std::make_unique<Block>(DocumentObjectId("blk_initial"));
        auto initialParagraph = std::make_unique<Paragraph>(DocumentObjectId("par_initial"));
        initialParagraph->addRun(TextRun(DocumentObjectId("run_initial"), ""));
        
        initialBlock->setParagraph(std::move(initialParagraph));
        initialSection->addBlock(std::move(initialBlock));
        sections_.push_back(std::move(initialSection));
    }
    
    int getVersion() const { return version_; }
    void incrementVersion() { version_++; }
    
    const std::vector<std::unique_ptr<Section>>& sections() const { return sections_; }
    std::vector<std::unique_ptr<Section>>& sections() { return sections_; }
    
    Paragraph* findParagraph(const std::string& id) {
        for (const auto& sec : sections_) {
            for (const auto& blk : sec->blocks()) {
                if (blk->paragraph() && blk->paragraph()->id().id() == id) {
                    return blk->paragraph();
                }
            }
        }
        return nullptr;
    }
    
    std::string getText() const {
        std::ostringstream ss;
        for (const auto& sec : sections_) {
            for (const auto& blk : sec->blocks()) {
                if (blk->paragraph()) {
                    ss << blk->paragraph()->text() << "\n";
                }
            }
        }
        std::string result = ss.str();
        if (!result.empty() && result.back() == '\n') result.pop_back(); 
        return result;
    }
    
    std::string toJson() const {
        std::ostringstream ss;
        ss << "{";
        ss << "\"id\":\"" << JsonBuilder::escape(id_.id()) << "\",";
        ss << "\"version\":" << version_ << ",";
        ss << "\"sections\":[";
        for (size_t i = 0; i < sections_.size(); ++i) {
            if (i > 0) ss << ",";
            ss << sections_[i]->toJson();
        }
        ss << "]";
        ss << "}";
        return ss.str();
    }
    
    void insertTextSimple(const std::string& text) {
        if (sections_.empty()) return;
        auto& sec = sections_.back();
        if (sec->blocks().empty()) return;
        auto& blk = sec->blocks().back();
        if (!blk->paragraph()) return;
        
        std::string fullText = blk->paragraph()->text();
        blk->paragraph()->insertText(fullText.length(), text);
        version_++;
    }

private:
    int version_;
    DocumentObjectId id_;
    std::vector<std::unique_ptr<Section>> sections_;
};

} 
} 
