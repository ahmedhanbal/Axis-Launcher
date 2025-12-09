package com.example.axis

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.axis.adapters.WallpapersAdapter
import com.example.axis.models.WallpaperItem
import com.example.axis.utils.PreferenceManager

class WallpapersActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var ivBack: ImageView
    private lateinit var ivDownload: ImageView
    private lateinit var preferenceManager: PreferenceManager
    
    private val pickImageLauncher = registerForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.GetContent()
    ) { uri: android.net.Uri? ->
        uri?.let {
            // Persist permission to access the URI across reboots
            try {
                contentResolver.takePersistableUriPermission(
                    it,
                    android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
            
            preferenceManager.setWallpaper(it.toString())
            Toast.makeText(this, "Custom wallpaper set", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallpapers)
        
        preferenceManager = PreferenceManager(this)
        
        ivBack = findViewById(R.id.ivBack)
        ivDownload = findViewById(R.id.ivDownload)
        recyclerView = findViewById(R.id.recyclerView)
        
        setupRecyclerView()
        setupListeners()
    }
    
    private fun setupRecyclerView() {
        val wallpapers = listOf(
            WallpaperItem("Ocean Sunset", "#5E60CE", "#6930C3"),
            WallpaperItem("Rose Garden", "#E63946", "#F72585"),
            WallpaperItem("Forest Mist", "#06A77D", "#2EC4B6"),
            WallpaperItem("Fire Sky", "#FF6D00", "#FF9E00"),
            WallpaperItem("Deep Blue", "#4361EE", "#3F37C9"),
            WallpaperItem("Aurora", "#B5179E", "#7209B7"),
            WallpaperItem("White", "#FFFFFF", "#F0F0F0")
        )
        
        val adapter = WallpapersAdapter(wallpapers) { wallpaper ->
            handleWallpaperSelection(wallpaper)
        }
        
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = adapter
    }
    
    private fun setupListeners() {
        ivBack.setOnClickListener { finish() }
        ivDownload.setOnClickListener {
            // "Download" icon acts as "Pick Image" button
            pickImageLauncher.launch("image/*")
        }
    }
    
    private fun handleWallpaperSelection(wallpaper: WallpaperItem) {
        preferenceManager.setWallpaper(wallpaper.name)
        Toast.makeText(this, "${wallpaper.name} applied", Toast.LENGTH_SHORT).show()
    }
}
