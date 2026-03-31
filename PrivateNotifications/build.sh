#!/bin/bash

# Private Notifications - Automated Build Script
# This script automates the build and installation process

set -e  # Exit on error

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo "======================================"
echo "  Private Notifications Build Script"
echo "======================================"
echo ""

# Function to print colored output
print_success() {
    echo -e "${GREEN}✓ $1${NC}"
}

print_error() {
    echo -e "${RED}✗ $1${NC}"
}

print_info() {
    echo -e "${YELLOW}ℹ $1${NC}"
}

# Check if ANDROID_HOME is set
if [ -z "$ANDROID_HOME" ]; then
    print_error "ANDROID_HOME is not set!"
    echo ""
    echo "Please set ANDROID_HOME environment variable:"
    echo "  export ANDROID_HOME=\$HOME/Android/Sdk"
    echo ""
    exit 1
fi

print_success "ANDROID_HOME is set: $ANDROID_HOME"

# Check if gradlew exists and is executable
if [ ! -f "./gradlew" ]; then
    print_error "gradlew not found!"
    exit 1
fi

if [ ! -x "./gradlew" ]; then
    print_info "Making gradlew executable..."
    chmod +x gradlew
    print_success "gradlew is now executable"
fi

# Parse command line arguments
BUILD_TYPE="debug"
INSTALL=false
CLEAN=false

while [[ $# -gt 0 ]]; do
    case $1 in
        --release)
            BUILD_TYPE="release"
            shift
            ;;
        --install)
            INSTALL=true
            shift
            ;;
        --clean)
            CLEAN=true
            shift
            ;;
        --help)
            echo "Usage: ./build.sh [OPTIONS]"
            echo ""
            echo "Options:"
            echo "  --release    Build release APK (default: debug)"
            echo "  --install    Install APK on connected device"
            echo "  --clean      Clean before building"
            echo "  --help       Show this help message"
            echo ""
            echo "Examples:"
            echo "  ./build.sh                          # Build debug APK"
            echo "  ./build.sh --release                # Build release APK"
            echo "  ./build.sh --clean --install        # Clean, build, and install"
            echo ""
            exit 0
            ;;
        *)
            print_error "Unknown option: $1"
            echo "Use --help for usage information"
            exit 1
            ;;
    esac
done

# Clean if requested
if [ "$CLEAN" = true ]; then
    print_info "Cleaning previous builds..."
    ./gradlew clean
    print_success "Clean complete"
fi

# Build the APK
print_info "Building $BUILD_TYPE APK..."
if [ "$BUILD_TYPE" = "release" ]; then
    ./gradlew assembleRelease
    APK_PATH="app/build/outputs/apk/release/app-release-unsigned.apk"
else
    ./gradlew assembleDebug
    APK_PATH="app/build/outputs/apk/debug/app-debug.apk"
fi

print_success "Build complete!"

# Check if APK was created
if [ ! -f "$APK_PATH" ]; then
    print_error "APK not found at $APK_PATH"
    exit 1
fi

print_success "APK location: $APK_PATH"

# Get APK size
APK_SIZE=$(du -h "$APK_PATH" | cut -f1)
print_info "APK size: $APK_SIZE"

# Install if requested
if [ "$INSTALL" = true ]; then
    print_info "Checking for connected devices..."
    
    # Check if adb is available
    if ! command -v adb &> /dev/null; then
        print_error "adb command not found!"
        echo "Make sure Android SDK platform-tools are in your PATH"
        exit 1
    fi
    
    # Check for connected devices
    DEVICE_COUNT=$(adb devices | grep -w "device" | wc -l)
    
    if [ "$DEVICE_COUNT" -eq 0 ]; then
        print_error "No devices connected!"
        echo ""
        echo "Please connect your device via USB and enable USB debugging"
        exit 1
    fi
    
    print_success "Found $DEVICE_COUNT device(s)"
    print_info "Installing APK..."
    
    adb install -r "$APK_PATH"
    
    print_success "Installation complete!"
    print_info "Launching app..."
    
    adb shell am start -n com.privatenotifications/.MainActivity
    
    print_success "App launched!"
fi

echo ""
echo "======================================"
echo "  Build Summary"
echo "======================================"
echo "Build Type:    $BUILD_TYPE"
echo "APK Location:  $APK_PATH"
echo "APK Size:      $APK_SIZE"
if [ "$INSTALL" = true ]; then
    echo "Installed:     Yes"
else
    echo "Installed:     No (use --install flag)"
fi
echo "======================================"
echo ""

print_success "All done! 🎉"

# Show next steps if not installed
if [ "$INSTALL" = false ]; then
    echo ""
    print_info "Next steps:"
    echo "  1. Copy APK to your device: $APK_PATH"
    echo "  2. Or install via adb: ./build.sh --install"
    echo "  3. Grant notification access permission"
    echo "  4. Select apps to monitor"
    echo "  5. Enable privacy mode"
    echo ""
fi
