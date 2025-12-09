package com.example.axis

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.axis.utils.PreferenceManager

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        val preferenceManager = PreferenceManager(this)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = when {
                !preferenceManager.isOnboardingCompleted() -> Intent(this, OnboardingActivity::class.java)
                !preferenceManager.isLoggedIn() -> Intent(this, SignInActivity::class.java)
                else -> Intent(this, HomeActivity::class.java)
            }
            startActivity(intent)
            finish()
        }, 3000)
    }
}
