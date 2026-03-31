# Complete Build Guide for Private Notifications App

This guide will walk you through building the Android APK from scratch using VS Code or command line.

## Prerequisites Checklist

### Required Software

1. **Java Development Kit (JDK) 11 or higher**
   - Download: https://adoptium.net/
   - Verify installation: `java -version`

2. **Android SDK**
   - Option A: Install Android Studio (easiest)
     - Download: https://developer.android.com/studio
     - During installation, it will install Android SDK automatically
   - Option B: Command Line Tools only
     - Download: https://developer.android.com/studio#command-tools
     - Extract and set ANDROID_HOME

3. **VS Code** (optional, can use terminal instead)
   - Download: https://code.visualstudio.com/

### Environment Variables

You must set `ANDROID_HOME` to point to your Android SDK location:

#### Windows
```cmd
setx ANDROID_HOME "C:\Users\YourUsername\AppData\Local\Android\Sdk"
setx PATH "%PATH%;%ANDROID_HOME%\platform-tools;%ANDROID_HOME%\tools"
```

#### Mac/Linux
Add to `~/.bashrc` or `~/.zshrc`:
```bash
export ANDROID_HOME=$HOME/Android/Sdk
export PATH=$PATH:$ANDROID_HOME/platform-tools:$ANDROID_HOME/tools
```

Then run: `source ~/.bashrc` (or `source ~/.zshrc`)

#### Verify Android SDK
```bash
# Check if adb is accessible
adb version

# Check SDK location
echo $ANDROID_HOME  # Mac/Linux
echo %ANDROID_HOME%  # Windows
```

## Step-by-Step Build Instructions

### Step 1: Download/Clone the Project

```bash
# If using Git
git clone https://github.com/yourusername/PrivateNotifications.git
cd PrivateNotifications

# Or download and extract ZIP
cd PrivateNotifications
```

### Step 2: Install Android SDK Components

The app requires specific SDK components. Install them using SDK Manager:

```bash
# Open Android Studio SDK Manager, or use command line:
sdkmanager "platform-tools" "platforms;android-34" "build-tools;34.0.0"
```

Or in Android Studio:
- Tools → SDK Manager
- SDK Platforms tab: Check "Android 14.0 (API 34)"
- SDK Tools tab: Check "Android SDK Build-Tools 34"
- Click "Apply"

### Step 3: Open in VS Code

```bash
code .
```

**Recommended VS Code Extensions:**
- Android IDE (`adelphes.android-dev-ext`)
- Kotlin Language (`mathiasfrohlich.Kotlin`)
- Gradle for Java (`vscjava.vscode-gradle`)

Install extensions:
1. Press `Ctrl+Shift+X` (Windows/Linux) or `Cmd+Shift+X` (Mac)
2. Search for "Android IDE" and click Install
3. Search for "Kotlin" and click Install
4. Search for "Gradle for Java" and click Install

### Step 4: Make Gradle Wrapper Executable (Mac/Linux only)

```bash
chmod +x gradlew
```

### Step 5: Build the APK

#### Option A: Using VS Code Terminal

1. Open integrated terminal: `` Ctrl+` `` or `Cmd+` ``
2. Run build command:

```bash
# Clean previous builds
./gradlew clean

# Build debug APK (for testing)
./gradlew assembleDebug

# Build release APK (for distribution)
./gradlew assembleRelease
```

#### Option B: Using External Terminal

```bash
# Windows
gradlew.bat clean
gradlew.bat assembleDebug

# Mac/Linux
./gradlew clean
./gradlew assembleDebug
```

### Step 6: Locate the APK

After successful build, find your APK at:

**Debug APK:**
```
app/build/outputs/apk/debug/app-debug.apk
```

**Release APK:**
```
app/build/outputs/apk/release/app-release-unsigned.apk
```

## Installing the APK on Your Device

### Method 1: USB Installation (Recommended)

1. **Enable Developer Options on Android:**
   - Go to Settings → About Phone
   - Tap "Build Number" 7 times
   - You'll see "You are now a developer!"

2. **Enable USB Debugging:**
   - Go to Settings → Developer Options
   - Toggle on "USB Debugging"

3. **Connect phone to computer via USB**

4. **Install using ADB:**
   ```bash
   adb install app/build/outputs/apk/debug/app-debug.apk
   ```

5. **If multiple devices connected:**
   ```bash
   # List devices
   adb devices
   
   # Install to specific device
   adb -s DEVICE_ID install app/build/outputs/apk/debug/app-debug.apk
   ```

### Method 2: File Transfer

1. Copy `app-debug.apk` to your phone (via USB, email, cloud storage)
2. On your phone, tap the APK file
3. You may need to enable "Install from Unknown Sources":
   - Settings → Security → Unknown Sources (toggle ON)
   - Or Settings → Apps → Special Access → Install Unknown Apps
4. Tap "Install"

### Method 3: Wireless ADB (if enabled)

```bash
# Connect to device wirelessly
adb connect 192.168.1.100:5555

