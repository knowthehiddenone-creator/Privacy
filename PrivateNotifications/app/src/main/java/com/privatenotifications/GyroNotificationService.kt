package com.privatenotifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import androidx.core.app.NotificationCompat

/**
 * GyroNotificationService - Core service for hiding/restoring notifications
 * 
 * This service monitors phone orientation using the accelerometer sensor.
 * When phone is face-down (Z-axis < -8.0), it hides notifications from selected apps.
 * When phone is face-up (Z-axis > 8.0), it restores all hidden notifications.
 * 
 * Key Features:
 * - Real-time accelerometer monitoring
 * - Per-app notification filtering
 * - Automatic notification restoration
 * - Foreground service for reliability
 * - Minimal battery impact with 500ms sampling rate
 */
class GyroNotificationService : NotificationListenerService(), SensorEventListener {

    companion object {
        private const val TAG = "GyroNotificationService"
        private const val PREFS_NAME = "PrivateNotificationsPrefs"
        private const val KEY_ENABLED = "service_enabled"
        private const val KEY_SELECTED_APPS = "selected_apps"
        private const val NOTIFICATION_CHANNEL_ID = "GyroNotificationServiceChannel"
        private const val FOREGROUND_NOTIFICATION_ID = 1001
        
        // Sensor thresholds for detecting phone orientation
        // Z-axis values: positive = face-up, negative = face-down
        private const val FACE_DOWN_THRESHOLD = -8.0f  // Phone facing down
        private const val FACE_UP_THRESHOLD = 8.0f     // Phone facing up
        private const val SENSOR_DELAY_MS = 500        // Check every 500ms
    }

    // Sensor management
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private var isServiceEnabled = false
    
    // Notification storage
    private val hiddenNotifications = mutableMapOf<String, NotificationData>()
    
    // Phone state tracking
    private var isFaceDown = false
    private var lastOrientation = 0f
    // 🔥 NEW: Movement detection
    private var lastX = 0f
    private var lastY = 0f
    private var lastZ = 0f
    private var isMoving = false
    // Utilities
    private lateinit var prefs: SharedPreferences
    private lateinit var vibrator: Vibrator
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Service created")
        
        // Initialize SharedPreferences
        prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        isServiceEnabled = prefs.getBoolean(KEY_ENABLED, false)
        
        // Initialize sensor manager and accelerometer
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        
        // Initialize vibrator for haptic feedback
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        
        // Start as foreground service to prevent Android from killing it
        startForegroundService()
        
