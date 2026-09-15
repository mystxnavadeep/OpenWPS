#pragma once
#include <string>

namespace openwps {
namespace core {

class DocumentObjectId {
public:
    explicit DocumentObjectId(std::string id) : id_(std::move(id)) {}
    const std::string& id() const { return id_; }
private:
    std::string id_;
};

} // namespace core
} // namespace openwps
