package com.example.axis

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class NotificationsActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var notificationsRecycler: RecyclerView
    private val items = mutableListOf<NotificationItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)
        toolbar = findViewById(R.id.notificationsToolbar)
        notificationsRecycler = findViewById(R.id.notificationsRecycler)
        setupToolbar()
        seedData()
        setupRecycler()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }

    private fun seedData() {
        items.clear()
        items.add(NotificationItem("System", "Update completed successfully", System.currentTimeMillis()))
        items.add(NotificationItem("Apps", "5 apps updated", System.currentTimeMillis()))
        items.add(NotificationItem("Security", "No threats detected", System.currentTimeMillis()))
    }

    private fun setupRecycler() {
        notificationsRecycler.layoutManager = LinearLayoutManager(this)
        notificationsRecycler.adapter = NotificationsAdapter(items)
    }
}

data class NotificationItem(val category: String, val message: String, val timestamp: Long)

class NotificationsAdapter(private val items: List<NotificationItem>) : RecyclerView.Adapter<NotificationsAdapter.NotifViewHolder>() {
    inner class NotifViewHolder(val view: android.view.View) : RecyclerView.ViewHolder(view) {
        val category: android.widget.TextView = view.findViewById(R.id.itemNotifCategory)
        val message: android.widget.TextView = view.findViewById(R.id.itemNotifMessage)
        val time: android.widget.TextView = view.findViewById(R.id.itemNotifTime)
    }

    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): NotifViewHolder {
        val v = android.view.LayoutInflater.from(parent.context).inflate(R.layout.item_notification, parent, false)
        return NotifViewHolder(v)
    }

    override fun onBindViewHolder(holder: NotifViewHolder, position: Int) {
        val item = items[position]
        holder.category.text = item.category
        holder.message.text = item.message
        holder.time.text = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault()).format(java.util.Date(item.timestamp))
    }

    override fun getItemCount(): Int = items.size
}
