package com.example.axis

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.axis.adapters.OnboardingAdapter
import com.example.axis.models.OnboardingItem
import com.example.axis.utils.PreferenceManager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class OnboardingActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var btnNext: Button
    private lateinit var tvSkip: TextView
    private lateinit var preferenceManager: PreferenceManager
    
    private val onboardingItems = listOf(
        OnboardingItem(
            title = "Fast & Efficient",
            description = "Launch your favorite apps instantly with our lightning-fast interface",
            icon = R.drawable.ic_onboarding_speed,
            backgroundColor = R.color.onboarding_blue
        ),
        OnboardingItem(
            title = "Fully Customizable",
            description = "Personalize your home screen with themes, widgets, and layouts",
            icon = R.drawable.ic_onboarding_customize,
            backgroundColor = R.color.onboarding_purple
        ),
        OnboardingItem(
            title = "Cloud Sync",
            description = "Your settings sync across all your devices seamlessly",
            icon = R.drawable.ic_onboarding_cloud,
            backgroundColor = R.color.onboarding_green
        )
    )
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        preferenceManager = PreferenceManager(this)
        
        // Check if onboarding already completed
        if (preferenceManager.isOnboardingCompleted()) {
            navigateToAuth()
            return
        }
        
        setContentView(R.layout.activity_onboarding)
        
        viewPager = findViewById(R.id.viewPager)
        tabLayout = findViewById(R.id.tabLayout)
        btnNext = findViewById(R.id.btnNext)
        tvSkip = findViewById(R.id.tvSkip)
        
        setupViewPager()
        setupListeners()
    }
    
    private fun setupViewPager() {
        val adapter = OnboardingAdapter(onboardingItems)
        viewPager.adapter = adapter
        
        TabLayoutMediator(tabLayout, viewPager) { _, _ -> }.attach()
    }
    
    private fun setupListeners() {
        btnNext.setOnClickListener {
            if (viewPager.currentItem < onboardingItems.size - 1) {
                viewPager.currentItem += 1
            } else {
                completeOnboarding()
            }
        }
        
        tvSkip.setOnClickListener {
            completeOnboarding()
        }
        
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (position == onboardingItems.size - 1) {
                    btnNext.text = "Get Started"
                    tvSkip.visibility = TextView.GONE
                } else {
                    btnNext.text = "Next"
                    tvSkip.visibility = TextView.VISIBLE
                }
            }
        })
    }
    
    private fun completeOnboarding() {
        preferenceManager.setOnboardingCompleted(true)
        navigateToAuth()
    }
    
    private fun navigateToAuth() {
        startActivity(Intent(this, SignInActivity::class.java))
        finish()
    }
}
