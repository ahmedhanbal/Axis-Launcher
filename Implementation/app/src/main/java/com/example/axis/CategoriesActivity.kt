package com.example.axis

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.axis.adapters.CategoriesAdapter
import com.example.axis.models.AppCategory
import com.example.axis.utils.AppManager

class CategoriesActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var ivBack: ImageView
    private lateinit var ivAdd: ImageView
    private lateinit var appManager: AppManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories)
        
        appManager = AppManager(this)
        
        ivBack = findViewById(R.id.ivBack)
        ivAdd = findViewById(R.id.ivAdd)
        recyclerView = findViewById(R.id.recyclerView)
        
        setupRecyclerView()
        setupListeners()
    }
    
    private fun setupRecyclerView() {
        val categories = getCategorizedApps()
        val adapter = CategoriesAdapter(categories) { category ->
            // Handle category click
        }
        
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }
    
    private fun setupListeners() {
        ivBack.setOnClickListener { finish() }
        ivAdd.setOnClickListener {
            // Handle add category
        }
    }
    
    private fun getCategorizedApps(): List<AppCategory> {
        val allApps = appManager.getInstalledApps()
        
        return listOf(
            AppCategory("Communication", R.drawable.ic_category_communication, 
                allApps.filter { it.packageName.contains("message") || it.packageName.contains("phone") || it.packageName.contains("contacts") }.size),
            AppCategory("Media & Entertainment", R.drawable.ic_category_media,
                allApps.filter { it.packageName.contains("music") || it.packageName.contains("video") || it.packageName.contains("youtube") }.size),
            AppCategory("Productivity", R.drawable.ic_category_productivity,
                allApps.filter { it.packageName.contains("docs") || it.packageName.contains("note") || it.packageName.contains("calendar") }.size),
            AppCategory("Social Media", R.drawable.ic_category_social,
                allApps.filter { it.packageName.contains("facebook") || it.packageName.contains("twitter") || it.packageName.contains("instagram") }.size),
            AppCategory("Utilities", R.drawable.ic_category_utilities,
                allApps.filter { it.packageName.contains("settings") || it.packageName.contains("clock") || it.packageName.contains("calculator") }.size),
            AppCategory("Health & Fitness", R.drawable.ic_category_health,
                allApps.filter { it.packageName.contains("health") || it.packageName.contains("fitness") }.size)
        )
    }
}
