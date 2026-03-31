package com.privatenotifications

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView

/**
 * MainActivity - Main user interface for the app
 * 
 * Features:
 * - Enable/disable privacy mode toggle
 * - Status display showing if service is running
 * - Button to grant notification access permission
 * - Button to select apps for notification hiding
 * - Quick setup guide for first-time users
 */
class MainActivity : AppCompatActivity() {

    companion object {
        private const val PREFS_NAME = "PrivateNotificationsPrefs"
        private const val KEY_ENABLED = "service_enabled"
        private const val KEY_FIRST_LAUNCH = "first_launch"
    }

    private lateinit var prefs: SharedPreferences
    private lateinit var statusText: TextView
    private lateinit var serviceToggle: Switch
    private lateinit var permissionButton: Button
    private lateinit var selectAppsButton: Button
    private lateinit var statusCard: MaterialCardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize SharedPreferences
        prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        // Initialize views
        initializeViews()

        // Setup click listeners
        setupListeners()

        // Update UI based on current state
        updateUI()

        // Show first-time setup dialog
        if (prefs.getBoolean(KEY_FIRST_LAUNCH, true)) {
            showFirstTimeSetup()
            prefs.edit().putBoolean(KEY_FIRST_LAUNCH, false).apply()
        }
    }

    /**
     * Initialize all UI views
     */
    private fun initializeViews() {
        statusText = findViewById(R.id.statusText)
        serviceToggle = findViewById(R.id.serviceToggle)
        permissionButton = findViewById(R.id.permissionButton)
        selectAppsButton = findViewById(R.id.selectAppsButton)
        statusCard = findViewById(R.id.statusCard)
    }

    /**
     * Setup click listeners for all interactive elements
     */
    private fun setupListeners() {
        // Service enable/disable toggle
        serviceToggle.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked && !isNotificationAccessGranted()) {
                // If user tries to enable but permission not granted
                serviceToggle.isChecked = false
                showPermissionRequiredDialog()
            } else {
                // Save state and update UI
                prefs.edit().putBoolean(KEY_ENABLED, isChecked).apply()
                updateUI()
                
                if (isChecked) {
                    Toast.makeText(this, "Privacy mode enabled", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Privacy mode disabled", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Grant notification access button
        permissionButton.setOnClickListener {
            openNotificationSettings()
        }

        // Select apps button
        selectAppsButton.setOnClickListener {
            if (isNotificationAccessGranted()) {
                openAppSelection()
            } else {
                showPermissionRequiredDialog()
            }
        }
    }

    /**
     * Updates UI elements based on current app state
     */
    private fun updateUI() {
        val isEnabled = prefs.getBoolean(KEY_ENABLED, false)
        val hasPermission = isNotificationAccessGranted()

        // Update toggle state
        serviceToggle.isChecked = isEnabled

        // Update status text
        when {
            !hasPermission -> {
                statusText.text = "⚠️ Notification access required"
                statusText.setTextColor(getColor(android.R.color.holo_orange_dark))
                statusCard.setCardBackgroundColor(getColor(android.R.color.holo_orange_light))
            }
            isEnabled -> {
                statusText.text = "✓ Privacy mode is ON"
                statusText.setTextColor(getColor(android.R.color.holo_green_dark))
                statusCard.setCardBackgroundColor(getColor(android.R.color.holo_green_light))
            }
            else -> {
                statusText.text = "Privacy mode is OFF"
                statusText.setTextColor(getColor(android.R.color.darker_gray))
                statusCard.setCardBackgroundColor(getColor(android.R.color.white))
            }
        }

        // Enable/disable controls based on permission
        serviceToggle.isEnabled = hasPermission
        selectAppsButton.isEnabled = hasPermission
        
        // Show/hide permission button
        permissionButton.visibility = if (hasPermission) {
            android.view.View.GONE
        } else {
            android.view.View.VISIBLE
        }
    }

    /**
     * Checks if notification access permission is granted
     */
    private fun isNotificationAccessGranted(): Boolean {
        val enabledListeners = Settings.Secure.getString(
            contentResolver,
            "enabled_notification_listeners"
        )
        val packageName = packageName
        return enabledListeners?.contains(packageName) == true
    }

    /**
     * Opens Android notification access settings
     */
    private fun openNotificationSettings() {
        try {
            val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
            startActivity(intent)
            Toast.makeText(
                this,
                "Please enable notification access for Private Notifications",
                Toast.LENGTH_LONG
            ).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Could not open settings", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Opens app selection activity
     */
    private fun openAppSelection() {
        val intent = Intent(this, AppSelectionActivity::class.java)
        startActivity(intent)
    }

    /**
     * Shows dialog explaining why permission is needed
     */
    private fun showPermissionRequiredDialog() {
        AlertDialog.Builder(this)
            .setTitle("Permission Required")
            .setMessage(
                "This app needs notification access permission to:\n\n" +
                "• Monitor incoming notifications\n" +
                "• Hide notifications when phone is face-down\n" +
                "• Restore notifications when phone is face-up\n\n" +
                "Your privacy is protected - all data stays on your device."
            )
            .setPositiveButton("Grant Access") { _, _ ->
                openNotificationSettings()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    /**
     * Shows first-time setup guide
     */
    private fun showFirstTimeSetup() {
        AlertDialog.Builder(this)
            .setTitle("Welcome to Private Notifications")
            .setMessage(
                "This app hides notifications when your phone is face-down.\n\n" +
                "Setup steps:\n" +
                "1. Grant notification access permission\n" +
                "2. Select which apps to monitor\n" +
                "3. Enable privacy mode\n\n" +
                "When your phone is face-down, notifications from selected apps will be hidden. " +
                "Flip it face-up to restore them!"
            )
            .setPositiveButton("Get Started") { _, _ ->
                openNotificationSettings()
            }
            .setNegativeButton("Later", null)
            .setCancelable(false)
            .show()
    }

    override fun onResume() {
        super.onResume()
        // Update UI when returning to activity (in case permission was granted)
        updateUI()
    }
}
