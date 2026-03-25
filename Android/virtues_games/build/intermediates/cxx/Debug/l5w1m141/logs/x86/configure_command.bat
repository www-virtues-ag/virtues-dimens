@echo off
"C:\\Users\\evert\\1_SDK_ANDROID_STUDIO\\cmake\\3.22.1\\bin\\cmake.exe" ^
  "-HC:\\Users\\evert\\Downloads\\Nova pasta\\virtues-dimens\\Android\\virtues_games\\src\\main\\cpp" ^
  "-DCMAKE_SYSTEM_NAME=Android" ^
  "-DCMAKE_EXPORT_COMPILE_COMMANDS=ON" ^
  "-DCMAKE_SYSTEM_VERSION=23" ^
  "-DANDROID_PLATFORM=android-23" ^
  "-DANDROID_ABI=x86" ^
  "-DCMAKE_ANDROID_ARCH_ABI=x86" ^
  "-DANDROID_NDK=C:\\Users\\evert\\1_SDK_ANDROID_STUDIO\\ndk\\27.0.12077973" ^
  "-DCMAKE_ANDROID_NDK=C:\\Users\\evert\\1_SDK_ANDROID_STUDIO\\ndk\\27.0.12077973" ^
  "-DCMAKE_TOOLCHAIN_FILE=C:\\Users\\evert\\1_SDK_ANDROID_STUDIO\\ndk\\27.0.12077973\\build\\cmake\\android.toolchain.cmake" ^
  "-DCMAKE_MAKE_PROGRAM=C:\\Users\\evert\\1_SDK_ANDROID_STUDIO\\cmake\\3.22.1\\bin\\ninja.exe" ^
  "-DCMAKE_CXX_FLAGS=-std=c++17 -frtti -fexceptions" ^
  "-DCMAKE_LIBRARY_OUTPUT_DIRECTORY=C:\\Users\\evert\\Downloads\\Nova pasta\\virtues-dimens\\Android\\virtues_games\\build\\intermediates\\cxx\\Debug\\l5w1m141\\obj\\x86" ^
  "-DCMAKE_RUNTIME_OUTPUT_DIRECTORY=C:\\Users\\evert\\Downloads\\Nova pasta\\virtues-dimens\\Android\\virtues_games\\build\\intermediates\\cxx\\Debug\\l5w1m141\\obj\\x86" ^
  "-DCMAKE_BUILD_TYPE=Debug" ^
  "-BC:\\Users\\evert\\Downloads\\Nova pasta\\virtues-dimens\\Android\\virtues_games\\.cxx\\Debug\\l5w1m141\\x86" ^
  -GNinja ^
  "-DANDROID_STL=c++_shared"
