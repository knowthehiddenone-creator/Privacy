package com.privatenotifications

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * AppSelectionActivity - Allows user to select which apps to monitor
 * 
 * Displays a list of all installed apps with checkboxes
 * User can select/deselect apps to hide notifications for
 * Selections are saved to SharedPreferences
 */
class AppSelectionActivity : AppCompatActivity() {

    companion object {
        private const val PREFS_NAME = "PrivateNotificationsPrefs"
        private const val KEY_SELECTED_APPS = "selected_apps"
    }

    private lateinit var prefs: SharedPreferences
    private lateinit var recyclerView: RecyclerView
    private lateinit var saveButton: Button
    private lateinit var adapter: AppAdapter
    private val selectedApps = mutableSetOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_selection)

        // Enable back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Initialize SharedPreferences
        prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        // Load previously selected apps
        selectedApps.addAll(prefs.getStringSet(KEY_SELECTED_APPS, emptySet()) ?: emptySet())

        // Initialize views
        initializeViews()

        // Load installed apps
        loadInstalledApps()

        // Setup save button
        saveButton.setOnClickListener {
            saveSelectedApps()
        }
    }

    /**
     * Initialize UI views
     */
    private fun initializeViews() {
        recyclerView = findViewById(R.id.appsRecyclerView)
        saveButton = findViewById(R.id.saveButton)
        
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    /**
     * Loads all installed apps and displays them in RecyclerView
     */
    private fun loadInstalledApps() {
        val packageManager = packageManager
        val apps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
            .filter { isUserApp(it) }  // Filter out system apps
            .map { appInfo ->
                AppItem(
                    packageName = appInfo.packageName,
                    appName = appInfo.loadLabel(packageManager).toString(),
                    icon = appInfo.loadIcon(packageManager),
                    isSelected = selectedApps.contains(appInfo.packageName)
                )
            }
            .sortedBy { it.appName.lowercase() }  // Sort alphabetically

        adapter = AppAdapter(apps) { packageName, isSelected ->
            if (isSelected) {
                selectedApps.add(packageName)
            } else {
                selectedApps.remove(packageName)
            }
        }
        
        recyclerView.adapter = adapter
    }

    /**
     * Checks if app is a user-installed app (not system app)
     */
    private fun isUserApp(appInfo: ApplicationInfo): Boolean {
        return (appInfo.flags and ApplicationInfo.FLAG_SYSTEM) == 0 ||
               (appInfo.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0
    }

    /**
     * Saves selected apps to SharedPreferences
     */
    private fun saveSelectedApps() {
        prefs.edit()
            .putStringSet(KEY_SELECTED_APPS, selectedApps)
            .apply()

        Toast.makeText(
            this,
            "Selected ${selectedApps.size} apps",
            Toast.LENGTH_SHORT
        ).show()

        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    /**
     * Data class representing an app item
     */
    data class AppItem(
        val packageName: String,
        val appName: String,
        val icon: android.graphics.drawable.Drawable,
        var isSelected: Boolean
    )

    /**
     * RecyclerView adapter for app list
     */
    class AppAdapter(
        private val apps: List<AppItem>,
        private val onSelectionChanged: (String, Boolean) -> Unit
    ) : RecyclerView.Adapter<AppAdapter.AppViewHolder>() {

        class AppViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val icon: ImageView = view.findViewById(R.id.appIcon)
            val name: TextView = view.findViewById(R.id.appName)
            val packageName: TextView = view.findViewById(R.id.packageName)
            val checkbox: CheckBox = view.findViewById(R.id.appCheckbox)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_app_selection, parent, false)
            return AppViewHolder(view)
        }

        override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
            val app = apps[position]
            
            holder.icon.setImageDrawable(app.icon)
            holder.name.text = app.appName
            holder.packageName.text = app.packageName
            holder.checkbox.isChecked = app.isSelected

            // Handle checkbox changes
            holder.checkbox.setOnCheckedChangeListener { _, isChecked ->
                app.isSelected = isChecked
                onSelectionChanged(app.packageName, isChecked)
            }

            // Make entire row clickable
            holder.itemView.setOnClickListener {
                holder.checkbox.isChecked = !holder.checkbox.isChecked
            }
        }

        override fun getItemCount() = apps.size
    }
}
