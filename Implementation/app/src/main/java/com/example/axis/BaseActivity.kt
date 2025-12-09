package com.example.axis

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.axis.utils.PreferenceManager

open class BaseActivity : AppCompatActivity() {
    lateinit var basePreferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        basePreferenceManager = PreferenceManager(this)
        applyTheme()
        super.onCreate(savedInstanceState)
    }

    private fun applyTheme() {
        // Apply dark mode setting
        val darkModeEnabled = basePreferenceManager.isDarkModeEnabled
        if (darkModeEnabled) {
            androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode(
                androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
            )
        } else {
            androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode(
                androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
            )
        }
        
        // Apply base theme (DayNight)
        setTheme(R.style.Theme_Axis)
    }
    
    override fun onResume() {
        super.onResume()
        applyWallpaper()
    }

    private fun applyWallpaper() {
        val wallpaperName = basePreferenceManager.getWallpaper()
        val rootView = window.decorView.rootView
        
        // Apply Dynamic Colors first
        applyDynamicColors()
        
        if (wallpaperName.startsWith("content://") || wallpaperName.startsWith("file://")) {
            try {
                val uri = android.net.Uri.parse(wallpaperName)
                val inputStream = contentResolver.openInputStream(uri)
                val drawable = android.graphics.drawable.Drawable.createFromStream(inputStream, uri.toString())
                rootView.background = drawable
                inputStream?.close()
            } catch (e: Exception) {
                e.printStackTrace()
                // Fallback to dynamic background
                rootView.setBackgroundColor(basePreferenceManager.dynamicBackground)
            }
        } else {
            val drawableRes = when (wallpaperName) {
                "Ocean Sunset" -> R.drawable.wallpaper_ocean_sunset
                "Rose Garden" -> R.drawable.wallpaper_rose_garden
                "Forest Mist" -> R.drawable.wallpaper_forest_mist
                "Fire Sky" -> R.drawable.wallpaper_fire_sky
                "Deep Blue" -> R.drawable.wallpaper_deep_blue
                "Aurora" -> R.drawable.wallpaper_aurora
                "White" -> R.drawable.wallpaper_white
                else -> 0
            }
            
            if (drawableRes != 0) {
                rootView.setBackgroundResource(drawableRes)
            } else {
                rootView.setBackgroundColor(basePreferenceManager.dynamicBackground)
            }
        }
    }

    private fun applyDynamicColors() {
        val primary = basePreferenceManager.dynamicPrimary
        val onPrimary = basePreferenceManager.dynamicOnPrimary
        val background = basePreferenceManager.dynamicBackground
        val onBackground = basePreferenceManager.dynamicOnBackground
        
        // Apply to Status Bar and Navigation Bar
        window.statusBarColor = primary
        window.navigationBarColor = background
        
        // Update Action Bar / Toolbar if present
        supportActionBar?.setBackgroundDrawable(android.graphics.drawable.ColorDrawable(primary))
        
        // We can't easily update all UI elements here without traversing the view tree or using a library
        // But we can set the window background
        window.decorView.setBackgroundColor(background)
        
        // For text colors, we rely on the layout using ?attr/textColorPrimary etc.
        // But since we can't dynamically update the theme attributes at runtime without RROs or extensive work,
        // we will focus on the main container colors.
        // To truly support dynamic text colors in standard views, we'd need to subclass TextView or iterate views.
        // For this scope, ensuring the background and status bar match the theme is the key "dynamic" part.
    }
}
