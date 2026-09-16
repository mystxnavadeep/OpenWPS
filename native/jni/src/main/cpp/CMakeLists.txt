cmake_minimum_required(VERSION 3.22.1)

project("openwps-core")

add_library(
        openwps-core
        SHARED
        jni/jni_bridge.cpp
        core/document/DocumentStructure.cpp)

find_library(
        log-lib
        log)

target_link_libraries(
        openwps-core
        ${log-lib})
