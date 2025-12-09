package com.example.axis.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import androidx.core.graphics.ColorUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream

object ThemeColorExtractor {

    data class ThemeColors(
        val primary: Int,
        val onPrimary: Int,
        val primaryContainer: Int,
        val onPrimaryContainer: Int,
        val background: Int,
        val onBackground: Int
    )

    // Predefined colors for built-in wallpapers
    private val WALLPAPER_COLORS = mapOf(
        "Ocean Sunset" to Color.parseColor("#5E60CE"), // Purple-Blue
        "Rose Garden" to Color.parseColor("#E63946"), // Red
        "Forest Mist" to Color.parseColor("#06A77D"), // Green
        "Fire Sky" to Color.parseColor("#FF6D00"),    // Orange
        "Deep Blue" to Color.parseColor("#4361EE"),   // Blue
        "Aurora" to Color.parseColor("#B5179E"),      // Pink/Purple
        "White" to Color.parseColor("#6750A4"),       // Default Purple for White
        "Default" to Color.parseColor("#6750A4")      // Default Purple
    )

    fun getColorsForWallpaper(wallpaperName: String, isDarkMode: Boolean): ThemeColors {
        val seedColor = WALLPAPER_COLORS[wallpaperName] ?: Color.parseColor("#6750A4")
        return generateThemeColors(seedColor, isDarkMode)
    }

    suspend fun extractColorsFromUri(context: Context, uri: Uri, isDarkMode: Boolean): ThemeColors {
        return withContext(Dispatchers.IO) {
            try {
                val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream?.close()
                
                val seedColor = if (bitmap != null) {
                    calculateDominantColor(bitmap)
                } else {
                    Color.parseColor("#6750A4")
                }
                generateThemeColors(seedColor, isDarkMode)
            } catch (e: Exception) {
                e.printStackTrace()
                generateThemeColors(Color.parseColor("#6750A4"), isDarkMode)
            }
        }
    }

    private fun calculateDominantColor(bitmap: Bitmap): Int {
        // Simple algorithm: Scale down and average center pixels
        val scaled = Bitmap.createScaledBitmap(bitmap, 20, 20, true)
        var redSum = 0
        var greenSum = 0
        var blueSum = 0
        var pixelCount = 0

        for (y in 0 until scaled.height) {
            for (x in 0 until scaled.width) {
                val pixel = scaled.getPixel(x, y)
                redSum += Color.red(pixel)
                greenSum += Color.green(pixel)
                blueSum += Color.blue(pixel)
                pixelCount++
            }
        }

        return Color.rgb(redSum / pixelCount, greenSum / pixelCount, blueSum / pixelCount)
    }

    private fun generateThemeColors(seedColor: Int, isDarkMode: Boolean): ThemeColors {
        val primary = if (isDarkMode) {
            // Lighten for dark mode
            ColorUtils.blendARGB(seedColor, Color.WHITE, 0.2f)
        } else {
            seedColor
        }

        val onPrimary = if (calculateLuminance(primary) > 0.5) Color.BLACK else Color.WHITE
        
        val primaryContainer = if (isDarkMode) {
            ColorUtils.blendARGB(primary, Color.BLACK, 0.7f)
        } else {
            ColorUtils.blendARGB(primary, Color.WHITE, 0.8f)
        }
        
        val onPrimaryContainer = if (isDarkMode) {
            ColorUtils.blendARGB(primary, Color.WHITE, 0.9f)
        } else {
            ColorUtils.blendARGB(primary, Color.BLACK, 0.9f)
        }

        val background = if (isDarkMode) Color.parseColor("#121212") else Color.parseColor("#F5F5F5")
        val onBackground = if (isDarkMode) Color.parseColor("#E0E0E0") else Color.parseColor("#1C1B1F")

        return ThemeColors(
            primary = primary,
            onPrimary = onPrimary,
            primaryContainer = primaryContainer,
            onPrimaryContainer = onPrimaryContainer,
            background = background,
            onBackground = onBackground
        )
    }

    private fun calculateLuminance(color: Int): Double {
        return ColorUtils.calculateLuminance(color)
    }
}
