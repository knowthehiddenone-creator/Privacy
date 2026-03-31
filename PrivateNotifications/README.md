# Private Notifications - Galaxy-Style Notification Privacy

An Android app that automatically hides notifications when your phone is face-down, using the device's accelerometer sensor. Perfect for meetings, presentations, or anytime you want instant notification privacy.

## Features

✨ **Automatic Notification Hiding** - Notifications are hidden when phone is face-down
🔄 **Auto-Restore** - Notifications reappear when phone is face-up
📱 **Per-App Control** - Choose which apps to monitor (WhatsApp, Gmail, etc.)
🎯 **Samsung Galaxy-Style** - Similar behavior to Samsung's private mode
⚡ **Lightweight** - Uses minimal battery with efficient sensor monitoring
🔒 **Privacy-Focused** - All processing happens on-device

## Project Structure

```
PrivateNotifications/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/privatenotifications/
│   │   │   │   ├── MainActivity.kt
│   │   │   │   ├── GyroNotificationService.kt
│   │   │   │   ├── AppSelectionActivity.kt
│   │   │   │   └── NotificationData.kt
│   │   │   ├── res/
│   │   │   │   ├── layout/
│   │   │   │   │   ├── activity_main.xml
│   │   │   │   │   ├── activity_app_selection.xml
│   │   │   │   │   └── item_app_selection.xml
│   │   │   │   ├── values/
│   │   │   │   │   ├── strings.xml
│   │   │   │   │   ├── colors.xml
│   │   │   │   │   └── themes.xml
│   │   │   │   └── drawable/
│   │   │   │       └── ic_launcher_background.xml
│   │   │   └── AndroidManifest.xml
│   │   └── test/ (unit tests)
│   ├── build.gradle
│   └── proguard-rules.pro
├── gradle/
│   └── wrapper/
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── build.gradle
├── settings.gradle
├── gradle.properties
├── gradlew
├── gradlew.bat
├── .gitignore
└── README.md
```

## Prerequisites

- **Android Studio** or **VS Code** with Android extensions
- **Java Development Kit (JDK) 11** or higher
- **Android SDK** (API Level 24 or higher)
- **Gradle 7.0+** (included in wrapper)

## Setup Instructions

### 1. Clone or Download the Project

```bash
git clone https://github.com/yourusername/PrivateNotifications.git
cd PrivateNotifications
```

### 2. Setup in VS Code

1. Install the following VS Code extensions:
   - **Android IDE** (extension ID: `adelphes.android-dev-ext`)
   - **Kotlin Language** (extension ID: `mathiasfrohlich.Kotlin`)
   - **Gradle for Java** (extension ID: `vscjava.vscode-gradle`)

2. Open the project folder in VS Code:
   ```bash
   code .
   ```

3. Set ANDROID_HOME environment variable:
   - **Windows**: `set ANDROID_HOME=C:\Users\YourUsername\AppData\Local\Android\Sdk`
   - **Mac/Linux**: `export ANDROID_HOME=$HOME/Android/Sdk`

### 3. Build the APK

#### Using Gradle Command Line (Recommended)

```bash
# Make gradlew executable (Mac/Linux only)
chmod +x gradlew

# Clean build
./gradlew clean

# Build debug APK
./gradlew assembleDebug

# Build release APK (unsigned)
./gradlew assembleRelease
```

The APK will be generated at:
- **Debug**: `app/build/outputs/apk/debug/app-debug.apk`
- **Release**: `app/build/outputs/apk/release/app-release-unsigned.apk`

#### Using VS Code Tasks

Press `Ctrl+Shift+P` (or `Cmd+Shift+P` on Mac) → "Tasks: Run Task" → "assembleDebug"

### 4. Install on Device

#### Via USB (ADB)

```bash
# Enable USB debugging on your Android device first
adb install app/build/outputs/apk/debug/app-debug.apk
```

#### Via File Transfer

