# 📱 COMPLETE SETUP GUIDE - PRIVATE NOTIFICATIONS APP
## Step-by-Step Instructions from Zero to APK

This guide will take you from **nothing installed** to a **working APK on your Android phone**.  
Follow EVERY step exactly as written.

---

## 🎯 WHAT YOU WILL ACCOMPLISH

By the end of this guide, you will have:
1. ✅ VS Code installed and configured
2. ✅ Java JDK installed
3. ✅ Android SDK installed
4. ✅ Project opened in VS Code
5. ✅ All dependencies downloaded
6. ✅ APK file built successfully
7. ✅ App installed on your Android phone

**Total Time:** 30-45 minutes (first time)  
**Skill Level:** Beginner friendly - no experience needed

---

## 📋 PART 1: INSTALL PREREQUISITES (15 minutes)

### STEP 1.1: Install Java JDK

**Windows:**
1. Go to: https://adoptium.net/
2. Click the big "Download" button (Temurin 17 LTS recommended)
3. Download the `.msi` file (Windows x64)
4. Run the installer
5. ✅ Check "Set JAVA_HOME variable" during installation
6. ✅ Check "Add to PATH" during installation
7. Click "Next" through the installation
8. Wait for installation to complete

**Mac:**
1. Go to: https://adoptium.net/
2. Download the `.pkg` file (macOS)
3. Run the installer
4. Follow the installation wizard
5. Complete installation

**Linux (Ubuntu/Debian):**
```bash
sudo apt update
sudo apt install openjdk-17-jdk -y
```

**Verify Java Installation:**
Open terminal/command prompt and run:
```bash
java -version
```

You should see something like:
```
openjdk version "17.0.x" ...
```

✅ If you see version number → Java is installed correctly!  
❌ If you get an error → Restart your computer and try again

---

### STEP 1.2: Install Android Studio (Includes Android SDK)

**Why Android Studio?**  
It automatically installs Android SDK, which we need to build the APK.

**Windows/Mac/Linux:**
1. Go to: https://developer.android.com/studio
2. Click "Download Android Studio"
3. Accept the terms and click "Download"
4. Run the installer (`.exe` for Windows, `.dmg` for Mac)

**Installation Steps:**
1. Run the installer
2. Select "Standard" installation type
3. ✅ Make sure "Android SDK" is checked
4. ✅ Make sure "Android SDK Platform" is checked
5. ✅ Make sure "Android Virtual Device" is checked (optional)
6. Choose installation location (default is fine)
7. Wait for installation (10-15 minutes, downloads ~3 GB)

**First Launch Setup:**
1. Open Android Studio
2. Click "Next" through the setup wizard
3. Choose "Standard" setup type
4. Choose theme (Light or Dark - your preference)
5. Click "Finish" and wait for SDK components to download
6. You'll see "Android SDK Platform 34" and other components downloading

✅ Wait until you see "Setup Finished" or the main Android Studio window

---

### STEP 1.3: Set ANDROID_HOME Environment Variable

This tells your computer where Android SDK is located.

**Windows:**

1. Press `Windows + R`
2. Type `sysdm.cpl` and press Enter
3. Click "Advanced" tab
4. Click "Environment Variables" button
5. Under "System variables", click "New"
6. Variable name: `ANDROID_HOME`
7. Variable value: `C:\Users\YourUsername\AppData\Local\Android\Sdk`
   - Replace `YourUsername` with your actual Windows username
   - Or click "Browse Directory" and navigate to the SDK folder
8. Click "OK"
9. Find "Path" in System variables, select it, click "Edit"
10. Click "New" and add: `%ANDROID_HOME%\platform-tools`
11. Click "New" and add: `%ANDROID_HOME%\tools`
12. Click "OK" on all windows
13. **RESTART YOUR COMPUTER** (important!)

**Mac:**

1. Open Terminal
2. Check which shell you're using:
   ```bash
   echo $SHELL
   ```
3. If it says `/bin/zsh`, edit `.zshrc`:
   ```bash
   nano ~/.zshrc
   ```
   If it says `/bin/bash`, edit `.bash_profile`:
   ```bash
   nano ~/.bash_profile
   ```
4. Add these lines at the end:
   ```bash
   export ANDROID_HOME=$HOME/Library/Android/sdk
   export PATH=$PATH:$ANDROID_HOME/platform-tools
   export PATH=$PATH:$ANDROID_HOME/tools
   ```
5. Press `Ctrl+O` to save, then `Ctrl+X` to exit
6. Run this to apply changes:
   ```bash
   source ~/.zshrc
   # or
   source ~/.bash_profile
   ```

