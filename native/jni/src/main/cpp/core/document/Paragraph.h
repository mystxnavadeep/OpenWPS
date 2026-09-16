#pragma once
#include <string>
#include <vector>
#include <memory>
#include "DocumentObjectId.h"
#include "TextRun.h"
#include "ParagraphStyle.h"

namespace openwps {
namespace core {

class Paragraph {
public:
    Paragraph(DocumentObjectId id, ParagraphStyle style = ParagraphStyle())
        : id_(std::move(id)), style_(std::move(style)) {}
        
    const DocumentObjectId& id() const { return id_; }
    const ParagraphStyle& style() const { return style_; }
    const std::vector<TextRun>& runs() const { return runs_; }
    std::vector<TextRun>& runs() { return runs_; }
    
    void addRun(const TextRun& run) {
        runs_.push_back(run);
    }
    
    void normalizeRuns();
    
    std::string toJson() const;
    
private:
    DocumentObjectId id_;
    ParagraphStyle style_;
    std::vector<TextRun> runs_;
};

}
}
