# Testing Guide - Private Notifications App

Complete guide to testing the gyroscope-based notification privacy features.

## Pre-Testing Checklist

Before testing, ensure:

- [ ] App is installed on physical Android device (API 24+)
- [ ] Notification access permission is granted
- [ ] At least one app is selected for monitoring
- [ ] Privacy mode is enabled in the app
- [ ] Device has working accelerometer sensor

**Note:** This app requires a physical device with accelerometer. Emulators may not accurately simulate sensor behavior.

## Test Scenarios

### Test 1: Basic Face-Down Detection

**Objective:** Verify notifications are hidden when phone is face-down

**Steps:**
1. Open the app and enable privacy mode
2. Select WhatsApp (or any messaging app) in app selection
3. Exit the app (press home button)
4. Place phone flat on table, **face-up** (screen facing you)
5. Send yourself a WhatsApp message from another device
6. **Expected:** Notification appears normally (sound + vibration + status bar)
7. Dismiss the notification
8. Now flip phone **face-down** (screen facing table)
9. Send yourself another WhatsApp message
10. **Expected:** 
    - No notification sound/vibration
    - No notification in status bar
    - Single short vibration pulse (haptic feedback)

**Pass Criteria:**
- ✅ Face-up notifications work normally
- ✅ Face-down notifications are hidden
- ✅ Haptic feedback occurs when notification is hidden

### Test 2: Face-Up Restoration

**Objective:** Verify hidden notifications are restored when phone is flipped face-up

**Steps:**
1. Phone is face-down with privacy mode enabled
2. Send yourself 3 WhatsApp messages (while phone is still face-down)
3. **Expected:** No notifications appear
4. Flip phone **face-up**
5. **Expected:**
    - All 3 notifications appear immediately
    - Double vibration pulse (haptic feedback)
    - Notifications show in status bar

**Pass Criteria:**
- ✅ All hidden notifications are restored
- ✅ Notifications appear with original content
- ✅ Double haptic feedback occurs

### Test 3: Per-App Selection

**Objective:** Verify only selected apps have notifications hidden

**Steps:**
1. In app selection, select only WhatsApp (deselect all others)
2. Enable privacy mode
3. Place phone face-down
4. Send yourself a WhatsApp message
5. **Expected:** Notification is hidden ✅
6. Send yourself a Gmail email (or any non-selected app)
7. **Expected:** Gmail notification appears normally ✅

**Pass Criteria:**
- ✅ Selected apps have notifications hidden
- ✅ Non-selected apps show notifications normally

### Test 4: Orientation Change Speed

**Objective:** Verify detection responds quickly to orientation changes

**Steps:**
1. Phone face-up, privacy mode enabled
2. Quickly flip to face-down
3. Within 2 seconds, send a notification
4. **Expected:** Notification is hidden

**Pass Criteria:**
- ✅ Orientation detected within ~500ms
- ✅ Service updates quickly enough to hide incoming notification

### Test 5: Multiple Notifications

**Objective:** Test handling of rapid successive notifications

**Steps:**
1. Phone face-down
2. Send 5 notifications rapidly (within 10 seconds)
3. **Expected:** All 5 are hidden
4. Flip phone face-up
5. **Expected:** All 5 notifications appear

**Pass Criteria:**
- ✅ All notifications are captured and hidden
- ✅ All notifications are restored correctly
- ✅ No crashes or missed notifications

### Test 6: Service Persistence

**Objective:** Verify service continues running in background

**Steps:**
1. Enable privacy mode
2. Use phone normally for 30 minutes
3. Check notification shade - "Privacy Mode Active" should be visible
4. Place phone face-down and test hiding notifications
5. **Expected:** Still works after extended use

**Pass Criteria:**
- ✅ Service notification remains visible
- ✅ Functionality works after extended runtime
- ✅ No crashes or service stops

### Test 7: Battery Optimization

**Objective:** Test service survives aggressive battery optimization

**Steps:**
1. Go to Settings → Battery → Battery Optimization
2. Find "Private Notifications"
3. Select "Optimize" (not recommended, but testing worst case)
4. Leave phone idle for 1 hour
5. Test notification hiding
6. **Expected:** May not work reliably due to OS killing service

**Recommended:**
- Set battery optimization to "Don't optimize" for reliable operation

### Test 8: Permission Revocation

**Objective:** Test app behavior when permission is revoked

**Steps:**
1. Privacy mode is enabled
2. Go to Settings → Notifications → Notification Access
3. Disable "Private Notifications"
4. Return to app
5. **Expected:**
    - Status shows warning
    - Toggle is disabled
    - Prompt to re-grant permission

**Pass Criteria:**
- ✅ App handles permission loss gracefully
- ✅ UI updates to show permission required
- ✅ No crashes

### Test 9: Extreme Orientations

**Objective:** Test detection accuracy in various positions

**Test Positions:**
1. **Flat face-up:** Z ≈ +9.8 → Notifications visible ✅
2. **Flat face-down:** Z ≈ -9.8 → Notifications hidden ✅
3. **Vertical (portrait):** Z ≈ 0 → Notifications visible ✅
4. **Tilted 45°:** Z ≈ ±7 → Notifications visible ✅
5. **In pocket (vertical):** Z ≈ 0 → Notifications visible ✅

