#include "TextStyle.h"
#include "ParagraphStyle.h"
#include "TextRun.h"
#include "Paragraph.h"
#include "Block.h"
#include "Section.h"
#include "../utils/JsonBuilder.h"
#include <sstream>
#include <cctype>

namespace openwps {
namespace core {

std::string TextStyle::toJson() const {
    std::ostringstream ss;
    ss << "{";
    bool first = true;
    auto addComma = [&]() { if (!first) ss << ","; first = false; };
    
    if (fontFamily) { addComma(); ss << "\"fontFamily\":\"" << JsonBuilder::escape(*fontFamily) << "\""; }
    if (fontSize) { addComma(); ss << "\"fontSize\":" << *fontSize; }
    if (isBold) { addComma(); ss << "\"isBold\":" << (*isBold ? "true" : "false"); }
    if (isItalic) { addComma(); ss << "\"isItalic\":" << (*isItalic ? "true" : "false"); }
    if (isUnderline) { addComma(); ss << "\"isUnderline\":" << (*isUnderline ? "true" : "false"); }
    if (isStrikethrough) { addComma(); ss << "\"isStrikethrough\":" << (*isStrikethrough ? "true" : "false"); }
    if (isSuperscript) { addComma(); ss << "\"isSuperscript\":" << (*isSuperscript ? "true" : "false"); }
    if (isSubscript) { addComma(); ss << "\"isSubscript\":" << (*isSubscript ? "true" : "false"); }
    if (textColorHex) { addComma(); ss << "\"textColorHex\":\"" << JsonBuilder::escape(*textColorHex) << "\""; }
    if (highlightColorHex) { addComma(); ss << "\"highlightColorHex\":\"" << JsonBuilder::escape(*highlightColorHex) << "\""; }
    ss << "}";
    return ss.str();
}

std::string ParagraphStyle::toJson() const {
    std::ostringstream ss;
    ss << "{";
    bool first = true;
    auto addComma = [&]() { if (!first) ss << ","; first = false; };
    
    if (alignment) { addComma(); ss << "\"alignment\":\"" << JsonBuilder::escape(*alignment) << "\""; }
    if (indentLeft) { addComma(); ss << "\"indentLeft\":" << *indentLeft; }
    if (indentRight) { addComma(); ss << "\"indentRight\":" << *indentRight; }
    if (indentFirstLine) { addComma(); ss << "\"indentFirstLine\":" << *indentFirstLine; }
    if (spacingBefore) { addComma(); ss << "\"spacingBefore\":" << *spacingBefore; }
    if (spacingAfter) { addComma(); ss << "\"spacingAfter\":" << *spacingAfter; }
    if (lineSpacing) { addComma(); ss << "\"lineSpacing\":" << *lineSpacing; }
    if (headingLevel) { addComma(); ss << "\"headingLevel\":" << *headingLevel; }
    if (isList) { addComma(); ss << "\"isList\":" << (*isList ? "true" : "false"); }
    if (listId) { addComma(); ss << "\"listId\":\"" << JsonBuilder::escape(*listId) << "\""; }
    if (listLevel) { addComma(); ss << "\"listLevel\":" << *listLevel; }
    
    ss << "}";
    return ss.str();
}

std::string TextRun::toJson() const {
    std::ostringstream ss;
    ss << "{";
    ss << "\"id\":\"" << JsonBuilder::escape(id_.id()) << "\",";
    ss << "\"text\":\"" << JsonBuilder::escape(text_) << "\",";
    ss << "\"style\":" << style_.toJson();
    ss << "}";
    return ss.str();
}

std::string Paragraph::toJson() const {
    std::ostringstream ss;
    ss << "{";
    ss << "\"id\":\"" << JsonBuilder::escape(id_.id()) << "\",";
    ss << "\"style\":" << style_.toJson() << ",";
    ss << "\"runs\":[";
    for (size_t i = 0; i < runs_.size(); ++i) {
        if (i > 0) ss << ",";
        ss << runs_[i].toJson();
    }
    ss << "]";
    ss << "}";
    return ss.str();
}

std::string Paragraph::text() const {
    std::string fullText;
    for (const auto& run : runs_) {
        fullText += run.text();
    }
    return fullText;
}

void Paragraph::splitRunsAt(int offset) {
    if (offset <= 0) return;
    int currentOffset = 0;
    for (size_t i = 0; i < runs_.size(); ++i) {
        int runLen = runs_[i].text().length();
        if (currentOffset + runLen > offset) {
            int splitPos = offset - currentOffset;
            std::string left = runs_[i].text().substr(0, splitPos);
            std::string right = runs_[i].text().substr(splitPos);
            TextStyle style = runs_[i].style();
            runs_[i].setText(left);
            runs_.insert(runs_.begin() + i + 1, TextRun(DocumentObjectId("run_split"), right, style));
            break;
        }
        currentOffset += runLen;
    }
}

void Paragraph::insertText(int offset, const std::string& textToInsert, const std::optional<TextStyle>& style) {
    if (textToInsert.empty()) return;
    
    if (runs_.empty()) {
        runs_.push_back(TextRun(DocumentObjectId("run_new"), textToInsert, style.value_or(TextStyle())));
        return;
    }

    if (offset <= 0) {
        runs_.insert(runs_.begin(), TextRun(DocumentObjectId("run_new"), textToInsert, style.value_or(runs_[0].style())));
    } else {
        std::string full = text();
        if (offset >= full.length()) {
            runs_.push_back(TextRun(DocumentObjectId("run_new"), textToInsert, style.value_or(runs_.back().style())));
        } else {
            splitRunsAt(offset);
            int currentOffset = 0;
            for (size_t i = 0; i < runs_.size(); ++i) {
                currentOffset += runs_[i].text().length();
                if (currentOffset == offset) {
                    TextStyle targetStyle = style.value_or(runs_[i].style());
                    runs_.insert(runs_.begin() + i + 1, TextRun(DocumentObjectId("run_new"), textToInsert, targetStyle));
                    break;
                }
            }
        }
    }
    normalizeRuns();
}

void Paragraph::deleteText(int startOffset, int endOffset) {
    if (startOffset >= endOffset) return;
    std::string full = text();
    if (startOffset < 0) startOffset = 0;
    if (endOffset > full.length()) endOffset = full.length();
    
    splitRunsAt(endOffset);
    splitRunsAt(startOffset);
    
    std::vector<TextRun> newRuns;
    int currentOffset = 0;
    for (const auto& run : runs_) {
        int runLen = run.text().length();
        bool isInsideDeleteRange = (currentOffset >= startOffset && currentOffset < endOffset);
        if (!isInsideDeleteRange) {
            newRuns.push_back(run);
        }
        currentOffset += runLen;
    }
    runs_ = std::move(newRuns);
    normalizeRuns();
}

void Paragraph::applyStyle(int startOffset, int endOffset, const TextStyle& style) {
    if (startOffset >= endOffset) return;
    std::string full = text();
    if (startOffset < 0) startOffset = 0;
    if (endOffset > full.length()) endOffset = full.length();

    splitRunsAt(endOffset);
    splitRunsAt(startOffset);
    
    int currentOffset = 0;
    for (auto& run : runs_) {
        int runLen = run.text().length();
        if (currentOffset >= startOffset && currentOffset < endOffset) {
            run.setStyle(style);
        }
        currentOffset += runLen;
    }
    normalizeRuns();
}

void Paragraph::normalizeRuns() {
    if (runs_.empty()) return;
    std::vector<TextRun> newRuns;
    
    for (size_t i = 0; i < runs_.size(); ++i) {
        if (runs_[i].text().empty()) continue;
        if (!newRuns.empty() && newRuns.back().style() == runs_[i].style()) {
            newRuns.back().setText(newRuns.back().text() + runs_[i].text());
        } else {
            newRuns.push_back(runs_[i]);
        }
    }
    
    if(newRuns.empty()) newRuns.push_back(TextRun(DocumentObjectId("run_empty"), ""));
    runs_ = std::move(newRuns);
}

// Tokenizer rules for words: consecutive alphanumerics or single punctuations.
// We consider any run of whitespaces as separating boundaries.
bool Paragraph::getWordRange(int wordIndex, int& outStart, int& outEnd) const {
    std::string t = text();
    int currentWordIdx = 0;
    bool inWord = false;
    int wordStart = -1;
    
    for (size_t i = 0; i < t.length(); ++i) {
        char c = t[i];
        if (std::isspace(c)) {
            if (inWord) {
                if (currentWordIdx == wordIndex) {
                    outStart = wordStart;
                    outEnd = i;
                    return true;
                }
                inWord = false;
                currentWordIdx++;
            }
        } else if (std::ispunct(c)) {
            if (inWord) {
                if (currentWordIdx == wordIndex) {
                    outStart = wordStart;
                    outEnd = i;
                    return true;
                }
                inWord = false;
                currentWordIdx++;
            }
            // Punctuation itself counts as a single word token
            if (currentWordIdx == wordIndex) {
                outStart = i;
                outEnd = i + 1;
                return true;
            }
            currentWordIdx++;
        } else {
            // Alphanumeric
            if (!inWord) {
                inWord = true;
                wordStart = i;
            }
        }
    }
    if (inWord && currentWordIdx == wordIndex) {
        outStart = wordStart;
        outEnd = t.length();
        return true;
    }
    return false;
}

// Tokenizer rules for sentences: ends with . ! ? followed by space or EOF
bool Paragraph::getSentenceRange(int sentenceIndex, int& outStart, int& outEnd) const {
    std::string t = text();
    int currentSentenceIdx = 0;
    int sentenceStart = 0;
    
    for (size_t i = 0; i < t.length(); ++i) {
        char c = t[i];
        if (c == '.' || c == '!' || c == '?') {
            if (i == t.length() - 1 || std::isspace(t[i + 1])) {
                // End of sentence found
                int endPos = i + 1;
                if (currentSentenceIdx == sentenceIndex) {
                    // skip leading whitespace
                    while(sentenceStart < endPos && std::isspace(t[sentenceStart])) sentenceStart++;
                    outStart = sentenceStart;
                    outEnd = endPos;
                    return true;
                }
                sentenceStart = endPos;
                currentSentenceIdx++;
            }
        }
    }
    
    if (sentenceStart < t.length() && currentSentenceIdx == sentenceIndex) {
        while(sentenceStart < t.length() && std::isspace(t[sentenceStart])) sentenceStart++;
        if (sentenceStart < t.length()) {
            outStart = sentenceStart;
            outEnd = t.length();
            return true;
        }
    }
    
    return false;
}


std::string Block::toJson() const {
    std::ostringstream ss;
    ss << "{";
    ss << "\"id\":\"" << JsonBuilder::escape(id_.id()) << "\"";
    if (paragraph_) {
        ss << ",\"paragraph\":" << paragraph_->toJson();
    }
    ss << "}";
    return ss.str();
}

std::string SectionProperties::toJson() const {
    std::ostringstream ss;
    ss << "{";
    bool first = true;
    auto addComma = [&]() { if (!first) ss << ","; first = false; };
    
    if (pageSize) {
        addComma();
        ss << "\"pageSize\":{\"width\":" << pageSize->width << ",\"height\":" << pageSize->height << "}";
    }
    if (orientation) {
        addComma();
        ss << "\"orientation\":\"" << JsonBuilder::escape(*orientation) << "\"";
    }
    if (margins) {
        addComma();
        ss << "\"margins\":{\"top\":" << margins->top << ",\"bottom\":" << margins->bottom << ",\"left\":" << margins->left << ",\"right\":" << margins->right << "}";
    }
    
    ss << "}";
    return ss.str();
}

std::string Section::toJson() const {
    std::ostringstream ss;
    ss << "{";
    ss << "\"id\":\"" << JsonBuilder::escape(id_.id()) << "\",";
    ss << "\"properties\":" << props_.toJson() << ",";
    ss << "\"blocks\":[";
    for (size_t i = 0; i < blocks_.size(); ++i) {
        if (i > 0) ss << ",";
        ss << blocks_[i]->toJson();
    }
    ss << "]";
    ss << "}";
    return ss.str();
}

}
}
