package com.example.axis

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProviderInfo
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.axis.adapters.WidgetsAdapter
import com.example.axis.models.WidgetInfo

class WidgetsActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var ivBack: ImageView
    private lateinit var ivAdd: ImageView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_widgets)
        
        ivBack = findViewById(R.id.ivBack)
        ivAdd = findViewById(R.id.ivAdd)
        recyclerView = findViewById(R.id.recyclerView)
        
        setupRecyclerView()
        setupListeners()
    }
    
    private fun setupRecyclerView() {
        val widgets = getAvailableWidgets()
        val adapter = WidgetsAdapter(widgets) { widget ->
            // Handle widget click
        }
        
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }
    
    private fun setupListeners() {
        ivBack.setOnClickListener { finish() }
        ivAdd.setOnClickListener {
            // Handle add widget
        }
    }
    
    private fun getAvailableWidgets(): List<WidgetInfo> {
        val appWidgetManager = AppWidgetManager.getInstance(this)
        val installedProviders = appWidgetManager.installedProviders
        
        val customWidgets = listOf(
            WidgetInfo("Weather", "San Francisco\nPartly Cloudy\n72°", R.drawable.ic_widget_weather, "weather_blue"),
            WidgetInfo("Schedule", "Team Meeting\n10:00 AM - 11:00 AM\n\nLunch Break\n12:30 PM - 1:30 PM", R.drawable.ic_widget_calendar, "white"),
            WidgetInfo("Music Player", "Now Playing\nSummer Nights\nThe Weeknd", R.drawable.ic_widget_music, "music_purple")
        )
        
        return customWidgets
    }
}
