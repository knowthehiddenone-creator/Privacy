# Quick Start Guide - Get Running in 5 Minutes

This guide gets you from zero to a working APK in 5 minutes.

## Prerequisites (1 minute)

1. **Java JDK** installed - Check with: `java -version`
   - If not installed: Download from https://adoptium.net/

2. **Android SDK** - Easiest via Android Studio
   - Download: https://developer.android.com/studio
   - Or just the command-line tools

3. **Set ANDROID_HOME** environment variable
   ```bash
   # Mac/Linux
   export ANDROID_HOME=$HOME/Android/Sdk
   
   # Windows
   set ANDROID_HOME=C:\Users\YourName\AppData\Local\Android\Sdk
   ```

## Build APK (2 minutes)

```bash
# 1. Navigate to project
cd PrivateNotifications

# 2. Make gradlew executable (Mac/Linux only)
chmod +x gradlew

# 3. Build debug APK
./gradlew assembleDebug
# Windows: gradlew.bat assembleDebug
```

**APK Location:** `app/build/outputs/apk/debug/app-debug.apk`

## Install on Phone (1 minute)

### Method 1: USB (Fastest)
```bash
# Enable USB debugging on phone first
# Settings → Developer Options → USB Debugging

# Install
adb install app/build/outputs/apk/debug/app-debug.apk
```

### Method 2: File Transfer
1. Copy `app-debug.apk` to phone
2. Tap file to install
3. Enable "Unknown Sources" if prompted

## Configure App (1 minute)

1. **Launch app** on phone
2. Tap **"Grant Notification Access"**
   - Find "Private Notifications" in list
   - Toggle ON
3. Tap **"Select Apps"**
   - Check WhatsApp, Gmail, etc.
   - Tap Save
4. Toggle **"Enable Privacy Mode"** ON

## Test It! (30 seconds)

1. Place phone **face-down** on table
2. Send yourself a WhatsApp message
3. You should NOT see/hear notification
4. Flip phone **face-up**
5. Notification appears!

---

## Troubleshooting

**Build fails?**
```bash
./gradlew clean
./gradlew assembleDebug --stacktrace
```

**Can't install?**
- Enable USB debugging on phone
- Check USB cable connection
- Try: `adb kill-server && adb start-server`

**Notifications not hiding?**
- Grant notification access permission
- Select apps in app settings
- Ensure privacy mode is enabled

## Need More Help?

- Detailed build guide: See `BUILD_GUIDE.md`
- Testing instructions: See `TESTING_GUIDE.md`
- Full documentation: See `README.md`

---

**You're all set! Enjoy private notifications! 🎉**
