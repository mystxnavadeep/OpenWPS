#pragma once
#include <string>
#include "DocumentObjectId.h"
#include <sstream>
#include "../utils/JsonBuilder.h"

namespace openwps {
namespace core {

struct DocumentRange {
    DocumentObjectId startObjectId;
    int startOffset;
    DocumentObjectId endObjectId;
    int endOffset;

    std::string toJson() const {
        std::ostringstream ss;
        ss << "{";
        ss << "\"startObjectId\":\"" << JsonBuilder::escape(startObjectId.id()) << "\",";
        ss << "\"startOffset\":" << startOffset << ",";
        ss << "\"endObjectId\":\"" << JsonBuilder::escape(endObjectId.id()) << "\",";
        ss << "\"endOffset\":" << endOffset;
        ss << "}";
        return ss.str();
    }
};

}
}
