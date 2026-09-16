#pragma once
#include <string>
#include "DocumentObjectId.h"
#include "TextStyle.h"

namespace openwps {
namespace core {

class TextRun {
public:
    TextRun(DocumentObjectId id, std::string text, TextStyle style = TextStyle())
        : id_(std::move(id)), text_(std::move(text)), style_(std::move(style)) {}
        
    const DocumentObjectId& id() const { return id_; }
    const std::string& text() const { return text_; }
    const TextStyle& style() const { return style_; }
    
    void setText(const std::string& newText) { text_ = newText; }
    void setStyle(const TextStyle& newStyle) { style_ = newStyle; }
    
    std::string toJson() const;
    
private:
    DocumentObjectId id_;
    std::string text_;
    TextStyle style_;
};

}
}
