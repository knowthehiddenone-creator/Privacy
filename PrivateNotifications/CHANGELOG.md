# Changelog

All notable changes to the Private Notifications project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.0.0] - 2024-03-30

### Added
- Initial release of Private Notifications app
- Accelerometer-based notification hiding when phone is face-down
- Automatic notification restoration when phone is face-up
- Per-app notification filtering (select specific apps to monitor)
- Material Design 3 user interface
- Foreground service for reliable background operation
- Haptic feedback (single vibration on hide, double on restore)
- Real-time orientation detection using accelerometer sensor
- SharedPreferences for persistent app selection storage
- Settings activity for app selection with search capability
- Notification access permission flow with user-friendly dialogs
- First-time setup wizard
- Detailed logging for debugging
- Battery-optimized sensor sampling (500ms intervals)
- Support for Android 7.0 (API 24) and above
- Complete VS Code integration with tasks and settings
- Comprehensive documentation (README, BUILD_GUIDE, TESTING_GUIDE)

### Features
- **Privacy Mode Toggle**: Enable/disable notification hiding
- **App Selection**: Choose which apps to monitor
- **Status Display**: Visual indication of service state
- **Orientation Detection**: Z-axis threshold detection (±8.0)
- **Notification Storage**: Temporary storage of hidden notifications
- **Auto-Restoration**: Automatic notification recovery on orientation change

### Technical Details
- Kotlin 1.9.20
- Android Gradle Plugin 8.1.2
- Minimum SDK: 24 (Android 7.0)
- Target SDK: 34 (Android 14)
- Compile SDK: 34

### Dependencies
- AndroidX Core KTX 1.12.0
- AndroidX AppCompat 1.6.1
- Material Components 1.10.0
- ConstraintLayout 2.1.4
- Lifecycle Runtime KTX 2.6.2
- RecyclerView 1.3.2

### Known Issues
- None at release

### Security
- All data stored locally (no internet permissions)
- No data collection or analytics
- Notification content never transmitted
- Settings backed up with Android Auto Backup (can be disabled)

---

## [Unreleased]

### Planned Features
- Whitelist mode (hide all except selected apps)
- Custom sensitivity adjustment for orientation detection
- Schedule-based privacy mode (auto-enable during meetings)
- Notification preview in-app
- Export/import settings
- Multi-device sync (optional)
- Dark theme support
- Landscape mode optimization
- Widget for quick toggle
- Tasker integration
- Alternative trigger methods (proximity sensor, face recognition)

### Future Improvements
- Unit tests for core functionality
- UI automation tests
- Continuous integration setup
- F-Droid distribution
- Play Store listing
- Localization (multiple languages)
- Accessibility improvements

---

## Version History

- **v1.0.0** (2024-03-30): Initial release

---

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md) for contribution guidelines.

## Support

For issues and feature requests, please use the [GitHub issue tracker](https://github.com/yourusername/PrivateNotifications/issues).
