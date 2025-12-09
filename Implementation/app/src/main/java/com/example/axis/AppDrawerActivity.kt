package com.example.axis

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.axis.adapters.AppsAdapter
import com.example.axis.models.AppInfo
import com.example.axis.utils.AppManager
import com.example.axis.utils.PreferenceManager

class AppDrawerActivity : BaseActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var searchEditText: EditText
    private lateinit var btnClearSearch: ImageButton
    private lateinit var btnGridView: ImageButton
    private lateinit var btnListView: ImageButton
    private lateinit var appsRecyclerView: RecyclerView
    private lateinit var emptyState: LinearLayout
    
    private lateinit var appManager: AppManager
    private lateinit var preferenceManager: PreferenceManager
    
    private var allApps: List<AppInfo> = listOf()
    private var filteredApps: List<AppInfo> = listOf()
    private var isGridView = true
    
    private lateinit var appsAdapter: AppsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_drawer)

        initializeViews()
        initializeManagers()
        setupToolbar()
        setupRecyclerView()
        loadApps()
        setupSearch()
    }

    private fun initializeViews() {
        toolbar = findViewById(R.id.toolbar)
        searchEditText = findViewById(R.id.searchEditText)
        btnClearSearch = findViewById(R.id.btnClearSearch)
        btnGridView = findViewById(R.id.btnGridView)
        btnListView = findViewById(R.id.btnListView)
        appsRecyclerView = findViewById(R.id.appsRecyclerView)
        emptyState = findViewById(R.id.emptyState)
    }

    private fun initializeManagers() {
        appManager = AppManager(this)
        preferenceManager = PreferenceManager(this)
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        
        // Setup view toggle buttons
        btnGridView.setOnClickListener {
            if (!isGridView) {
                isGridView = true
                updateLayoutManager()
            }
        }
        
        btnListView.setOnClickListener {
            if (isGridView) {
                isGridView = false
                updateLayoutManager()
            }
        }
    }
    
    private fun updateLayoutManager() {
        val columns = if (isGridView) preferenceManager.gridColumns else 1
        appsRecyclerView.layoutManager = GridLayoutManager(this, columns)
        appsAdapter.notifyDataSetChanged()
    }

    private fun setupRecyclerView() {
        val columns = preferenceManager.gridColumns
        appsRecyclerView.layoutManager = GridLayoutManager(this, columns)
        
        appsAdapter = AppsAdapter(
            apps = filteredApps,
            iconSize = preferenceManager.iconSize,
            showLabels = preferenceManager.showLabels,
            iconPack = preferenceManager.iconPack,
            onAppClick = { appInfo -> launchApp(appInfo) },
            onAppLongClick = { appInfo -> 
                showAppOptions(appInfo)
                true
            }
        )
        appsRecyclerView.adapter = appsAdapter
    }

    private fun loadApps() {
        allApps = appManager.getInstalledApps()
        filteredApps = allApps
        updateAppsList()
    }

    private fun setupSearch() {
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterApps(s.toString())
                btnClearSearch.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        btnClearSearch.setOnClickListener {
            searchEditText.text.clear()
        }
    }

    private fun filterApps(query: String) {
        filteredApps = if (query.isEmpty()) {
            allApps
        } else {
            allApps.filter { app ->
                app.appName.contains(query, ignoreCase = true)
            }
        }
        updateAppsList()
    }

    private fun updateAppsList() {
        appsAdapter = AppsAdapter(
            apps = filteredApps,
            iconSize = preferenceManager.iconSize,
            showLabels = preferenceManager.showLabels,
            iconPack = preferenceManager.iconPack,
            onAppClick = { appInfo -> launchApp(appInfo) },
            onAppLongClick = { appInfo -> 
                showAppOptions(appInfo)
                true
            }
        )
        appsRecyclerView.adapter = appsAdapter
        
        // Show empty state if no apps found
        if (filteredApps.isEmpty()) {
            emptyState.visibility = View.VISIBLE
            appsRecyclerView.visibility = View.GONE
        } else {
            emptyState.visibility = View.GONE
            appsRecyclerView.visibility = View.VISIBLE
        }
    }

    private fun launchApp(appInfo: AppInfo) {
        appManager.launchApp(appInfo.packageName)
        finish() // Close drawer after launching app
    }

    private fun showAppOptions(appInfo: AppInfo) {
        val bottomSheet = com.example.axis.dialogs.AppOptionsBottomSheet(appInfo) {
            loadApps() // Refresh when favorites change
        }
        bottomSheet.show(supportFragmentManager, com.example.axis.dialogs.AppOptionsBottomSheet.TAG)
    }
}
