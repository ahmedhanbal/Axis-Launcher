package com.example.axis

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.axis.utils.PreferenceManager

class SignInActivity : AppCompatActivity() {
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnSignIn: Button
    private lateinit var tvSignUp: TextView
    private lateinit var tvForgotPassword: TextView
    private lateinit var ivBack: ImageView
    private lateinit var ivPasswordToggle: ImageView
    private lateinit var preferenceManager: PreferenceManager
    private var isPasswordVisible = false
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        
        preferenceManager = PreferenceManager(this)
        
        initViews()
        setupListeners()
    }
    
    private fun initViews() {
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnSignIn = findViewById(R.id.btnSignIn)
        tvSignUp = findViewById(R.id.tvSignUp)
        tvForgotPassword = findViewById(R.id.tvForgotPassword)
        ivBack = findViewById(R.id.ivBack)
        ivPasswordToggle = findViewById(R.id.ivPasswordToggle)
    }
    
    private fun setupListeners() {
        btnSignIn.setOnClickListener {
            handleSignIn()
        }
        
        tvSignUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
        
        tvForgotPassword.setOnClickListener {
            Toast.makeText(this, "Password reset feature coming soon", Toast.LENGTH_SHORT).show()
        }
        
        ivBack.setOnClickListener {
            finish()
        }
        
        ivPasswordToggle.setOnClickListener {
            togglePasswordVisibility()
        }
    }
    
    private fun handleSignIn() {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()
        
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }



        val call = com.example.axis.api.ApiClient.apiService.login(email, password)
        call.enqueue(object : retrofit2.Callback<com.example.axis.api.models.AuthResponse> {
            override fun onResponse(
                call: retrofit2.Call<com.example.axis.api.models.AuthResponse>,
                response: retrofit2.Response<com.example.axis.api.models.AuthResponse>
            ) {
                if (response.isSuccessful) {
                    val authResponse = response.body()
                    if (authResponse?.status == "success") {
                        val user = authResponse.user
                        if (user != null) {
                            preferenceManager.setUserEmail(user.email)
                            preferenceManager.setUserName(user.username)
                            preferenceManager.setUserId(user.id)
                            preferenceManager.setLoggedIn(true)
                            
                            user.profileImage?.let {
                                preferenceManager.setProfileImage(it)
                            }
                            
                            Toast.makeText(this@SignInActivity, "Login successful", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@SignInActivity, HomeActivity::class.java))
                            finish()
                        }
                    } else {
                        Toast.makeText(this@SignInActivity, authResponse?.message ?: "Login failed", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@SignInActivity, "Server error", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: retrofit2.Call<com.example.axis.api.models.AuthResponse>, t: Throwable) {
                Toast.makeText(this@SignInActivity, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    
    private fun togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible
        if (isPasswordVisible) {
            etPassword.inputType = android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            ivPasswordToggle.setImageResource(R.drawable.ic_visibility_on)
        } else {
            etPassword.inputType = android.text.InputType.TYPE_CLASS_TEXT or 
                                    android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
            ivPasswordToggle.setImageResource(R.drawable.ic_visibility_off)
        }
        etPassword.setSelection(etPassword.text.length)
    }
}
