#include "TextStyle.h"
#include "ParagraphStyle.h"
#include "TextRun.h"
#include "Paragraph.h"
#include "Block.h"
#include "Section.h"
#include "../utils/JsonBuilder.h"
#include <sstream>

namespace openwps {
namespace core {

std::string TextStyle::toJson() const {
    std::ostringstream ss;
    ss << "{";
    bool first = true;
    auto addProp = [&](const char* name, const std::string& val, bool isStr) {
        if (!first) ss << ",";
        ss << "\\"" << name << "\\":";
        if (isStr) ss << "\\"" << val << "\\""; else ss << val;
        first = false;
    };
    if (fontFamily) addProp("fontFamily", JsonBuilder::escape(*fontFamily), true);
    if (fontSize) addProp("fontSize", std::to_string(*fontSize), false);
    if (isBold) addProp("isBold", *isBold ? "true" : "false", false);
    if (isItalic) addProp("isItalic", *isItalic ? "true" : "false", false);
    if (isUnderline) addProp("isUnderline", *isUnderline ? "true" : "false", false);
    if (textColorHex) addProp("textColorHex", JsonBuilder::escape(*textColorHex), true);
    ss << "}";
    return ss.str();
}

std::string ParagraphStyle::toJson() const {
    std::ostringstream ss;
    ss << "{";
    if (alignment) {
        ss << "\\"alignment\\":\\"" << JsonBuilder::escape(*alignment) << "\\"";
    }
    ss << "}";
    return ss.str();
}

std::string TextRun::toJson() const {
    std::ostringstream ss;
    ss << "{";
    ss << "\\"id\\":\\"" << JsonBuilder::escape(id_.id()) << "\\",";
    ss << "\\"text\\":\\"" << JsonBuilder::escape(text_) << "\\",";
    ss << "\\"style\\":" << style_.toJson();
    ss << "}";
    return ss.str();
}

std::string Paragraph::toJson() const {
    std::ostringstream ss;
    ss << "{";
    ss << "\\"id\\":\\"" << JsonBuilder::escape(id_.id()) << "\\",";
    ss << "\\"style\\":" << style_.toJson() << ",";
    ss << "\\"runs\\":[";
    for (size_t i = 0; i < runs_.size(); ++i) {
        if (i > 0) ss << ",";
        ss << runs_[i].toJson();
    }
    ss << "]";
    ss << "}";
    return ss.str();
}

void Paragraph::normalizeRuns() {
    if (runs_.empty()) return;
    std::vector<TextRun> newRuns;
    newRuns.push_back(runs_[0]);
    for (size_t i = 1; i < runs_.size(); ++i) {
        if (newRuns.back().style() == runs_[i].style()) {
            newRuns.back().setText(newRuns.back().text() + runs_[i].text());
        } else {
            newRuns.push_back(runs_[i]);
        }
    }
    runs_ = std::move(newRuns);
}

std::string Block::toJson() const {
    std::ostringstream ss;
    ss << "{";
    ss << "\\"id\\":\\"" << JsonBuilder::escape(id_.id()) << "\\"";
    if (paragraph_) {
        ss << ",\\"paragraph\\":" << paragraph_->toJson();
    }
    ss << "}";
    return ss.str();
}

std::string Section::toJson() const {
    std::ostringstream ss;
    ss << "{";
    ss << "\\"id\\":\\"" << JsonBuilder::escape(id_.id()) << "\\",";
    ss << "\\"blocks\\":[";
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