**Linux:**

```bash
nano ~/.bashrc
```

Add these lines at the end:
```bash
export ANDROID_HOME=$HOME/Android/Sdk
export PATH=$PATH:$ANDROID_HOME/platform-tools
export PATH=$PATH:$ANDROID_HOME/tools
```

Press `Ctrl+O` to save, `Ctrl+X` to exit, then:
```bash
source ~/.bashrc
```

**Verify ANDROID_HOME:**

Open a NEW terminal/command prompt and run:
```bash
echo $ANDROID_HOME
# Mac/Linux

echo %ANDROID_HOME%
# Windows
```

You should see the path to your Android SDK.

---

### STEP 1.4: Install VS Code

1. Go to: https://code.visualstudio.com/
2. Click "Download" for your operating system
3. Run the installer
4. ✅ Check "Add to PATH" during installation (important!)
5. Complete installation
6. Launch VS Code

---

### STEP 1.5: Install VS Code Extensions

1. Open VS Code
2. Click the Extensions icon (or press `Ctrl+Shift+X`)
3. Search for and install these extensions:

**Required Extensions:**
- **Kotlin Language** (by mathiasfrohlich)
  - Search: "Kotlin"
  - Click "Install"

- **Gradle for Java** (by Microsoft)
  - Search: "Gradle for Java"
  - Click "Install"

**Optional but Recommended:**
- **Android iOS Emulator** (by DiemasMichiels)
- **XML Tools** (by Josh Johnson)

Wait for all extensions to install.

---

## 📦 PART 2: EXTRACT AND OPEN PROJECT (5 minutes)

### STEP 2.1: Extract the ZIP File

1. Download `PrivateNotifications.zip` 
2. **Right-click** on the ZIP file
3. Choose "Extract All" (Windows) or "Open" (Mac)
4. Extract to a location you can find easily, like:
   - Windows: `C:\Users\YourName\AndroidProjects\`
   - Mac: `/Users/YourName/AndroidProjects/`
   - Linux: `/home/yourname/AndroidProjects/`

You should now have a folder called `PrivateNotifications`

---

### STEP 2.2: Open Project in VS Code

**Method 1: From VS Code**
1. Open VS Code
2. Click "File" → "Open Folder"
3. Navigate to the `PrivateNotifications` folder
4. Click "Select Folder" (Windows) or "Open" (Mac)

**Method 2: From Terminal/Command Prompt**
```bash
cd /path/to/PrivateNotifications
code .
```

**Method 3: Right-click (if you have "Open with Code" option)**
1. Right-click on `PrivateNotifications` folder
2. Choose "Open with Code"

---

### STEP 2.3: Trust the Workspace

When VS Code opens the project:
1. You'll see a popup: "Do you trust the authors of this folder?"
2. Click "Yes, I trust the authors"

---

## 🔧 PART 3: INSTALL DEPENDENCIES (10 minutes)

### STEP 3.1: Open Integrated Terminal in VS Code

1. In VS Code, press `` Ctrl+` `` (backtick key, usually above Tab)
2. Or click "Terminal" → "New Terminal" from the menu

You should see a terminal at the bottom of VS Code.

---

### STEP 3.2: Make Gradle Wrapper Executable (Mac/Linux Only)

**Mac/Linux users run:**
```bash
chmod +x gradlew
```

**Windows users:** Skip this step

---

### STEP 3.3: Download All Dependencies

This command will download all Android dependencies automatically.

**Run this command in the VS Code terminal:**

**Windows:**
```bash
gradlew.bat build --refresh-dependencies
```

**Mac/Linux:**
```bash
./gradlew build --refresh-dependencies
```

**What happens:**
- Gradle downloads (~5 minutes)
- Android dependencies download (~5-10 minutes)
- You'll see lots of text scrolling
- First time takes longer (downloading ~500 MB)

**Wait until you see:**
```
BUILD SUCCESSFUL
```

✅ If you see "BUILD SUCCESSFUL" → All dependencies installed!  
❌ If you see "BUILD FAILED" → See Troubleshooting section below

---

## 🏗️ PART 4: BUILD THE APK (5 minutes)

### STEP 4.1: Clean Previous Builds (optional, recommended)

```bash
# Windows
gradlew.bat clean

# Mac/Linux
./gradlew clean
```

---

### STEP 4.2: Build Debug APK

This creates the APK file you'll install on your phone.

**Windows:**
```bash
gradlew.bat assembleDebug
```

**Mac/Linux:**
```bash
./gradlew assembleDebug
```

