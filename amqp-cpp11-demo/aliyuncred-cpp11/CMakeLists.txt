cmake_minimum_required(VERSION 3.15)
project(aliyuncred)

set(CMAKE_CXX_STANDARD 11)

include_directories(SYSTEM ${CMAKE_CURRENT_SOURCE_DIR}/include)

set(LIBRARY_OUTPUT_PATH ${CMAKE_CURRENT_BINARY_DIR}/lib)
aux_source_directory(src src_MAIN)
add_library(${PROJECT_NAME} STATIC ${src_MAIN} src/AliyunCred.cpp)
file(COPY ${CMAKE_CURRENT_SOURCE_DIR}/include/aliyun DESTINATION ${CMAKE_CURRENT_BINARY_DIR}/include/)

