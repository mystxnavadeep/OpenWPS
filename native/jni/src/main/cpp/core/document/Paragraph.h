#pragma once
#include <string>
#include <vector>
#include <memory>
#include <optional>
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
    
    // Core text ops
    std::string text() const;
    void insertText(int offset, const std::string& text, const std::optional<TextStyle>& style = std::nullopt);
    void deleteText(int startOffset, int endOffset);
    void applyStyle(int startOffset, int endOffset, const TextStyle& style);
    
    // Resolution
    bool getWordRange(int wordIndex, int& outStart, int& outEnd) const;
    bool getSentenceRange(int sentenceIndex, int& outStart, int& outEnd) const;
    
    void normalizeRuns();
    
    std::string toJson() const;
    
private:
    void splitRunsAt(int offset);

    DocumentObjectId id_;
    ParagraphStyle style_;
    std::vector<TextRun> runs_;
};

}
}
