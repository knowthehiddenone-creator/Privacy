@echo off
REM Private Notifications - Automated Build Script for Windows
REM This script automates the build and installation process

setlocal enabledelayedexpansion

echo ======================================
echo   Private Notifications Build Script
echo ======================================
echo.

REM Check if ANDROID_HOME is set
if "%ANDROID_HOME%"=="" (
    echo [ERROR] ANDROID_HOME is not set!
    echo.
    echo Please set ANDROID_HOME environment variable:
    echo   set ANDROID_HOME=C:\Users\YourName\AppData\Local\Android\Sdk
    echo.
    pause
    exit /b 1
)

echo [OK] ANDROID_HOME is set: %ANDROID_HOME%

REM Check if gradlew.bat exists
if not exist "gradlew.bat" (
    echo [ERROR] gradlew.bat not found!
    pause
    exit /b 1
)

REM Parse command line arguments
set BUILD_TYPE=debug
set INSTALL=false
set CLEAN=false

:parse_args
if "%~1"=="" goto args_done
if /i "%~1"=="--release" (
    set BUILD_TYPE=release
    shift
    goto parse_args
)
if /i "%~1"=="--install" (
    set INSTALL=true
    shift
    goto parse_args
)
if /i "%~1"=="--clean" (
    set CLEAN=true
    shift
    goto parse_args
)
if /i "%~1"=="--help" (
    echo Usage: build.bat [OPTIONS]
    echo.
    echo Options:
    echo   --release    Build release APK (default: debug^)
    echo   --install    Install APK on connected device
    echo   --clean      Clean before building
    echo   --help       Show this help message
    echo.
    echo Examples:
    echo   build.bat                          # Build debug APK
    echo   build.bat --release                # Build release APK
    echo   build.bat --clean --install        # Clean, build, and install
    echo.
    pause
    exit /b 0
)
echo [ERROR] Unknown option: %~1
echo Use --help for usage information
pause
exit /b 1

:args_done

REM Clean if requested
if "%CLEAN%"=="true" (
    echo [INFO] Cleaning previous builds...
    call gradlew.bat clean
    echo [OK] Clean complete
)

REM Build the APK
echo [INFO] Building %BUILD_TYPE% APK...
if "%BUILD_TYPE%"=="release" (
    call gradlew.bat assembleRelease
    set APK_PATH=app\build\outputs\apk\release\app-release-unsigned.apk
) else (
    call gradlew.bat assembleDebug
    set APK_PATH=app\build\outputs\apk\debug\app-debug.apk
)

echo [OK] Build complete!

REM Check if APK was created
if not exist "%APK_PATH%" (
    echo [ERROR] APK not found at %APK_PATH%
    pause
    exit /b 1
)

echo [OK] APK location: %APK_PATH%

REM Get APK size
for %%A in ("%APK_PATH%") do set APK_SIZE=%%~zA
set /a APK_SIZE_MB=%APK_SIZE% / 1024 / 1024
echo [INFO] APK size: %APK_SIZE_MB% MB

REM Install if requested
if "%INSTALL%"=="true" (
    echo [INFO] Checking for connected devices...
    
    REM Check if adb is available
    where adb >nul 2>nul
    if errorlevel 1 (
        echo [ERROR] adb command not found!
        echo Make sure Android SDK platform-tools are in your PATH
        pause
        exit /b 1
    )
    
    REM Check for connected devices
    adb devices | find "device" >nul
    if errorlevel 1 (
        echo [ERROR] No devices connected!
        echo.
        echo Please connect your device via USB and enable USB debugging
        pause
        exit /b 1
    )
    
    echo [OK] Device found
    echo [INFO] Installing APK...
    
    adb install -r "%APK_PATH%"
    
    echo [OK] Installation complete!
    echo [INFO] Launching app...
    
    adb shell am start -n com.privatenotifications/.MainActivity
    
    echo [OK] App launched!
)

echo.
echo ======================================
echo   Build Summary
echo ======================================
echo Build Type:    %BUILD_TYPE%
echo APK Location:  %APK_PATH%
echo APK Size:      %APK_SIZE_MB% MB
if "%INSTALL%"=="true" (
    echo Installed:     Yes
) else (
    echo Installed:     No (use --install flag^)
)
echo ======================================
echo.

echo [OK] All done!

REM Show next steps if not installed
if "%INSTALL%"=="false" (
    echo.
    echo [INFO] Next steps:
    echo   1. Copy APK to your device: %APK_PATH%
    echo   2. Or install via adb: build.bat --install
    echo   3. Grant notification access permission
    echo   4. Select apps to monitor
    echo   5. Enable privacy mode
    echo.
)

pause
