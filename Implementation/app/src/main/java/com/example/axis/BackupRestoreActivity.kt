package com.example.axis

import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.axis.utils.PreferenceManager
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class BackupRestoreActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var lastBackupText: TextView
    private lateinit var backupNowButton: Button
    private lateinit var restoreButton: Button
    private lateinit var autoBackupSwitch: Switch
    private lateinit var preferenceManager: PreferenceManager
    private var lastBackupTime: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_backup_restore)
        preferenceManager = PreferenceManager(this)
        initializeViews()
        setupToolbar()
        bindData()
        setupActions()
    }

    private fun initializeViews() {
        toolbar = findViewById(R.id.backupToolbar)
        lastBackupText = findViewById(R.id.lastBackupText)
        backupNowButton = findViewById(R.id.backupNowButton)
        restoreButton = findViewById(R.id.restoreButton)
        autoBackupSwitch = findViewById(R.id.autoBackupSwitch)
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }

    private fun bindData() {
        lastBackupText.text = if (lastBackupTime == 0L) "No backups yet" else formatTime(lastBackupTime)
        autoBackupSwitch.isChecked = false
    }

    private fun setupActions() {
        backupNowButton.setOnClickListener {
            performBackup()
        }
        restoreButton.setOnClickListener {
            performRestore()
        }
        autoBackupSwitch.setOnCheckedChangeListener { _, isChecked -> 
            // Save auto backup pref
        }
    }

    private fun performBackup() {
        try {
            val backupData = org.json.JSONObject()
            backupData.put("grid_rows", preferenceManager.gridRows)
            backupData.put("grid_columns", preferenceManager.gridColumns)
            backupData.put("icon_size", preferenceManager.iconSize)
            backupData.put("show_labels", preferenceManager.showLabels)
            backupData.put("drawer_mode", preferenceManager.drawerMode)
            
            val favoritesArray = org.json.JSONArray()
            preferenceManager.getFavoriteApps().forEach { favoritesArray.put(it) }
            backupData.put("favorites", favoritesArray)
            
            val file = java.io.File(getExternalFilesDir(null), "backup.json")
            file.writeText(backupData.toString())
            
            lastBackupTime = System.currentTimeMillis()
            lastBackupText.text = formatTime(lastBackupTime)
            
            android.widget.Toast.makeText(this, "Backup saved successfully", android.widget.Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            android.widget.Toast.makeText(this, "Backup failed: ${e.message}", android.widget.Toast.LENGTH_SHORT).show()
        }
    }

    private fun performRestore() {
        try {
            val file = java.io.File(getExternalFilesDir(null), "backup.json")
            if (!file.exists()) {
                android.widget.Toast.makeText(this, "No backup found", android.widget.Toast.LENGTH_SHORT).show()
                return
            }
            
            val jsonString = file.readText()
            val backupData = org.json.JSONObject(jsonString)
            
            preferenceManager.gridRows = backupData.optInt("grid_rows", 5)
            preferenceManager.gridColumns = backupData.optInt("grid_columns", 4)
            preferenceManager.iconSize = backupData.optInt("icon_size", 70)
            preferenceManager.showLabels = backupData.optBoolean("show_labels", true)
            preferenceManager.drawerMode = backupData.optBoolean("drawer_mode", true)
            
            val favoritesArray = backupData.optJSONArray("favorites")
            val favoritesSet = mutableSetOf<String>()
            if (favoritesArray != null) {
                for (i in 0 until favoritesArray.length()) {
                    favoritesSet.add(favoritesArray.getString(i))
                }
            }
            preferenceManager.setFavoriteApps(favoritesSet)
            
            android.widget.Toast.makeText(this, "Restore successful. Restarting...", android.widget.Toast.LENGTH_SHORT).show()
            
            // Restart app to apply changes
            val intent = android.content.Intent(this, HomeActivity::class.java)
            intent.flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK or android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        } catch (e: Exception) {
            android.widget.Toast.makeText(this, "Restore failed: ${e.message}", android.widget.Toast.LENGTH_SHORT).show()
        }
    }

    private fun formatTime(time: Long): String {
        val sdf = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
        return "Last Backup: ${sdf.format(Date(time))}"
    }
}
