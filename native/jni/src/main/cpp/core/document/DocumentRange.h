#pragma once
#include "DocumentObjectId.h"

namespace openwps {
namespace core {

class DocumentRange {
public:
    DocumentRange(DocumentObjectId startId, int startOff, DocumentObjectId endId, int endOff)
        : startObjectId_(std::move(startId)), startOffset_(startOff),
          endObjectId_(std::move(endId)), endOffset_(endOff) {}

    const DocumentObjectId& startObjectId() const { return startObjectId_; }
    int startOffset() const { return startOffset_; }
    const DocumentObjectId& endObjectId() const { return endObjectId_; }
    int endOffset() const { return endOffset_; }

private:
    DocumentObjectId startObjectId_;
    int startOffset_;
    DocumentObjectId endObjectId_;
    int endOffset_;
};

} // namespace core
} // namespace openwps
