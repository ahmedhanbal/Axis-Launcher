package com.example.axis

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.axis.adapters.ThemesAdapter
import com.example.axis.models.ThemeItem
import com.example.axis.utils.PreferenceManager

class ThemesActivity : BaseActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var ivBack: ImageView
    private lateinit var preferenceManager: PreferenceManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_themes)
        
        preferenceManager = PreferenceManager(this)
        
        ivBack = findViewById(R.id.ivBack)
        recyclerView = findViewById(R.id.recyclerView)
        
        setupRecyclerView()
        setupListeners()
    }
    
    private fun setupRecyclerView() {
        val themes = listOf(
            ThemeItem("Light Theme", "Clean and minimal interface", 
                listOf("#4A90E2", "#9B59B6", "#2ECC71", "#E74C3C", "#F39C12", "#E91E63"),
                isActive = true, isPremium = false),
            ThemeItem("Dark Theme", "Easy on the eyes",
                listOf("#2C3E50", "#8E44AD", "#27AE60", "#C0392B", "#D35400", "#884EA0"),
                isActive = false, isPremium = false),
            ThemeItem("Sunset Theme", "Warm gradient colors",
                listOf("#FF6B9D", "#C44569", "#FFA07A", "#FF7F50", "#FF6347", "#FF4500"),
                isActive = false, isPremium = true)
        )
        
        val adapter = ThemesAdapter(themes) { theme ->
            handleThemeSelection(theme)
        }
        
        recyclerView.layoutManager = GridLayoutManager(this, 1)
        recyclerView.adapter = adapter
    }
    
    private fun setupListeners() {
        ivBack.setOnClickListener { finish() }
    }
    
    private fun handleThemeSelection(theme: ThemeItem) {
        if (theme.isPremium) {
            Toast.makeText(this, "This is a premium theme", Toast.LENGTH_SHORT).show()
        } else {
            preferenceManager.setTheme(theme.name)
            Toast.makeText(this, "${theme.name} applied", Toast.LENGTH_SHORT).show()
            recreate()
        }
    }
}
