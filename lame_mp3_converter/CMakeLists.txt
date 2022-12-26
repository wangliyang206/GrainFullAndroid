# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)

#define cpp source path
set(SRC_DIR src/main/cpp/lamemp3)

#set *.h source path
include_directories(src/main/cpp/lamemp3)

#set cpp source path
aux_source_directory(src/main/cpp/lamemp3 SRC_LIST)

#set *.so files output path，please add this before add_library
set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${PROJECT_SOURCE_DIR}/src/main/jniLibs/${ANDROID_ABI})

#add cpp files into library
add_library(lame-mp3-utils SHARED src/main/cpp/lame-mp3-utils.cpp ${SRC_LIST})

find_library( # Sets the name of the path variable.
              log-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
                       lame-mp3-utils

                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib} )