1. Copy `app-debug.apk` to your phone
2. Open the APK file on your device
3. Allow installation from unknown sources if prompted
4. Install the app

## App Configuration

### First-Time Setup

1. **Launch the app** on your Android device
2. **Grant Notification Access**:
   - Tap "Enable Notification Access"
   - Find "Private Notifications" in the list
   - Toggle it ON
   - Tap "Allow"
3. **Select Apps**:
   - Tap "Select Apps to Monitor"
   - Choose apps like WhatsApp, Gmail, Telegram, etc.
   - Tap "Save"
4. **Enable Service**:
   - Toggle "Enable Privacy Mode" to ON
   - The service will start monitoring

### Testing the App

1. **Face-Down Test**:
   - Place your phone face-down on a flat surface
   - Send yourself a test notification (WhatsApp message, email, etc.)
   - You should NOT see/hear the notification
   
2. **Face-Up Test**:
   - Flip your phone face-up
   - The hidden notification should now appear
   - You should hear/see the notification sound/vibration

3. **Per-App Test**:
   - Only apps you selected should be hidden
   - Other apps will show notifications normally

## How It Works

### Sensor Detection

The app uses the **accelerometer sensor** to detect phone orientation:

- **Z-axis value < -8.0**: Phone is face-down → Hide notifications
- **Z-axis value > 8.0**: Phone is face-up → Restore notifications
- **Sampling**: Checks every 500ms to balance battery and responsiveness

### Notification Handling

1. **NotificationListenerService** monitors all incoming notifications
2. When a notification arrives:
   - Check if phone is face-down
   - Check if app is in the monitored list
   - If both true: Cancel the notification and store it
3. When phone is flipped face-up:
   - Restore all hidden notifications
   - Show them with original content and actions

### Privacy & Permissions

- **BIND_NOTIFICATION_LISTENER_SERVICE**: Required to intercept notifications
- **VIBRATE**: Optional, for haptic feedback
- All data is stored locally using SharedPreferences
- No internet permissions - completely offline

## Building for Production

### Generate Signed APK

1. Create a keystore:
   ```bash
   keytool -genkey -v -keystore my-release-key.jks -keyalg RSA -keysize 2048 -validity 10000 -alias my-alias
   ```

2. Add to `app/build.gradle`:
   ```gradle
   android {
       signingConfigs {
           release {
               storeFile file("my-release-key.jks")
               storePassword "your-password"
               keyAlias "my-alias"
               keyPassword "your-password"
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

3. Build release APK:
   ```bash
   ./gradlew assembleRelease
   ```

## Troubleshooting

### Notifications Not Hiding

- Ensure Notification Access is granted in Settings
- Check that the app is in the selected apps list
- Verify sensor is working: Place phone flat face-down

### Service Stops After Time

- Disable battery optimization for this app
- Settings → Battery → Battery Optimization → All Apps → Private Notifications → Don't optimize

### Build Errors

- **SDK not found**: Set ANDROID_HOME environment variable
- **Gradle sync failed**: Run `./gradlew clean build`
- **Dependencies error**: Check internet connection, update `build.gradle` versions

### Permission Denied (gradlew)

```bash
chmod +x gradlew
./gradlew clean build
```

## Technical Details

- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 34 (Android 14)
- **Language**: Kotlin 1.9+
- **Build System**: Gradle 8.0+
- **Architecture**: Service-based with foreground service for reliability

## Contributing

1. Fork the repository
2. Create your feature branch: `git checkout -b feature/amazing-feature`
3. Commit your changes: `git commit -m 'Add amazing feature'`
4. Push to the branch: `git push origin feature/amazing-feature`
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments

- Inspired by Samsung Galaxy private notification features
- Built with Android Jetpack components
- Uses Material Design 3 guidelines

## Support

For issues, questions, or contributions:
- Open an issue on GitHub
- Email: support@privatenotifications.dev

---

**Made with ❤️ for privacy-conscious Android users**
