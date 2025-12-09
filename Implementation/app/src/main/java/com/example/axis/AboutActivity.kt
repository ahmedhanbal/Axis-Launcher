package com.example.axis

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import android.widget.TextView

class AboutActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var versionText: TextView
    private lateinit var buildText: TextView
    private lateinit var developerText: TextView
    private lateinit var descriptionText: TextView
    private lateinit var rateRow: CardView
    private lateinit var shareRow: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        toolbar = findViewById(R.id.aboutToolbar)
        versionText = findViewById(R.id.aboutVersion)
        buildText = findViewById(R.id.aboutBuild)
        developerText = findViewById(R.id.aboutDeveloper)
        descriptionText = findViewById(R.id.aboutDescription)
        rateRow = findViewById(R.id.aboutRateRow)
        shareRow = findViewById(R.id.aboutShareRow)
        setupToolbar()
        bindData()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }

    private fun bindData() {
        versionText.text = "Version 1.0"
        buildText.text = "Build 100"
        developerText.text = "Developer: Example"
        descriptionText.text = "Axis Launcher is a customizable Android home experience."
        
        rateRow.setOnClickListener { 
            try {
                startActivity(android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse("market://details?id=$packageName")))
            } catch (e: android.content.ActivityNotFoundException) {
                startActivity(android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse("https://play.google.com/store/apps/details?id=$packageName")))
            }
        }
        
        shareRow.setOnClickListener { 
            val sendIntent = android.content.Intent().apply {
                action = android.content.Intent.ACTION_SEND
                putExtra(android.content.Intent.EXTRA_TEXT, "Check out Axis Launcher! https://example.com/axis")
                type = "text/plain"
            }
            val shareIntent = android.content.Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }
    }
}
