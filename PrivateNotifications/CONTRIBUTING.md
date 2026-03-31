# Contributing to Private Notifications

Thank you for considering contributing to Private Notifications! This document provides guidelines for contributing to the project.

## Table of Contents

1. [Code of Conduct](#code-of-conduct)
2. [How Can I Contribute?](#how-can-i-contribute)
3. [Development Setup](#development-setup)
4. [Coding Standards](#coding-standards)
5. [Commit Guidelines](#commit-guidelines)
6. [Pull Request Process](#pull-request-process)
7. [Reporting Bugs](#reporting-bugs)
8. [Suggesting Features](#suggesting-features)

## Code of Conduct

This project follows a simple code of conduct:

- **Be respectful**: Treat everyone with respect and kindness
- **Be constructive**: Provide helpful, actionable feedback
- **Be collaborative**: Work together towards common goals
- **Be inclusive**: Welcome contributors of all backgrounds and skill levels

## How Can I Contribute?

### 1. Reporting Bugs

Before creating bug reports, please check existing issues to avoid duplicates.

**Good bug reports include:**
- Clear, descriptive title
- Device and Android version
- Steps to reproduce
- Expected vs actual behavior
- Logs from `adb logcat`
- Screenshots (if applicable)

### 2. Suggesting Features

Feature suggestions are welcome! Please:
- Check if the feature already exists or is planned
- Provide clear use cases
- Explain the benefits
- Consider implementation complexity

### 3. Code Contributions

Contributions in these areas are especially welcome:
- Bug fixes
- Performance improvements
- UI/UX enhancements
- Test coverage
- Documentation improvements
- Translations/localization

## Development Setup

### Prerequisites

1. Java JDK 11+
2. Android SDK (API 24-34)
3. Git
4. VS Code or Android Studio

### Fork and Clone

```bash
# Fork the repository on GitHub, then:
git clone https://github.com/YOUR_USERNAME/PrivateNotifications.git
cd PrivateNotifications

# Add upstream remote
git remote add upstream https://github.com/ORIGINAL_OWNER/PrivateNotifications.git
```

### Build and Test

```bash
# Build debug APK
./gradlew assembleDebug

# Run unit tests (when available)
./gradlew test

# Install on device
adb install app/build/outputs/apk/debug/app-debug.apk
```

### Project Structure

```
app/src/main/
├── java/com/privatenotifications/
│   ├── MainActivity.kt              # Main UI
│   ├── AppSelectionActivity.kt      # App selection UI
│   ├── GyroNotificationService.kt   # Core service
│   └── NotificationData.kt          # Data model
├── res/
│   ├── layout/                      # UI layouts
│   ├── values/                      # Strings, colors, themes
│   └── xml/                         # Backup rules
└── AndroidManifest.xml              # App configuration
```

## Coding Standards

### Kotlin Style Guide

Follow [Kotlin coding conventions](https://kotlinlang.org/docs/coding-conventions.html):

```kotlin
// Good
class MyClass {
    private val myProperty: String = "value"
    
    fun myFunction(param: Int): String {
        return "result"
    }
}

// Bad
class myClass {
    private val my_property:String="value"
    
    fun MyFunction(param:Int):String{
        return "result"
    }
}
```

### Naming Conventions

- **Classes**: PascalCase (`MainActivity`, `NotificationData`)
- **Functions**: camelCase (`hideNotification`, `restoreAll`)
- **Variables**: camelCase (`isFaceDown`, `selectedApps`)
- **Constants**: SCREAMING_SNAKE_CASE (`FACE_DOWN_THRESHOLD`, `TAG`)
- **Resources**: snake_case (`activity_main.xml`, `app_icon.png`)

### Documentation

Add KDoc comments for public APIs:

```kotlin
/**
 * Hides a notification and stores it for later restoration.
 * 
 * @param sbn The StatusBarNotification to hide
 * @throws SecurityException if notification access is not granted
 */
private fun hideNotification(sbn: StatusBarNotification) {
    // Implementation
}
```

### Code Organization

- Keep functions focused and single-purpose
- Extract complex logic into separate functions
- Use meaningful variable names
- Avoid deep nesting (max 3 levels)
- Handle errors gracefully

## Commit Guidelines

### Commit Message Format

```
<type>(<scope>): <subject>

<body>

<footer>
```

**Types:**
- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation changes
- `style`: Code formatting (no logic change)
- `refactor`: Code restructuring
- `test`: Adding tests
- `chore`: Build/config changes

**Examples:**

```
feat(service): add haptic feedback on notification hide

Added single vibration pulse when notification is hidden
and double pulse when notifications are restored.

Closes #42
```

```
fix(ui): prevent crash when permission is revoked

Handle null pointer exception when notification access
is revoked while app is running.

Fixes #38
```

### Commit Best Practices

- Write clear, concise commit messages
- Reference issue numbers when applicable
- Keep commits atomic (one logical change per commit)
- Test before committing

## Pull Request Process

### Before Submitting

1. **Update from upstream:**
   ```bash
   git fetch upstream
   git rebase upstream/main
   ```

2. **Test thoroughly:**
   - Build succeeds: `./gradlew assembleDebug`
   - App runs without crashes
   - Changes work as expected
   - No new warnings in logs

3. **Update documentation:**
   - Update README.md if needed
   - Add/update code comments
   - Update CHANGELOG.md

### Creating the PR

1. **Push to your fork:**
   ```bash
   git push origin feature/my-feature
   ```

2. **Create PR on GitHub** with:
   - Clear title describing the change
   - Description of what changed and why
   - Steps to test the changes
   - Screenshots (for UI changes)
   - Reference to related issues

3. **PR Template:**
   ```markdown
   ## Description
   Brief description of changes
   
   ## Motivation
   Why is this change needed?
   
   ## Changes Made
   - List of specific changes
   - Another change
   
   ## Testing
   How to test these changes:
   1. Step one
   2. Step two
   
   ## Screenshots
   (if applicable)
   
   ## Checklist
   - [ ] Code follows style guidelines
   - [ ] Self-review completed
   - [ ] Comments added for complex code
   - [ ] Documentation updated
   - [ ] No new warnings
   - [ ] Tested on physical device
   
   Closes #issue_number
   ```

### Review Process

- Maintainers will review your PR
- Address feedback promptly
- Be open to suggestions
- Keep discussions constructive

### After Merge

- Delete your feature branch
- Update your fork:
  ```bash
  git checkout main
  git pull upstream main
  git push origin main
  ```

## Reporting Bugs

Use the GitHub issue tracker with this template:

```markdown
**Describe the bug**
A clear description of the bug.

**To Reproduce**
Steps to reproduce:
1. Go to '...'
2. Click on '...'
3. See error

**Expected behavior**
What you expected to happen.

**Actual behavior**
What actually happened.

**Screenshots**
If applicable, add screenshots.

**Device Information:**
- Device: [e.g. Pixel 7]
- Android Version: [e.g. 14]
- App Version: [e.g. 1.0.0]

**Logs**
```
Paste relevant logcat output here
```

**Additional context**
Any other relevant information.
```

## Suggesting Features

Use the GitHub issue tracker with this template:

```markdown
**Feature Description**
Clear description of the proposed feature.

**Use Case**
Why is this feature needed? What problem does it solve?

**Proposed Solution**
How should this feature work?

**Alternatives Considered**
Other solutions you've considered.

**Additional Context**
Mockups, examples, or other relevant information.
```

## Questions?

Feel free to:
- Open a discussion on GitHub
- Ask in pull request comments
- Contact maintainers

---

Thank you for contributing to Private Notifications! 🎉
