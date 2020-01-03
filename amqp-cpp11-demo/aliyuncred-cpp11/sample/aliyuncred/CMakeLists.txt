cmake_minimum_required(VERSION 3.15)
project(sample)
set(CMAKE_CXX_STANDARD 11)

set(ALIYUNCRED_INCLUDE_PATH ./include/)
set(ALIYUNCRED_LIB_PATH ./lib/)
set(OPENSSL_LIB_PATH ./lib/)

include_directories(${ALIYUNCRED_INCLUDE_PATH})
message(${ALIYUNCRED_LIB_PATH})
link_directories(${ALIYUNCRED_LIB_PATH}
        ${OPENSSL_LIB_PATH})
link_libraries(aliyuncred)
link_libraries(crypto)
add_executable(sample sample.cpp)