**What you'll see:**
```
> Task :app:compileDebugKotlin
> Task :app:processDebugResources
> Task :app:mergeDebugResources
> Task :app:packageDebug
> Task :app:assembleDebug

BUILD SUCCESSFUL in 2m 15s
```

✅ **BUILD SUCCESSFUL** = APK created successfully!

---

### STEP 4.3: Locate Your APK

The APK file is at:
```
PrivateNotifications/app/build/outputs/apk/debug/app-debug.apk
```

**In VS Code:**
1. Click on "Explorer" (top-left icon)
2. Navigate: `app` → `build` → `outputs` → `apk` → `debug`
3. You should see `app-debug.apk` (about 5-8 MB)

**Right-click on `app-debug.apk`** → "Reveal in File Explorer" (Windows) or "Reveal in Finder" (Mac)

---

## 📱 PART 5: INSTALL ON ANDROID PHONE (10 minutes)

### STEP 5.1: Enable USB Debugging on Your Phone

**On your Android phone:**

1. Go to **Settings**
2. Scroll to **About Phone**
3. Find **Build Number**
4. **Tap "Build Number" 7 times rapidly**
5. You'll see "You are now a developer!"
6. Go back to **Settings**
7. You should now see **Developer Options** (or **System** → **Developer Options**)
8. Open **Developer Options**
9. Find **USB Debugging**
10. Toggle it **ON**
11. When prompted, tap **"OK"** to allow USB debugging

---

### STEP 5.2: Connect Phone to Computer

1. Get a USB cable
2. Connect your phone to your computer
3. On your phone, you'll see a popup: "Allow USB debugging?"
4. ✅ Check "Always allow from this computer"
5. Tap **"OK"**

---

### STEP 5.3: Verify Phone Connection

**In VS Code terminal, run:**
```bash
adb devices
```

**You should see:**
```
List of devices attached
ABC123456789    device
```

✅ If you see `device` → Phone is connected correctly!  
❌ If you see `unauthorized` → Check your phone screen, approve the popup  
❌ If you see `no devices` → Check USB cable, try different port  
❌ If you see `adb not found` → ANDROID_HOME not set correctly, restart computer

---

### STEP 5.4: Install APK on Phone

**Method 1: Using ADB (Recommended)**

