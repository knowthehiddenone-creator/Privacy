# Private Notifications - File Index

Complete index of all files in this project.

## 📁 Project Root

| File | Description |
|------|-------------|
| `README.md` | Main documentation and overview |
| `BUILD_GUIDE.md` | Detailed build instructions |
| `TESTING_GUIDE.md` | Complete testing procedures |
| `QUICKSTART.md` | 5-minute quick setup guide |
| `CONTRIBUTING.md` | Contribution guidelines |
| `CHANGELOG.md` | Version history |
| `LICENSE` | MIT License |
| `PROJECT_SUMMARY.md` | Complete project summary |
| `INDEX.md` | This file |
| `build.sh` | Unix/Mac build automation script |
| `build.bat` | Windows build automation script |
| `.gitignore` | Git ignore rules |

## 📁 Build Configuration

| File | Description |
|------|-------------|
| `build.gradle` | Root Gradle build file |
| `settings.gradle` | Gradle settings |
| `gradle.properties` | Gradle properties |
| `gradlew` | Gradle wrapper (Unix/Mac) |
| `gradlew.bat` | Gradle wrapper (Windows) |

## 📁 app/

| File | Description |
|------|-------------|
| `build.gradle` | App module build configuration |
| `proguard-rules.pro` | ProGuard rules for release builds |

## 📁 app/src/main/

| File | Description |
|------|-------------|
| `AndroidManifest.xml` | App manifest with permissions |

## 📁 app/src/main/java/com/privatenotifications/

| File | Lines | Description |
|------|-------|-------------|
| `MainActivity.kt` | ~200 | Main UI activity with controls |
| `AppSelectionActivity.kt` | ~150 | App selection interface |
| `GyroNotificationService.kt` | ~400 | Core notification service |
| `NotificationData.kt` | ~30 | Notification data model |

### Key Classes

**MainActivity**
- Service status display
- Enable/disable toggle
- Permission handling
- Navigation to app selection

**AppSelectionActivity**
- Lists all installed apps
- Checkbox selection
- Saves to SharedPreferences
- RecyclerView adapter

**GyroNotificationService**
- NotificationListenerService
- SensorEventListener
- Accelerometer monitoring
- Notification hiding/restoring
- Foreground service

**NotificationData**
- Stores hidden notification info
- Enables restoration

## 📁 app/src/main/res/layout/

| File | Description |
|------|-------------|
| `activity_main.xml` | Main activity layout |
| `activity_app_selection.xml` | App selection layout |
| `item_app_selection.xml` | RecyclerView item layout |

## 📁 app/src/main/res/values/

| File | Description |
|------|-------------|
| `strings.xml` | String resources |
| `colors.xml` | Color palette |
| `themes.xml` | Material Design 3 themes |

## 📁 app/src/main/res/xml/

| File | Description |
|------|-------------|
| `backup_rules.xml` | Backup configuration |
| `data_extraction_rules.xml` | Data extraction rules |

## 📁 gradle/wrapper/

| File | Description |
|------|-------------|
| `gradle-wrapper.properties` | Gradle wrapper configuration |

## 📁 .vscode/

| File | Description |
|------|-------------|
| `tasks.json` | VS Code build tasks |
| `settings.json` | VS Code editor settings |

## File Statistics

### Source Code
- **Kotlin files**: 4
- **Total lines of code**: ~780
- **XML layouts**: 3
- **Resource files**: 5

### Documentation
- **Markdown files**: 9
- **Total documentation**: ~2000 lines

### Configuration
- **Gradle files**: 5
- **VS Code files**: 2

## Quick Access

### Most Important Files

**For Building:**
1. `gradlew` / `gradlew.bat` - Build wrapper
2. `app/build.gradle` - Build configuration
3. `build.sh` / `build.bat` - Automated build

**For Development:**
1. `GyroNotificationService.kt` - Core logic
2. `MainActivity.kt` - Main UI
3. `AndroidManifest.xml` - Permissions

**For Documentation:**
1. `README.md` - Start here
2. `QUICKSTART.md` - Fast setup
3. `BUILD_GUIDE.md` - Detailed build info

### Entry Points

**To understand the app:**
1. Read `PROJECT_SUMMARY.md`
2. Review `GyroNotificationService.kt`
3. Check `MainActivity.kt`

**To build the app:**
1. Read `QUICKSTART.md`
2. Run `./build.sh` or `build.bat`
3. Install APK

**To modify the app:**
1. Read `CONTRIBUTING.md`
2. Modify Kotlin files
3. Test on device

## File Organization Principles

- **Source code**: `app/src/main/java/`
- **Layouts**: `app/src/main/res/layout/`
- **Resources**: `app/src/main/res/values/`
- **Documentation**: Root directory (`.md` files)
- **Build scripts**: Root directory
- **Configuration**: Root directory (Gradle) and `.vscode/`

## Total Project Size

**Estimated sizes:**
- Source code: ~50 KB
- Resources: ~20 KB
- Documentation: ~150 KB
- Build files: ~10 KB
- Total (excluding build outputs): ~230 KB

**Build outputs** (not in repository):
- Debug APK: ~5-8 MB
- Release APK: ~3-5 MB

## Navigation Guide

```
Starting point: README.md
     │
     ├─> Want to build? → QUICKSTART.md → build.sh
     ├─> Need details? → BUILD_GUIDE.md
     ├─> Want to test? → TESTING_GUIDE.md
     ├─> Want to contribute? → CONTRIBUTING.md
     ├─> Need overview? → PROJECT_SUMMARY.md
     └─> Want to understand code? → GyroNotificationService.kt
```

---

**All files are ready to use!** 
Copy the entire `PrivateNotifications` directory to your development machine and start building.
