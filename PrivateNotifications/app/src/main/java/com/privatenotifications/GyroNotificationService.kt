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

class GyroNotificationService : NotificationListenerService(), SensorEventListener {

    companion object {
        private const val TAG = "GyroNotificationService"
        private const val PREFS_NAME = "PrivateNotificationsPrefs"
        private const val KEY_ENABLED = "service_enabled"
        private const val KEY_SELECTED_APPS = "selected_apps"
        private const val NOTIFICATION_CHANNEL_ID = "GyroNotificationServiceChannel"
        private const val FOREGROUND_NOTIFICATION_ID = 1001
    }

    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private var isServiceEnabled = false

    private val hiddenNotifications = mutableMapOf<String, NotificationData>()

    private var isFaceDown = false

    // 🔥 NEW: stability + movement detection
    private var lastX = 0f
    private var lastY = 0f
    private var lastZ = 0f
    private var isMoving = false
    private var lastStateChangeTime = 0L

    private lateinit var prefs: SharedPreferences
    private lateinit var vibrator: Vibrator
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate() {
        super.onCreate()

        prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        isServiceEnabled = prefs.getBoolean(KEY_ENABLED, false)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        startForegroundService()

        if (isServiceEnabled && accelerometer != null) {
            registerSensorListener()
        }
    }

    private fun startForegroundService() {
        createNotificationChannel()

        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
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

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "Gyro Notification Service",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    private fun registerSensorListener() {
        accelerometer?.let {
            sensorManager.registerListener(
                this,
                it,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }

    private fun unregisterSensorListener() {
        sensorManager.unregisterListener(this)
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        if (sbn == null || !isServiceEnabled) return

        val pkg = sbn.packageName

        if (!isAppSelected(pkg)) return

        if (isFaceDown) {
            hideNotification(sbn)
            vibrateOnce()
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        if (sbn == null) return
        hiddenNotifications.remove(sbn.key)
    }

    private fun hideNotification(sbn: StatusBarNotification) {
        hiddenNotifications[sbn.key] = NotificationData.from(sbn)
        cancelNotification(sbn.key)
    }

    private fun restoreAllNotifications() {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        hiddenNotifications.values.forEach {
            manager.notify(it.tag, it.id, it.notification)
        }

        hiddenNotifications.clear()
        vibrateTwice()
    }

    private fun isAppSelected(packageName: String): Boolean {
        val set = prefs.getStringSet(KEY_SELECTED_APPS, emptySet()) ?: emptySet()
        return set.contains(packageName)
    }

    // 🔥🔥🔥 MAIN FIXED SENSOR LOGIC
    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null || event.sensor.type != Sensor.TYPE_ACCELEROMETER) return

        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]
        val currentTime = System.currentTimeMillis()

        // 🔥 Movement detection (ignore shake)
        val dx = Math.abs(x - lastX)
        val dy = Math.abs(y - lastY)
        val dz = Math.abs(z - lastZ)

        isMoving = (dx + dy + dz) > 2.5f

        lastX = x
        lastY = y
        lastZ = z

        if (isMoving) return

        // 🔥 Ignore half tilt
        if (Math.abs(z) < 9.0f) return

        // 🔥 Delay control
        val delay = 1500L
        if (currentTime - lastStateChangeTime < delay) return

        // 🔥 Face Down
        if (z < -9.5f && !isFaceDown) {
            isFaceDown = true
            lastStateChangeTime = currentTime

            Log.d(TAG, "FACE DOWN")
            updateForegroundNotification("Phone face-down - Hiding notifications")
        }

        // 🔥 Face Up
        else if (z > 9.5f && isFaceDown) {
            isFaceDown = false
            lastStateChangeTime = currentTime

            Log.d(TAG, "FACE UP")
            updateForegroundNotification("Phone face-up - Notifications visible")

            restoreAllNotifications()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    private fun updateForegroundNotification(text: String) {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Privacy Mode Active")
            .setContentText(text)
            .setSmallIcon(android.R.drawable.ic_lock_idle_lock)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()

        val manager = getSystemService(NotificationManager::class.java)
        manager.notify(FOREGROUND_NOTIFICATION_ID, notification)
    }

    private fun vibrateOnce() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
        }
    }

    private fun vibrateTwice() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createWaveform(longArrayOf(0, 50, 100, 50), -1))
        }
    }

    override fun onListenerConnected() {
        if (isServiceEnabled && accelerometer != null) {
            registerSensorListener()
        }
    }

    override fun onListenerDisconnected() {
        unregisterSensorListener()
    }

    override fun onDestroy() {
        unregisterSensorListener()
        restoreAllNotifications()
        super.onDestroy()
    }
}
