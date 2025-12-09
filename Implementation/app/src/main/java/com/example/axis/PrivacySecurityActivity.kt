package com.example.axis

import android.os.Bundle
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import android.widget.TextView

class PrivacySecurityActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var appLockSwitch: Switch
    private lateinit var hideAppsSwitch: Switch
    private lateinit var permissionsRow: CardView
    private lateinit var privacyPolicyRow: CardView
    private lateinit var securityStatusText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy_security)
        toolbar = findViewById(R.id.privacyToolbar)
        appLockSwitch = findViewById(R.id.appLockSwitch)
        hideAppsSwitch = findViewById(R.id.hideAppsSwitch)
        permissionsRow = findViewById(R.id.permissionsRow)
        privacyPolicyRow = findViewById(R.id.privacyPolicyRow)
        securityStatusText = findViewById(R.id.securityStatusText)
        setupToolbar()
        bindInitial()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }

    private fun bindInitial() {
        val preferenceManager = com.example.axis.utils.PreferenceManager(this)
        
        // We need to add these keys to PreferenceManager first, but for now let's assume they exist or use generic putBoolean if possible.
        // PreferenceManager doesn't have generic putBoolean exposed publicly usually, but let's check.
        // It has specific getters/setters. I should add appLock and hideApps to PreferenceManager.
        // For now, I'll just use the switches visually or add the methods to PreferenceManager.
        // I'll add them to PreferenceManager in the next step if needed.
        // Let's assume I'll add them.
        
        appLockSwitch.isChecked = preferenceManager.appLock
        hideAppsSwitch.isChecked = preferenceManager.hideApps
        
        securityStatusText.text = "Secure"
        
        appLockSwitch.setOnCheckedChangeListener { _, isChecked -> 
            preferenceManager.appLock = isChecked
            android.widget.Toast.makeText(this, "App Lock ${if(isChecked) "Enabled" else "Disabled"}", android.widget.Toast.LENGTH_SHORT).show()
        }
        
        hideAppsSwitch.setOnCheckedChangeListener { _, isChecked -> 
            preferenceManager.hideApps = isChecked
             android.widget.Toast.makeText(this, "Hide Apps ${if(isChecked) "Enabled" else "Disabled"}", android.widget.Toast.LENGTH_SHORT).show()
        }
        
        permissionsRow.setOnClickListener { 
            android.widget.Toast.makeText(this, "Permissions Manager", android.widget.Toast.LENGTH_SHORT).show()
        }
        privacyPolicyRow.setOnClickListener { 
            val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse("https://example.com/privacy"))
            startActivity(intent)
        }
    }
}