        // Register accelerometer listener if service is enabled
        if (isServiceEnabled && accelerometer != null) {
            registerSensorListener()
        }
    }

    /**
     * Starts the service in foreground mode with persistent notification
     * This ensures Android doesn't kill the service in the background
     */
    private fun startForegroundService() {
        createNotificationChannel()
        
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, notificationIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        
        val notification = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Privacy Mode Active")
            .setContentText("Monitoring phone orientation")
            .setSmallIcon(android.R.drawable.ic_lock_idle_lock)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
        
        startForeground(FOREGROUND_NOTIFICATION_ID, notification)
    }

    /**
     * Creates notification channel for Android O and above
     */
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "Gyro Notification Service",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Shows that privacy mode is active"
                setShowBadge(false)
            }
            
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * Registers the accelerometer sensor listener
     * Samples at SENSOR_DELAY_NORMAL for battery efficiency
     */
    private fun registerSensorListener() {
        accelerometer?.let {
            sensorManager.registerListener(
                this,
                it,
                SensorManager.SENSOR_DELAY_NORMAL  // ~200ms delay for battery efficiency
            )
            Log.d(TAG, "Accelerometer listener registered")
        } ?: Log.e(TAG, "Accelerometer not available!")
    }

    /**
     * Unregisters the sensor listener to save battery
     */
    private fun unregisterSensorListener() {
        sensorManager.unregisterListener(this)
        Log.d(TAG, "Accelerometer listener unregistered")
    }

    /**
     * Called when new notification is posted
     * This is where we decide whether to hide the notification
     */
    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        if (sbn == null || !isServiceEnabled) return
        
        val packageName = sbn.packageName
        Log.d(TAG, "Notification posted from: $packageName")
        
        // Check if this app is in our monitored list
        if (!isAppSelected(packageName)) {
            Log.d(TAG, "App not in monitored list, allowing notification")
            return
        }
        
        // If phone is face-down, hide the notification
        if (isFaceDown) {
            Log.d(TAG, "Phone is face-down, hiding notification from $packageName")
            hideNotification(sbn)
            vibrateOnce()  // Subtle haptic feedback
        }
    }

    /**
     * Called when notification is removed
     * Clean up our hidden notifications list
     */
    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        if (sbn == null) return
        
        // Remove from hidden notifications if present
        hiddenNotifications.remove(sbn.key)
        Log.d(TAG, "Notification removed: ${sbn.key}")
    }

    /**
     * Hides a notification by canceling it and storing it for later
     */
    private fun hideNotification(sbn: StatusBarNotification) {
        try {
            // Store the notification data
            hiddenNotifications[sbn.key] = NotificationData.from(sbn)
            
            // Cancel the notification (removes it from status bar)
            cancelNotification(sbn.key)
            
            Log.d(TAG, "Hidden notification: ${sbn.key} from ${sbn.packageName}")
        } catch (e: Exception) {
            Log.e(TAG, "Error hiding notification", e)
        }
    }

    /**
     * Restores all hidden notifications when phone is flipped face-up
     */
    private fun restoreAllNotifications() {
        if (hiddenNotifications.isEmpty()) {
            Log.d(TAG, "No notifications to restore")
            return
        }
        
        Log.d(TAG, "Restoring ${hiddenNotifications.size} notifications")
        
        // Post each hidden notification back
        hiddenNotifications.values.forEach { notifData ->
            try {
                // Re-post the notification using NotificationManager
                val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.notify(notifData.tag, notifData.id, notifData.notification)
                
                Log.d(TAG, "Restored notification from ${notifData.packageName}")
            } catch (e: Exception) {
                Log.e(TAG, "Error restoring notification", e)
            }
        }
        
        // Clear the hidden notifications list
        hiddenNotifications.clear()
        
        // Haptic feedback for restoration
        vibrateTwice()
    }

    /**
     * Checks if an app is selected for notification hiding
     */
    private fun isAppSelected(packageName: String): Boolean {
        val selectedApps = prefs.getStringSet(KEY_SELECTED_APPS, emptySet()) ?: emptySet()
        return selectedApps.contains(packageName)
    }

    /**
     * SensorEventListener implementation
     * This is called when accelerometer values change
     * 
     * Accelerometer axes:
     * X-axis: left (-) to right (+)
     * Y-axis: bottom (-) to top (+)
     * Z-axis: back (-) to front (+)
     * 
     * When phone is flat on table:
     * - Face-up: Z ≈ +9.8 (gravity pulling down)
     * - Face-down: Z ≈ -9.8 (gravity pulling up)
     */
    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null || event.sensor.type != Sensor.TYPE_ACCELEROMETER) return
        
        val z = event.values[2]

// Debounce: Only process if orientation changed significantly
if (Math.abs(z - lastOrientation) < 2.0f) {
    return
}

lastOrientation = z

// Detect face-down position
if (z < FACE_DOWN_THRESHOLD && !isFaceDown) {
    isFaceDown = true
    Log.d(TAG, "Phone is now FACE-DOWN (Z=$z)")
    updateForegroundNotification("Phone face-down - Hiding notifications")
}
// Detect face-up position
else if (z > FACE_UP_THRESHOLD && isFaceDown) {
    isFaceDown = false
    Log.d(TAG, "Phone is now FACE-UP (Z=$z)")
    updateForegroundNotification("Phone face-up - Notifications visible")

    // Restore all hidden notifications
    restoreAllNotifications()
}

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Not needed for this use case
    }

    /**
     * Updates the foreground service notification text
     */
    private fun updateForegroundNotification(text: String) {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, notificationIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        
        val notification = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Privacy Mode Active")
            .setContentText(text)
            .setSmallIcon(android.R.drawable.ic_lock_idle_lock)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
        
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.notify(FOREGROUND_NOTIFICATION_ID, notification)
    }

    /**
     * Single vibration pulse for hiding notification
     */
    private fun vibrateOnce() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(50)
        }
    }

    /**
     * Double vibration pulse for restoring notifications
     */
    private fun vibrateTwice() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val timings = longArrayOf(0, 50, 100, 50)
            vibrator.vibrate(VibrationEffect.createWaveform(timings, -1))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(longArrayOf(0, 50, 100, 50), -1)
        }
    }

    /**
     * Called when notification listener is connected
     */
    override fun onListenerConnected() {
        super.onListenerConnected()
        Log.d(TAG, "Notification listener connected")
        
        if (isServiceEnabled && accelerometer != null) {
            registerSensorListener()
        }
    }

    /**
     * Called when notification listener is disconnected
     */
    override fun onListenerDisconnected() {
        super.onListenerDisconnected()
        Log.d(TAG, "Notification listener disconnected")
        unregisterSensorListener()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "Service destroyed")
        unregisterSensorListener()
        
        // Restore any remaining hidden notifications before shutdown
        restoreAllNotifications()
    }
}