# Install APK
adb install app/build/outputs/apk/debug/app-debug.apk
```

## Troubleshooting Common Build Issues

### Issue: "ANDROID_HOME not set"

**Solution:**
```bash
# Set temporarily (session only)
export ANDROID_HOME=$HOME/Android/Sdk  # Mac/Linux
set ANDROID_HOME=C:\Users\...\Android\Sdk  # Windows

# Or add to permanent environment variables (see Prerequisites)
```

### Issue: "SDK location not found"

**Solution:**
Create `local.properties` in project root:
```properties
sdk.dir=/Users/YourUsername/Android/Sdk
```

### Issue: "gradlew: Permission denied"

**Solution (Mac/Linux):**
```bash
chmod +x gradlew
./gradlew assembleDebug
```

### Issue: "Could not find or load main class org.gradle.wrapper.GradleWrapperMain"

**Solution:**
The Gradle wrapper JAR is missing. Download it manually:
```bash
# Download Gradle distribution
gradle wrapper --gradle-version 8.0
```

### Issue: "Installed Build Tools revision 34.0.0 is corrupted"

**Solution:**
```bash
# Reinstall build tools
sdkmanager --uninstall "build-tools;34.0.0"
sdkmanager "build-tools;34.0.0"
```

### Issue: "No matching variant of project :app"

**Solution:**
```bash
# Clean and rebuild
./gradlew clean
./gradlew assembleDebug --refresh-dependencies
```

### Issue: Build succeeds but app crashes on launch

**Solutions:**
1. Check minimum SDK version (should be 24+)
2. Ensure phone Android version is 7.0+
3. Check logcat for errors:
   ```bash
   adb logcat | grep "PrivateNotifications"
   ```

## Building a Signed Release APK

For production distribution, you need a signed APK:

### Step 1: Generate a Keystore

```bash
keytool -genkey -v -keystore my-release-key.jks -keyalg RSA -keysize 2048 -validity 10000 -alias my-key-alias
```

Follow prompts to set password and details.

### Step 2: Configure Signing in build.gradle

Edit `app/build.gradle` and add:

```gradle
android {
    signingConfigs {
        release {
            storeFile file("../my-release-key.jks")
            storePassword "your-keystore-password"
            keyAlias "my-key-alias"
            keyPassword "your-key-password"
        }
    }
    
    buildTypes {
        release {
            signingConfig signingConfigs.release
            minifyEnabled true
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
}
```

### Step 3: Build Signed APK

```bash
./gradlew assembleRelease
```

Signed APK location:
```
app/build/outputs/apk/release/app-release.apk
```

## Performance Tips

### Faster Builds

Add to `gradle.properties`:
```properties
org.gradle.jvmargs=-Xmx4096m
org.gradle.parallel=true
org.gradle.caching=true
org.gradle.configureondemand=true
```

### Skip Tests (faster builds)

```bash
./gradlew assembleDebug -x test
```

### Build Specific Variant

```bash
# Debug only
./gradlew assembleDebug

# Release only
./gradlew assembleRelease
```

## Verifying the Build

### Check APK Details

```bash
# Using aapt
aapt dump badging app/build/outputs/apk/debug/app-debug.apk

# View APK contents
unzip -l app/build/outputs/apk/debug/app-debug.apk
```

### Test Installation

```bash
# Install and launch
adb install -r app/build/outputs/apk/debug/app-debug.apk
adb shell am start -n com.privatenotifications/.MainActivity
```

### Check Logs

```bash
# View app logs
adb logcat -s PrivateNotifications

# Clear logs first
adb logcat -c
adb logcat -s GyroNotificationService
```

## Next Steps

After successful build:

1. ✅ Install app on device
2. ✅ Grant notification access permission
3. ✅ Select apps to monitor
4. ✅ Enable privacy mode
5. ✅ Test by placing phone face-down

## Additional Resources

- **Android Developer Docs**: https://developer.android.com/studio/build
- **Gradle Build Docs**: https://docs.gradle.org/current/userguide/userguide.html
- **ADB Commands**: https://developer.android.com/studio/command-line/adb

## Support

If you encounter issues not covered here:
1. Check `build` directory for detailed error logs
2. Run with `--stacktrace` flag: `./gradlew assembleDebug --stacktrace`
3. Open an issue on GitHub with full error output
