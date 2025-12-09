package com.example.axis

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.axis.utils.PreferenceManager

class CustomizeActivity : AppCompatActivity() {
    private lateinit var ivBack: ImageView
    private lateinit var ivSave: ImageView
    private lateinit var cardGridSize: CardView
    private lateinit var cardIconSize: CardView
    private lateinit var cardAppLabels: CardView
    private lateinit var btnSaveLayout: Button
    private lateinit var tvGridSize: TextView
    private lateinit var tvIconSize: TextView
    private lateinit var tvAppLabels: TextView
    private lateinit var preferenceManager: PreferenceManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customize)
        
        preferenceManager = PreferenceManager(this)
        
        initViews()
        loadCurrentSettings()
        setupListeners()
    }
    
    private fun initViews() {
        ivBack = findViewById(R.id.ivBack)
        ivSave = findViewById(R.id.ivSave)
        cardGridSize = findViewById(R.id.cardGridSize)
        cardIconSize = findViewById(R.id.cardIconSize)
        cardAppLabels = findViewById(R.id.cardAppLabels)
        btnSaveLayout = findViewById(R.id.btnSaveLayout)
        tvGridSize = findViewById(R.id.tvGridSize)
        tvIconSize = findViewById(R.id.tvIconSize)
        tvAppLabels = findViewById(R.id.tvAppLabels)
    }
    
    private fun loadCurrentSettings() {
        tvGridSize.text = "${preferenceManager.gridColumns}x${preferenceManager.gridColumns}"
        
        val iconSize = when (preferenceManager.iconSize) {
            1 -> "Small"
            2 -> "Medium"
            3 -> "Large"
            else -> "Medium"
        }
        tvIconSize.text = iconSize
        
        tvAppLabels.text = if (preferenceManager.showAppLabels) "Show below icons" else "Hidden"
    }
    
    private fun setupListeners() {
        ivBack.setOnClickListener { finish() }
        
        ivSave.setOnClickListener {
            Toast.makeText(this, "Layout saved", Toast.LENGTH_SHORT).show()
            finish()
        }
        
        cardGridSize.setOnClickListener {
            showGridSizeDialog()
        }
        
        cardIconSize.setOnClickListener {
            showIconSizeDialog()
        }
        
        cardAppLabels.setOnClickListener {
            showAppLabelsDialog()
        }
        
        btnSaveLayout.setOnClickListener {
            Toast.makeText(this, "Layout saved successfully", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
    
    private fun showGridSizeDialog() {
        val options = arrayOf("3x3", "4x4", "5x5", "6x6")
        val currentIndex = preferenceManager.gridColumns - 3
        
        AlertDialog.Builder(this)
            .setTitle("Grid Size")
            .setSingleChoiceItems(options, currentIndex) { dialog, which ->
                preferenceManager.gridColumns = which + 3
                tvGridSize.text = options[which]
                dialog.dismiss()
            }
            .show()
    }
    
    private fun showIconSizeDialog() {
        val options = arrayOf("Small", "Medium", "Large")
        val currentIndex = preferenceManager.iconSize - 1
        
        AlertDialog.Builder(this)
            .setTitle("Icon Size")
            .setSingleChoiceItems(options, currentIndex) { dialog, which ->
                preferenceManager.iconSize = which + 1
                tvIconSize.text = options[which]
                dialog.dismiss()
            }
            .show()
    }
    
    private fun showAppLabelsDialog() {
        val options = arrayOf("Show below icons", "Hidden")
        val currentIndex = if (preferenceManager.showAppLabels) 0 else 1
        
        AlertDialog.Builder(this)
            .setTitle("App Labels")
            .setSingleChoiceItems(options, currentIndex) { dialog, which ->
                preferenceManager.showAppLabels = which == 0
                tvAppLabels.text = options[which]
                dialog.dismiss()
            }
            .show()
    }
}
