package com.example.axis

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.axis.adapters.SettingsAdapter
import com.example.axis.adapters.SettingsAdapter.SettingItem
import com.example.axis.adapters.SettingsAdapter.SettingType
import com.example.axis.utils.PreferenceManager
import com.example.axis.utils.SyncManager

class SettingsActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var settingsRecyclerView: RecyclerView
    private lateinit var preferenceManager: PreferenceManager
    private lateinit var syncManager: SyncManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        initializeViews()
        setupToolbar()
        setupSettings()
    }

    private fun initializeViews() {
        toolbar = findViewById(R.id.toolbar)
        settingsRecyclerView = findViewById(R.id.settingsRecyclerView)
        preferenceManager = PreferenceManager(this)
        syncManager = SyncManager(this)
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupSettings() {
        // Define items
        val gridColumns = SettingItem(
            "Grid Columns",
            "Current: ${preferenceManager.gridColumns} columns",
            SettingType.GRID_SIZE
        )
        val iconSize = SettingItem(
            "Icon Size",
            "Current: ${preferenceManager.iconSize}dp",
            SettingType.ICON_SIZE
        )
        val showLabels = SettingItem(
            "Show Labels",
            if (preferenceManager.showLabels) "Labels are shown" else "Labels are hidden",
            SettingType.SHOW_LABELS
        )
        val homeStyle = SettingItem(
            "Home Screen Style",
            if (preferenceManager.drawerMode) "Drawer (App Drawer enabled)" else "Classic (All apps on Home)",
            SettingType.DRAWER_MODE
        )
        val appearance = SettingItem(
            "Appearance",
            "Theme, wallpaper & dark mode",
            SettingType.APPEARANCE
        )
        val profile = SettingItem(
            "Profile",
            "User info & stats",
            SettingType.PROFILE
        )
        val backup = SettingItem(
            "Backup & Restore",
            "Manage local backups",
            SettingType.BACKUP_RESTORE
        )
        val cloudSync = SettingItem(
            "Cloud Sync",
            "Sync launcher data",
            SettingType.CLOUD_SYNC
        )
        val notifications = SettingItem(
            "Notifications",
            "View recent alerts",
            SettingType.NOTIFICATIONS
        )
        val privacy = SettingItem(
            "Privacy & Security",
            "Lock & permissions",
            SettingType.PRIVACY_SECURITY
        )
        val about = SettingItem(
            "About",
            "App info & credits",
            SettingType.ABOUT
        )

        // Organize into sections
        val settingsItems = listOf(
            // User Section
            SettingsAdapter.SettingsListItem.Header("User"),
            SettingsAdapter.SettingsListItem.Setting(profile),

            // Display Section
            SettingsAdapter.SettingsListItem.Header("Display"),
            SettingsAdapter.SettingsListItem.Setting(gridColumns),
            SettingsAdapter.SettingsListItem.Setting(iconSize),
            SettingsAdapter.SettingsListItem.Setting(showLabels),
            SettingsAdapter.SettingsListItem.Setting(homeStyle),
            SettingsAdapter.SettingsListItem.Setting(appearance),

            // Data & Sync Section
            SettingsAdapter.SettingsListItem.Header("Data & Sync"),
            SettingsAdapter.SettingsListItem.Setting(backup),
            SettingsAdapter.SettingsListItem.Setting(cloudSync),

            // System Section
            SettingsAdapter.SettingsListItem.Header("System"),
            SettingsAdapter.SettingsListItem.Setting(notifications),
            SettingsAdapter.SettingsListItem.Setting(privacy),
            SettingsAdapter.SettingsListItem.Setting(about)
        )

        settingsRecyclerView.layoutManager = LinearLayoutManager(this)
        settingsRecyclerView.adapter = SettingsAdapter(settingsItems) { settingItem ->
            handleSettingClick(settingItem)
        }
    }

    private fun handleSettingClick(settingItem: SettingItem) {
        when (settingItem.type) {
            SettingType.GRID_SIZE -> showGridSizeDialog()
            SettingType.ICON_SIZE -> showIconSizeDialog()
            SettingType.SHOW_LABELS -> toggleShowLabels()
            SettingType.DRAWER_MODE -> showHomeStyleDialog()
            SettingType.APPEARANCE -> showAppearanceSettings()
            SettingType.PROFILE -> launch(ProfileActivity::class.java)
            SettingType.BACKUP_RESTORE -> launch(BackupRestoreActivity::class.java)
            SettingType.CLOUD_SYNC -> launch(CloudSyncActivity::class.java)
            SettingType.NOTIFICATIONS -> launch(NotificationsActivity::class.java)
            SettingType.PRIVACY_SECURITY -> launch(PrivacySecurityActivity::class.java)
            SettingType.ABOUT -> launch(AboutActivity::class.java)
            SettingType.WALLPAPERS -> launch(WallpapersActivity::class.java)
        }
    }

    private fun showGridSizeDialog() {
        showColumnDialog()
    }



    private fun showColumnDialog() {
        val columns = arrayOf("3", "4", "5")
        AlertDialog.Builder(this)
            .setTitle("Select Columns")
            .setItems(columns) { _, which ->
                preferenceManager.gridColumns = columns[which].toInt()
                Toast.makeText(this, "Grid columns set to ${columns[which]}", Toast.LENGTH_SHORT).show()
                syncManager.syncSettings()
                setupSettings() // Refresh
            }
            .show()
    }

    private fun showIconSizeDialog() {
        val sizes = arrayOf("Small (60dp)", "Medium (70dp)", "Large (80dp)", "Extra Large (90dp)")
        val sizeValues = arrayOf(60, 70, 80, 90)
        
        AlertDialog.Builder(this)
            .setTitle("Icon Size")
            .setItems(sizes) { _, which ->
                preferenceManager.iconSize = sizeValues[which]
                Toast.makeText(this, "Icon size set to ${sizes[which]}", Toast.LENGTH_SHORT).show()
                syncManager.syncSettings()
                setupSettings() // Refresh
            }
            .show()
    }

    private fun toggleShowLabels() {
        preferenceManager.showLabels = !preferenceManager.showLabels
        Toast.makeText(
            this,
            if (preferenceManager.showLabels) "Labels shown" else "Labels hidden",
            Toast.LENGTH_SHORT
        ).show()
        syncManager.syncSettings()
        setupSettings() // Refresh
    }

    private fun showHomeStyleDialog() {
        val styles = arrayOf("Drawer Mode", "Classic Mode")
        val currentStyle = if (preferenceManager.drawerMode) 0 else 1
        
        AlertDialog.Builder(this)
            .setTitle("Home Screen Style")
            .setSingleChoiceItems(styles, currentStyle) { dialog, which ->
                val newDrawerMode = which == 0
                if (preferenceManager.drawerMode != newDrawerMode) {
                    preferenceManager.drawerMode = newDrawerMode
                    syncManager.syncSettings()
                    setupSettings() // Refresh
                    
                    Toast.makeText(
                        this,
                        if (newDrawerMode) "Switched to Drawer Mode" else "Switched to Classic Mode",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showAppearanceSettings() {
        val options = arrayOf(
            "Theme",
            "Wallpaper"
        )
        
        AlertDialog.Builder(this)
            .setTitle("Appearance")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> showThemeSelection()
                    1 -> showWallpaperPicker()
                }
            }
            .setNegativeButton("Close", null)
            .show()
    }
    
    private fun showThemeSelection() {
        val themes = arrayOf("Light", "Dark")
        val currentThemeIndex = if (preferenceManager.isDarkModeEnabled) 1 else 0

        AlertDialog.Builder(this)
            .setTitle("Select Theme")
            .setSingleChoiceItems(themes, currentThemeIndex) { dialog, which ->
                val isDark = which == 1
                if (preferenceManager.isDarkModeEnabled != isDark) {
                    preferenceManager.isDarkModeEnabled = isDark
                    
                    // Update dynamic colors for new mode
                    val currentWallpaper = preferenceManager.getWallpaper()
                    val colors = com.example.axis.utils.ThemeColorExtractor.getColorsForWallpaper(currentWallpaper, isDark)
                    updateDynamicColors(colors)
                    
                    syncManager.syncSettings()
                    
                    // Apply change
                    AppCompatDelegate.setDefaultNightMode(
                        if (isDark) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
                    )
                    
                    dialog.dismiss()
                    recreate() // Faster than finish/startActivity
                } else {
                    dialog.dismiss()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun updateDynamicColors(colors: com.example.axis.utils.ThemeColorExtractor.ThemeColors) {
        preferenceManager.dynamicPrimary = colors.primary
        preferenceManager.dynamicOnPrimary = colors.onPrimary
        preferenceManager.dynamicPrimaryContainer = colors.primaryContainer
        preferenceManager.dynamicOnPrimaryContainer = colors.onPrimaryContainer
        preferenceManager.dynamicBackground = colors.background
        preferenceManager.dynamicOnBackground = colors.onBackground
    }
    
    private fun showWallpaperPicker() {
        val wallpapers = arrayOf(
            "Default", 
            "Ocean Sunset", 
            "Rose Garden", 
            "Forest Mist", 
            "Fire Sky", 
            "Deep Blue", 
            "Aurora", 
            "White",
            "Custom Image..."
        )
        val currentWallpaper = preferenceManager.getWallpaper()
        val checkedItem = wallpapers.indexOf(currentWallpaper).takeIf { it >= 0 } ?: 0
        
        AlertDialog.Builder(this)
            .setTitle("Select Wallpaper")
            .setSingleChoiceItems(wallpapers, checkedItem) { dialog, which ->
                if (which == wallpapers.size - 1) {
                    // Custom Image option - launch WallpapersActivity
                    dialog.dismiss()
                    launch(WallpapersActivity::class.java)
                } else {
                    val selectedWallpaper = wallpapers[which]
                    preferenceManager.setWallpaper(selectedWallpaper)
                    
                    // Extract and save new colors
                    val isDark = preferenceManager.isDarkModeEnabled
                    val colors = com.example.axis.utils.ThemeColorExtractor.getColorsForWallpaper(selectedWallpaper, isDark)
                    updateDynamicColors(colors)
                    
                    syncManager.syncSettings()
                    Toast.makeText(this, "$selectedWallpaper applied", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                    recreate() // Apply new theme colors immediately
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }



    private fun <T> launch(cls: Class<T>) {
        startActivity(android.content.Intent(this, cls))
    }
}