**Pass Criteria:**
- ✅ Only flat face-down position hides notifications
- ✅ All other positions allow notifications

### Test 10: App Reinstallation

**Objective:** Verify settings persist after app updates

**Steps:**
1. Configure app (select apps, enable mode)
2. Note selected apps
3. Reinstall app (adb install -r)
4. Open app
5. **Expected:** Settings are preserved

**Note:** If using fresh install (not -r), settings will reset.

## Logging and Debugging

### Enable Detailed Logging

View real-time logs to debug issues:

```bash
# View all app logs
adb logcat -s PrivateNotifications

# View service logs only
adb logcat -s GyroNotificationService

# View with timestamps
adb logcat -v time -s GyroNotificationService

# Save logs to file
adb logcat -s GyroNotificationService > logs.txt
```

### Key Log Messages

**Service Started:**
```
D/GyroNotificationService: Service created
D/GyroNotificationService: Accelerometer listener registered
D/GyroNotificationService: Notification listener connected
```

**Orientation Detection:**
```
D/GyroNotificationService: Phone is now FACE-DOWN (Z=-9.2)
D/GyroNotificationService: Phone is now FACE-UP (Z=9.5)
```

**Notification Handling:**
```
D/GyroNotificationService: Notification posted from: com.whatsapp
D/GyroNotificationService: Phone is face-down, hiding notification from com.whatsapp
D/GyroNotificationService: Hidden notification: key123 from com.whatsapp
D/GyroNotificationService: Restoring 3 notifications
D/GyroNotificationService: Restored notification from com.whatsapp
```

### Sensor Values

Monitor real-time sensor data:

```bash
adb shell dumpsys sensorservice
```

Check accelerometer readings:
```bash
# Live sensor data
adb shell "cat /sys/bus/iio/devices/iio:device*/in_accel_*"
```

## Performance Testing

### Battery Impact Test

1. Install battery monitoring app (like AccuBattery)
2. Enable privacy mode
3. Monitor battery drain over 24 hours
4. **Expected:** <2% additional battery drain per day

### Memory Usage Test

```bash
# Check memory usage
adb shell dumpsys meminfo com.privatenotifications

# Monitor in real-time
adb shell top | grep privatenotifications
```

**Expected:**
- Memory usage: <50 MB
- CPU usage: <1% (when idle)

## Automated Testing Script

Create `test.sh` for automated testing:

```bash
#!/bin/bash

echo "=== Private Notifications Test Suite ==="

# Check if device is connected
if ! adb devices | grep -q "device$"; then
    echo "❌ No device connected"
    exit 1
fi

echo "✅ Device connected"

# Check if app is installed
if ! adb shell pm list packages | grep -q "com.privatenotifications"; then
    echo "❌ App not installed"
    exit 1
fi

echo "✅ App installed"

# Check notification access
if ! adb shell cmd notification allow_listener com.privatenotifications/com.privatenotifications.GyroNotificationService; then
    echo "⚠️  Notification access may not be granted"
fi

echo "✅ Permissions checked"

# Launch app
adb shell am start -n com.privatenotifications/.MainActivity
echo "✅ App launched"

# Monitor logs
echo "📋 Monitoring logs (Ctrl+C to stop)..."
adb logcat -c
adb logcat -s GyroNotificationService
```

## Troubleshooting Common Issues

### Issue: Notifications Not Hiding

**Debug Steps:**
1. Check logs for "Notification posted" message
2. Verify app is in selected apps list
3. Check accelerometer Z-axis value (should be < -8.0)
4. Ensure service is running (check persistent notification)

**Solutions:**
- Re-grant notification access
- Ensure privacy mode toggle is ON
- Check if app is selected in app selection
- Restart the service (toggle privacy mode off/on)

### Issue: Notifications Not Restoring

**Debug Steps:**
1. Check logs for "Restoring N notifications"
2. Verify Z-axis value > 8.0 when face-up

**Solutions:**
- Ensure phone is completely flat face-up
- Check if notifications were actually hidden first
- Restart app and try again

### Issue: Service Stops

**Debug Steps:**
1. Check battery optimization settings
2. Look for crash logs: `adb logcat *:E`
3. Check if permission was revoked

**Solutions:**
- Disable battery optimization for the app
- Re-grant notification access
- Check Android version compatibility (needs 7.0+)

## Success Criteria

App is ready for production when:

- ✅ All 10 test scenarios pass
- ✅ No crashes in 24 hours of use
- ✅ Battery impact <2% per day
- ✅ Memory usage <50 MB
- ✅ Notification detection <500ms latency
- ✅ Service survives overnight
- ✅ Works with at least 5 different apps

## Reporting Issues

When reporting bugs, include:

1. Device model and Android version
2. Full logcat output: `adb logcat -d > full_log.txt`
3. Steps to reproduce
4. Expected vs actual behavior
5. Screenshots if applicable

---

**Happy Testing! 🧪**