In VS Code terminal, run:
```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

You should see:
```
Performing Streamed Install
Success
```

✅ **Success** = App installed on your phone!

---

**Method 2: Manual File Transfer**

If ADB doesn't work:

1. Copy `app-debug.apk` to your phone:
   - Connect phone via USB
   - Copy the APK file to your phone's Downloads folder
   - Or email it to yourself
   - Or upload to Google Drive and download on phone

2. On your phone:
   - Open the Downloads folder
   - Tap on `app-debug.apk`
   - You may see "Install blocked"
   - Tap "Settings"
   - Enable "Install unknown apps" for your file manager
   - Go back and tap the APK again
   - Tap "Install"

---

## ⚙️ PART 6: CONFIGURE THE APP (5 minutes)

### STEP 6.1: Launch the App

1. On your phone, find "Private Notifications" app
2. Tap to open it

---

### STEP 6.2: Grant Notification Access Permission

**This is THE MOST IMPORTANT STEP!**

1. You'll see the main screen with a button: **"Grant Notification Access"**
2. Tap **"Grant Notification Access"**
3. You'll be taken to Android Settings → Notification Access
4. Find **"Private Notifications"** in the list
5. Tap on it
6. Toggle it **ON**
7. Tap **"Allow"** on the confirmation popup
8. Press **Back** button to return to the app

---

### STEP 6.3: Select Apps to Monitor

1. In the app, tap **"Select Apps"**
2. You'll see a list of all your installed apps
3. **Check the apps** you want to monitor (e.g., WhatsApp, Gmail, Telegram)
4. Tap **"Save"** at the bottom

---

### STEP 6.4: Enable Privacy Mode

1. On the main screen, toggle **"Enable Privacy Mode"** to **ON**
2. You should see:
   - Status: "✓ Privacy mode is ON" (in green)
   - A persistent notification: "Privacy Mode Active"

---

## 🧪 PART 7: TEST THE APP (2 minutes)

### TEST 1: Face-Down Hiding

1. Place your phone **face-down** on a table (screen touching the table)
2. Send yourself a WhatsApp message (or any app you selected)
3. **Expected:** 
   - You should NOT see/hear the notification
   - You should feel a single short vibration (notification hidden)

✅ If notification is hidden → It's working!

---

### TEST 2: Face-Up Restoration

1. Flip your phone **face-up** (screen facing you)
2. **Expected:**
   - The notification should appear immediately
   - You should feel a double vibration (notifications restored)

✅ If notification appears → Perfect! App is working correctly!

---

## 🎉 SUCCESS! YOU'RE DONE!

Congratulations! You have successfully:
- ✅ Installed all prerequisites
- ✅ Opened project in VS Code
- ✅ Downloaded all dependencies
- ✅ Built the APK
- ✅ Installed app on your phone
- ✅ Configured the app
- ✅ Tested it successfully

---

## 🚨 TROUBLESHOOTING

### Problem: "ANDROID_HOME is not set"

**Solution:**
1. Make sure you set ANDROID_HOME (see Step 1.3)
2. **Restart your computer** (very important!)
3. Open a NEW terminal and verify:
   ```bash
   echo $ANDROID_HOME  # Mac/Linux
   echo %ANDROID_HOME%  # Windows
   ```

---

### Problem: "SDK location not found"

**Solution:**
Create a file called `local.properties` in the project root:

```properties
sdk.dir=C:\\Users\\YourName\\AppData\\Local\\Android\\Sdk
```

(Replace with your actual SDK path, use double backslashes on Windows)

---

### Problem: "gradlew: Permission denied" (Mac/Linux)

**Solution:**
```bash
chmod +x gradlew
./gradlew assembleDebug
```

---

### Problem: Build fails with "Could not find or load main class"

**Solution:**
```bash
# Delete gradle wrapper and reinstall
rm -rf .gradle
./gradlew wrapper --gradle-version=8.0
./gradlew clean assembleDebug
```

---

### Problem: "adb: command not found"

**Solution:**
1. Make sure ANDROID_HOME is set correctly
2. Add platform-tools to PATH:
   ```bash
   # Add to ~/.bashrc or ~/.zshrc
   export PATH=$PATH:$ANDROID_HOME/platform-tools
   ```
3. Restart terminal
4. Or use full path:
   ```bash
   $ANDROID_HOME/platform-tools/adb devices
   ```

---

### Problem: Phone shows "unauthorized" in adb devices

**Solution:**
1. Disconnect phone
2. Run: `adb kill-server`
3. Run: `adb start-server`
4. Reconnect phone
5. Check phone screen for USB debugging popup
6. Tap "OK" to allow

---

### Problem: App crashes on launch

**Solution:**
1. Check Android version (must be 7.0+)
2. View logs:
   ```bash
   adb logcat | grep PrivateNotifications
   ```
3. Reinstall:
   ```bash
   adb uninstall com.privatenotifications
   adb install app/build/outputs/apk/debug/app-debug.apk
   ```

---

### Problem: Notifications not hiding

**Solution:**
1. Make sure you granted Notification Access permission
2. Check that the app is selected in "Select Apps"
3. Ensure Privacy Mode toggle is ON
4. Place phone completely flat face-down
5. Check logs:
   ```bash
   adb logcat -s GyroNotificationService
   ```

---

## 📞 NEED MORE HELP?

1. **Read the documentation:**
   - `BUILD_GUIDE.md` - Detailed build instructions
   - `TESTING_GUIDE.md` - Testing procedures
   - `README.md` - General overview

2. **Check logs:**
   ```bash
   adb logcat -s GyroNotificationService
   ```

3. **Rebuild from scratch:**
   ```bash
   ./gradlew clean
   ./gradlew assembleDebug --refresh-dependencies
   ```

---

## 📋 QUICK COMMAND REFERENCE

```bash
# Build APK
./gradlew assembleDebug

# Clean and rebuild
./gradlew clean assembleDebug

# Install on phone
adb install -r app/build/outputs/apk/debug/app-debug.apk

# Check connected devices
adb devices

# View logs
adb logcat -s GyroNotificationService

# Uninstall app
adb uninstall com.privatenotifications
```

---

## 🎯 WHAT'S NEXT?

Now that you have the app working:

1. **Customize it:**
   - Change colors in `app/src/main/res/values/colors.xml`
   - Modify sensitivity in `GyroNotificationService.kt`
   - Change app name in `app/src/main/res/values/strings.xml`

2. **Build release APK:**
   ```bash
   ./gradlew assembleRelease
   ```

3. **Push to GitHub:**
   ```bash
   git init
   git add .
   git commit -m "Initial commit"
   git remote add origin YOUR_REPO_URL
   git push -u origin main
   ```

4. **Share with friends!**

---

**YOU DID IT! 🎉**

Your Private Notifications app is now running on your phone!

Enjoy your privacy-enhanced notifications! 🔒📱
