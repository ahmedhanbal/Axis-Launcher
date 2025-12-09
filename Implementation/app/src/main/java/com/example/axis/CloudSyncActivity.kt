package com.example.axis

import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CloudSyncActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var overallStatusText: TextView
    private lateinit var syncRecycler: RecyclerView
    private lateinit var syncNowButton: Button
    private lateinit var autoSyncSwitch: Switch
    private var lastSyncTime: Long = 0L
    private val items = mutableListOf<SyncItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cloud_sync)
        initializeViews()
        setupToolbar()
        seedData()
        bindData()
        setupActions()
    }

    private fun initializeViews() {
        toolbar = findViewById(R.id.cloudSyncToolbar)
        overallStatusText = findViewById(R.id.cloudSyncOverallStatus)
        syncRecycler = findViewById(R.id.cloudSyncRecycler)
        syncNowButton = findViewById(R.id.cloudSyncSyncNow)
        autoSyncSwitch = findViewById(R.id.cloudSyncAutoSyncSwitch)
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }

    private fun seedData() {
        items.clear()
        items.add(SyncItem("Apps", true, System.currentTimeMillis()))
        items.add(SyncItem("Preferences", true, System.currentTimeMillis()))
        items.add(SyncItem("Themes", true, System.currentTimeMillis()))
    }

    private fun bindData() {
        overallStatusText.text = if (lastSyncTime == 0L) "Never Synced" else formatTime(lastSyncTime)
        syncRecycler.layoutManager = LinearLayoutManager(this)
        syncRecycler.adapter = SyncItemsAdapter(items)
    }

    private fun setupActions() {
        syncNowButton.setOnClickListener {
            syncNowButton.isEnabled = false
            overallStatusText.text = "Syncing..."
            
            val syncManager = com.example.axis.utils.SyncManager(this)
            
            // Sync Settings
            syncManager.syncSettings { settingsSuccess ->
                // Sync Favorites
                syncManager.syncFavorites { favoritesSuccess ->
                    runOnUiThread {
                        lastSyncTime = System.currentTimeMillis()
                        
                        items.clear()
                        items.add(SyncItem("Settings", settingsSuccess, lastSyncTime))
                        items.add(SyncItem("Favorites", favoritesSuccess, lastSyncTime))
                        
                        syncRecycler.adapter = SyncItemsAdapter(items)
                        overallStatusText.text = formatTime(lastSyncTime)
                        syncNowButton.isEnabled = true
                    }
                }
            }
        }
        autoSyncSwitch.setOnCheckedChangeListener { _, isChecked -> 
            // Save auto sync pref
        }
    }

    private fun formatTime(time: Long): String {
        val sdf = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
        return "Last Sync: ${sdf.format(Date(time))}"
    }
}

data class SyncItem(val label: String, val success: Boolean, val timestamp: Long)

class SyncItemsAdapter(private val items: List<SyncItem>) : RecyclerView.Adapter<SyncItemsAdapter.SyncViewHolder>() {
    inner class SyncViewHolder(val view: android.view.View) : RecyclerView.ViewHolder(view) {
        val label: TextView = view.findViewById(R.id.itemSyncLabel)
        val status: TextView = view.findViewById(R.id.itemSyncStatus)
        val time: TextView = view.findViewById(R.id.itemSyncTime)
    }

    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): SyncViewHolder {
        val v = android.view.LayoutInflater.from(parent.context).inflate(R.layout.item_sync, parent, false)
        return SyncViewHolder(v)
    }

    override fun onBindViewHolder(holder: SyncViewHolder, position: Int) {
        val item = items[position]
        holder.label.text = item.label
        holder.status.text = if (item.success) "Success" else "Failed"
        holder.time.text = java.text.SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(item.timestamp))
    }

    override fun getItemCount(): Int = items.size
}
