package com.example.axis

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.axis.adapters.AppsAdapter
import com.example.axis.models.AppInfo
import com.example.axis.utils.AppManager
import com.example.axis.utils.PreferenceManager
import com.example.axis.utils.SyncManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.*

class HomeActivity : BaseActivity() {

    private lateinit var favoriteAppsRecycler: RecyclerView
    private lateinit var recentAppsRecycler: RecyclerView
    private lateinit var greetingText: TextView
    private lateinit var dateText: TextView
    private lateinit var fabAppDrawer: FloatingActionButton
    private lateinit var searchBar: LinearLayout
    private lateinit var btnSettings: ImageButton
    
    private lateinit var appManager: AppManager
    private lateinit var preferenceManager: PreferenceManager
    private lateinit var syncManager: SyncManager
    
    private var allApps: List<AppInfo> = listOf()
    private var favoriteApps: List<AppInfo> = listOf()

    private lateinit var gestureDetector: android.view.GestureDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        initializeViews()
        initializeManagers()
        setupRecyclerViews()
        loadApps()
        updateGreeting()
        setupClickListeners()
        setupGestures()
        
        setupClickListeners()
        setupGestures()
        
        checkNotificationPermission()
    }

    private fun checkNotificationPermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (androidx.core.content.ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != android.content.pm.PackageManager.PERMISSION_GRANTED
            ) {
                androidx.core.app.ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    101
                )
            } else {
                showReviewSettingsNotification()
            }
        } else {
            showReviewSettingsNotification()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 101) {
            if (grantResults.isNotEmpty() && grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                showReviewSettingsNotification()
            }
        }
    }

    private fun showReviewSettingsNotification() {
        val notificationManager = getSystemService(android.content.Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
        val channelId = "local_notification_channel"
        
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = android.app.NotificationChannel(
                channelId,
                "Local Notifications",
                android.app.NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(this, SettingsActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = android.app.PendingIntent.getActivity(
            this, 100, intent,
            android.app.PendingIntent.FLAG_IMMUTABLE or android.app.PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = androidx.core.app.NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Review your settings")
            .setContentText("Tap to customize your launcher experience.")
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(1001, notification)
    }

    private fun setupGestures() {
        gestureDetector = android.view.GestureDetector(this, object : android.view.GestureDetector.SimpleOnGestureListener() {
            override fun onFling(e1: android.view.MotionEvent?, e2: android.view.MotionEvent, velocityX: Float, velocityY: Float): Boolean {
                if (e1 == null) return false
                val diffY = e2.y - e1.y
                val diffX = e2.x - e1.x
                if (Math.abs(diffY) > Math.abs(diffX)) {
                    if (Math.abs(diffY) > 100 && Math.abs(velocityY) > 100) {
                        if (diffY < 0) {
                            // Swipe Up
                            startActivity(Intent(this@HomeActivity, AppDrawerActivity::class.java))
                            return true
                        } else {
                            // Swipe Down - Expand Notifications
                            try {
                                val service = getSystemService("statusbar")
                                val statusbarManager = Class.forName("android.app.StatusBarManager")
                                val expand = statusbarManager.getMethod("expandNotificationsPanel")
                                expand.invoke(service)
                            } catch (e: Exception) {
                                // Fallback or ignore
                            }
                            return true
                        }
                    }
                }
                return false
            }
            
            override fun onDoubleTap(e: android.view.MotionEvent): Boolean {
                // Double tap to lock (requires admin) or just show settings
                startActivity(Intent(this@HomeActivity, SettingsActivity::class.java))
                return true
            }
        })
    }

    override fun onTouchEvent(event: android.view.MotionEvent?): Boolean {
        return if (event != null) {
            gestureDetector.onTouchEvent(event) || super.onTouchEvent(event)
        } else {
            super.onTouchEvent(event)
        }
    }

    override fun onResume() {
        super.onResume()
        // Refresh apps in case any were installed/uninstalled
        loadApps()
    }


    private fun initializeViews() {
        favoriteAppsRecycler = findViewById(R.id.favoriteAppsRecycler)
        recentAppsRecycler = findViewById(R.id.recentAppsRecycler)
        greetingText = findViewById(R.id.greetingText)
        dateText = findViewById(R.id.dateText)
        fabAppDrawer = findViewById(R.id.fabAppDrawer)
        searchBar = findViewById(R.id.searchBar)
        btnSettings = findViewById(R.id.btnSettings)
    }

    private fun initializeManagers() {
        appManager = AppManager(this)
        preferenceManager = PreferenceManager(this)
        syncManager = SyncManager(this)
        
        // Fetch latest data from backend
        syncManager.fetchSettings()
        syncManager.fetchFavorites()
    }

    private fun setupRecyclerViews() {
        // Favorite apps - horizontal scroll
        favoriteAppsRecycler.layoutManager = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
        
        // Recent apps - grid layout
        val columns = preferenceManager.gridColumns
        recentAppsRecycler.layoutManager = GridLayoutManager(this, columns)
    }

    private fun loadApps() {
        allApps = appManager.getInstalledApps()
        
        // Get favorite apps
        val favoritePackages = preferenceManager.getFavoriteApps()
        favoriteApps = allApps.filter { favoritePackages.contains(it.packageName) }
        
        // Update adapters
        updateFavoriteApps()
        updateRecentApps()
    }

    private fun updateFavoriteApps() {
        favoriteAppsRecycler.adapter = AppsAdapter(
            apps = favoriteApps.take(10),
            iconSize = preferenceManager.iconSize,
            showLabels = preferenceManager.showLabels,
            iconPack = preferenceManager.iconPack,
            onAppClick = { appInfo -> launchApp(appInfo) },
            onAppLongClick = { appInfo -> 
                showAppOptions(appInfo)
                true
            }
        )
    }

    private fun updateRecentApps() {
        val isDrawerMode = preferenceManager.drawerMode
        val appsToShow = if (isDrawerMode) {
            // Drawer Mode: Show only recent/top apps (limit to 12)
            allApps.take(12)
        } else {
            // Classic Mode: Show ALL apps
            allApps
        }

        // Update Header Visibility
        val recentAppsHeader = findViewById<TextView>(R.id.viewAllApps).parent as android.view.View
        if (isDrawerMode) {
            recentAppsHeader.visibility = android.view.View.VISIBLE
        } else {
            // In Classic mode, we don't need "Recent Apps" header or "View All" button
            // But we might want to keep the "Recent Apps" title or change it to "All Apps"
            // For now, let's hide the "View All" button but keep the title as "All Apps"
            findViewById<TextView>(R.id.viewAllApps).visibility = android.view.View.GONE
            // We need to find the "Recent Apps" TextView. It doesn't have an ID in the layout XML provided earlier,
            // but it's the first child of the RelativeLayout.
            // Let's assume we can just hide the whole header row for a cleaner look, or rename it.
            // Actually, looking at layout, it's a RelativeLayout containing two TextViews.
            // Let's just update the adapter first.
        }

        recentAppsRecycler.adapter = AppsAdapter(
            apps = appsToShow,
            iconSize = preferenceManager.iconSize,
            showLabels = preferenceManager.showLabels,
            iconPack = preferenceManager.iconPack,
            onAppClick = { appInfo -> launchApp(appInfo) },
            onAppLongClick = { appInfo -> 
                showAppOptions(appInfo)
                true
            }
        )
    }

    private fun updateGreeting() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        
        val greeting = when (hour) {
            in 0..11 -> "Good Morning"
            in 12..16 -> "Good Afternoon"
            else -> "Good Evening"
        }
        
        val userName = preferenceManager.getUserName()
        greetingText.text = "$greeting, $userName"
        
        val dateFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
        dateText.text = dateFormat.format(Date())
        
        // Update Profile Image
        val profileImageBase64 = preferenceManager.getProfileImage()
        val ivProfile = findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.ivProfile)
        if (!profileImageBase64.isNullOrEmpty()) {
            try {
                val decodedString = android.util.Base64.decode(profileImageBase64, android.util.Base64.DEFAULT)
                val decodedByte = android.graphics.BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                ivProfile.setImageBitmap(decodedByte)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun setupClickListeners() {
        if (preferenceManager.drawerMode) {
            fabAppDrawer.visibility = android.view.View.VISIBLE
            fabAppDrawer.setOnClickListener {
                startActivity(Intent(this, AppDrawerActivity::class.java))
            }
        } else {
            fabAppDrawer.visibility = android.view.View.GONE
            // In Classic mode, we might want to show all apps in the main grid or handle pagination.
            // For now, hiding the FAB is the first step.
        }

        searchBar.setOnClickListener {
            startActivity(Intent(this, AppDrawerActivity::class.java))
        }

        btnSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        findViewById<ImageButton>(R.id.btnNotifications).setOnClickListener {
            startActivity(Intent(this, NotificationsActivity::class.java))
        }

        findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.ivProfile).setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        findViewById<TextView>(R.id.viewAllApps).setOnClickListener {
            startActivity(Intent(this, AppDrawerActivity::class.java))
        }
    }

    private fun launchApp(appInfo: AppInfo) {
        appManager.launchApp(appInfo.packageName)
    }

    private fun showAppOptions(appInfo: AppInfo) {
        val bottomSheet = com.example.axis.dialogs.AppOptionsBottomSheet(appInfo) {
            loadApps() // Refresh when favorites change
        }
        bottomSheet.show(supportFragmentManager, com.example.axis.dialogs.AppOptionsBottomSheet.TAG)
    }


}
