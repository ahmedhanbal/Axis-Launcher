package com.example.axis.utils

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager(context: Context) {
    
    private val prefs: SharedPreferences = 
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREF_NAME = "AxisLauncherPrefs"
        private const val KEY_GRID_ROWS = "grid_rows"
        private const val KEY_GRID_COLUMNS = "grid_columns"
        private const val KEY_ICON_SIZE = "icon_size"
        private const val KEY_SHOW_LABELS = "show_labels"
        private const val KEY_DRAWER_MODE = "drawer_mode"
        private const val KEY_FAVORITE_APPS = "favorite_apps"
        private const val KEY_ONBOARDING_COMPLETED = "onboarding_completed"
        private const val KEY_LOGGED_IN = "logged_in"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_THEME = "theme"
        private const val KEY_WALLPAPER = "wallpaper"
        private const val KEY_PROFILE_IMAGE = "profile_image"
        private const val KEY_DARK_MODE = "dark_mode_enabled"
        
        // Dynamic Colors
        private const val KEY_DYN_PRIMARY = "dyn_primary"
        private const val KEY_DYN_ON_PRIMARY = "dyn_on_primary"
        private const val KEY_DYN_PRIMARY_CONTAINER = "dyn_primary_container"
        private const val KEY_DYN_ON_PRIMARY_CONTAINER = "dyn_on_primary_container"
        private const val KEY_DYN_BACKGROUND = "dyn_background"
        private const val KEY_DYN_ON_BACKGROUND = "dyn_on_background"
    }

    fun getProfileImage(): String? {
        return prefs.getString(KEY_PROFILE_IMAGE, null)
    }

    fun setProfileImage(base64Image: String) {
        prefs.edit().putString(KEY_PROFILE_IMAGE, base64Image).apply()
    }

    var gridRows: Int
        get() = prefs.getInt(KEY_GRID_ROWS, 5)
        set(value) = prefs.edit().putInt(KEY_GRID_ROWS, value).apply()

    var gridColumns: Int
        get() = prefs.getInt(KEY_GRID_COLUMNS, 4)
        set(value) = prefs.edit().putInt(KEY_GRID_COLUMNS, value).apply()

    var iconSize: Int
        get() = prefs.getInt(KEY_ICON_SIZE, 70)
        set(value) = prefs.edit().putInt(KEY_ICON_SIZE, value).apply()

    var showLabels: Boolean
        get() = prefs.getBoolean(KEY_SHOW_LABELS, true)
        set(value) = prefs.edit().putBoolean(KEY_SHOW_LABELS, value).apply()

    var drawerMode: Boolean
        get() = prefs.getBoolean(KEY_DRAWER_MODE, true)
        set(value) = prefs.edit().putBoolean(KEY_DRAWER_MODE, value).apply()

    fun addFavoriteApp(packageName: String) {
        val favorites = getFavoriteApps().toMutableSet()
        favorites.add(packageName)
        prefs.edit().putStringSet(KEY_FAVORITE_APPS, favorites).apply()
    }

    fun removeFavoriteApp(packageName: String) {
        val favorites = getFavoriteApps().toMutableSet()
        favorites.remove(packageName)
        prefs.edit().putStringSet(KEY_FAVORITE_APPS, favorites).apply()
    }

    fun getFavoriteApps(): Set<String> {
        return prefs.getStringSet(KEY_FAVORITE_APPS, emptySet()) ?: emptySet()
    }

    fun setFavoriteApps(favorites: Set<String>) {
        prefs.edit().putStringSet(KEY_FAVORITE_APPS, favorites).apply()
    }

    fun isFavorite(packageName: String): Boolean {
        return getFavoriteApps().contains(packageName)
    }
    
    // Onboarding
    fun isOnboardingCompleted(): Boolean {
        return prefs.getBoolean(KEY_ONBOARDING_COMPLETED, false)
    }
    
    fun setOnboardingCompleted(completed: Boolean) {
        prefs.edit().putBoolean(KEY_ONBOARDING_COMPLETED, completed).apply()
    }
    
    // Authentication
    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_LOGGED_IN, false)
    }
    
    fun setLoggedIn(loggedIn: Boolean) {
        prefs.edit().putBoolean(KEY_LOGGED_IN, loggedIn).apply()
    }
    
    fun getUserEmail(): String {
        return prefs.getString(KEY_USER_EMAIL, "") ?: ""
    }
    
    fun setUserEmail(email: String) {
        prefs.edit().putString(KEY_USER_EMAIL, email).apply()
    }

    fun getUserId(): Int {
        return prefs.getInt(KEY_USER_ID, -1)
    }

    fun setUserId(id: Int) {
        prefs.edit().putInt(KEY_USER_ID, id).apply()
    }
    
    fun getUserName(): String {
        return prefs.getString(KEY_USER_NAME, "User") ?: "User"
    }
    
    fun setUserName(name: String) {
        prefs.edit().putString(KEY_USER_NAME, name).apply()
    }
    
    // Theme & Wallpaper
    fun getTheme(): String {
        return prefs.getString(KEY_THEME, "Light Theme") ?: "Light Theme"
    }
    
    fun setTheme(theme: String) {
        prefs.edit().putString(KEY_THEME, theme).apply()
    }
    
    fun getWallpaper(): String {
        return prefs.getString(KEY_WALLPAPER, "Default") ?: "Default"
    }
    
    fun setWallpaper(wallpaper: String) {
        prefs.edit().putString(KEY_WALLPAPER, wallpaper).apply()
    }
    
    // Dark Mode
    var isDarkModeEnabled: Boolean
        get() = prefs.getBoolean(KEY_DARK_MODE, false)
        set(value) = prefs.edit().putBoolean(KEY_DARK_MODE, value).apply()
    
    // Additional settings
    var showAppLabels: Boolean
        get() = prefs.getBoolean(KEY_SHOW_LABELS, true)
        set(value) = prefs.edit().putBoolean(KEY_SHOW_LABELS, value).apply()

    var appLock: Boolean
        get() = prefs.getBoolean("app_lock", false)
        set(value) = prefs.edit().putBoolean("app_lock", value).apply()

    var hideApps: Boolean
        get() = prefs.getBoolean("hide_apps", false)
        set(value) = prefs.edit().putBoolean("hide_apps", value).apply()

    var iconPack: String
        get() = prefs.getString("icon_pack", "classic") ?: "classic"
        set(value) = prefs.edit().putString("icon_pack", value).apply()
        
    // Dynamic Colors
    var dynamicPrimary: Int
        get() = prefs.getInt(KEY_DYN_PRIMARY, android.graphics.Color.parseColor("#6750A4"))
        set(value) = prefs.edit().putInt(KEY_DYN_PRIMARY, value).apply()
        
    var dynamicOnPrimary: Int
        get() = prefs.getInt(KEY_DYN_ON_PRIMARY, android.graphics.Color.WHITE)
        set(value) = prefs.edit().putInt(KEY_DYN_ON_PRIMARY, value).apply()
        
    var dynamicPrimaryContainer: Int
        get() = prefs.getInt(KEY_DYN_PRIMARY_CONTAINER, android.graphics.Color.parseColor("#EADDFF"))
        set(value) = prefs.edit().putInt(KEY_DYN_PRIMARY_CONTAINER, value).apply()
        
    var dynamicOnPrimaryContainer: Int
        get() = prefs.getInt(KEY_DYN_ON_PRIMARY_CONTAINER, android.graphics.Color.parseColor("#21005D"))
        set(value) = prefs.edit().putInt(KEY_DYN_ON_PRIMARY_CONTAINER, value).apply()
        
    var dynamicBackground: Int
        get() = prefs.getInt(KEY_DYN_BACKGROUND, android.graphics.Color.parseColor("#F5F5F5"))
        set(value) = prefs.edit().putInt(KEY_DYN_BACKGROUND, value).apply()
        
    var dynamicOnBackground: Int
        get() = prefs.getInt(KEY_DYN_ON_BACKGROUND, android.graphics.Color.parseColor("#1C1B1F"))
        set(value) = prefs.edit().putInt(KEY_DYN_ON_BACKGROUND, value).apply()
}
