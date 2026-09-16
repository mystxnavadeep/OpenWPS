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
        // Create an initial empty section and block and paragraph
        auto initialSection = std::make_unique<Section>(DocumentObjectId("sec_initial"));
        auto initialBlock = std::make_unique<Block>(DocumentObjectId("blk_initial"));
        auto initialParagraph = std::make_unique<Paragraph>(DocumentObjectId("par_initial"));
        // Give it an empty run
        initialParagraph->addRun(TextRun(DocumentObjectId("run_initial"), ""));
        
        initialBlock->setParagraph(std::move(initialParagraph));
        initialSection->addBlock(std::move(initialBlock));
        sections_.push_back(std::move(initialSection));
    }
    
    int getVersion() const { return version_; }
    void incrementVersion() { version_++; }
    
    // Core structure access
    const std::vector<std::unique_ptr<Section>>& sections() const { return sections_; }
    std::vector<std::unique_ptr<Section>>& sections() { return sections_; }
    
    // Very basic extraction for text commands (just for compatibility with the old interface for now, though we should really upgrade commands)
    std::string getText() const {
        std::ostringstream ss;
        for (const auto& sec : sections_) {
            for (const auto& blk : sec->blocks()) {
                if (blk->paragraph()) {
                    for (const auto& run : blk->paragraph()->runs()) {
                        ss << run.text();
                    }
                    ss << "\n";
                }
            }
        }
        std::string result = ss.str();
        if (!result.empty() && result.back() == '\n') result.pop_back(); // remove trailing newline for single paragraph
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
    
    // Helper to find a run by ID or just use first run (to support simple InsertText). 
    // A proper command should use a DocumentRange!
    void insertTextSimple(const std::string& text) {
        // Find the very last run and append text
        if (sections_.empty()) return;
        auto& sec = sections_.back();
        if (sec->blocks().empty()) return;
        auto& blk = sec->blocks().back();
        if (!blk->paragraph()) return;
        auto& runs = blk->paragraph()->runs();
        if (runs.empty()) {
            runs.push_back(TextRun(DocumentObjectId("run_dyn_" + std::to_string(++idCounter)), text));
        } else {
            runs.back().setText(runs.back().text() + text);
        }
        blk->paragraph()->normalizeRuns();
        version_++;
    }

private:
    std::string content_; // Deprecated, but maybe keep temporarily if needed
    int version_;
    DocumentObjectId id_;
    std::vector<std::unique_ptr<Section>> sections_;
    int idCounter = 0;
};

} // namespace core
} // namespace openwps
