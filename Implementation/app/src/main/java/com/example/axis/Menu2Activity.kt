package com.example.axis
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.GridLayoutManager

class Menu2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_2)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 4) // 4 columns per row
        recyclerView.adapter = AppIconsAdapter(getAppIcons())
    }

    private fun getAppIcons(): List<AppIcon> {
        return listOf(
            AppIcon(R.drawable.ic_message, "Messages"),
            AppIcon(R.drawable.ic_phone, "Phone"),
            AppIcon(R.drawable.ic_camera, "Camera"),
            AppIcon(R.drawable.ic_message, "Messages"),
            AppIcon(R.drawable.ic_phone, "Phone"),
            AppIcon(R.drawable.ic_camera, "Camera"),
            AppIcon(R.drawable.ic_message, "Messages"),
            AppIcon(R.drawable.ic_phone, "Phone"),
            AppIcon(R.drawable.ic_camera, "Camera")
            // Add more AppIcon objects here
        )
    }
}
