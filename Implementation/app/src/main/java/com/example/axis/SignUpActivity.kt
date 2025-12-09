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

class SignUpActivity : AppCompatActivity() {
    private lateinit var etFullName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPhone: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnSignUp: Button
    private lateinit var tvSignIn: TextView
    private lateinit var ivBack: ImageView
    private lateinit var ivPasswordToggle: ImageView
    private lateinit var preferenceManager: PreferenceManager
    private var isPasswordVisible = false
    
    private lateinit var ivProfileImage: de.hdodenhof.circleimageview.CircleImageView
    private lateinit var tvAddPhoto: TextView
    private var selectedImageBase64: String? = null

    private val pickImageLauncher = registerForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.GetContent()
    ) { uri: android.net.Uri? ->
        uri?.let {
            ivProfileImage.setImageURI(it)
            // Convert to Base64 with compression
            try {
                val inputStream = contentResolver.openInputStream(it)
                val originalBitmap = android.graphics.BitmapFactory.decodeStream(inputStream)
                inputStream?.close()

                if (originalBitmap != null) {
                    // Resize if too large (max 800px)
                    val maxDimension = 800
                    val ratio = Math.min(
                        maxDimension.toDouble() / originalBitmap.width,
                        maxDimension.toDouble() / originalBitmap.height
                    )
                    
                    val finalBitmap = if (ratio < 1.0) {
                        val width = (originalBitmap.width * ratio).toInt()
                        val height = (originalBitmap.height * ratio).toInt()
                        android.graphics.Bitmap.createScaledBitmap(originalBitmap, width, height, true)
                    } else {
                        originalBitmap
                    }

                    // Compress to JPEG
                    val outputStream = java.io.ByteArrayOutputStream()
                    finalBitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 70, outputStream)
                    val bytes = outputStream.toByteArray()
                    
                    selectedImageBase64 = android.util.Base64.encodeToString(bytes, android.util.Base64.DEFAULT)
                    
                    // Cleanup
                    if (finalBitmap != originalBitmap) {
                        finalBitmap.recycle()
                    }
                    originalBitmap.recycle()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Error processing image", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        
        preferenceManager = PreferenceManager(this)
        
        initViews()
        setupListeners()
    }
    
    private fun initViews() {
        etFullName = findViewById(R.id.etFullName)
        etEmail = findViewById(R.id.etEmail)
        etPhone = findViewById(R.id.etPhone)
        etPassword = findViewById(R.id.etPassword)
        btnSignUp = findViewById(R.id.btnSignUp)
        tvSignIn = findViewById(R.id.tvSignIn)
        ivBack = findViewById(R.id.ivBack)
        ivPasswordToggle = findViewById(R.id.ivPasswordToggle)
        ivProfileImage = findViewById(R.id.ivProfileImage)
        tvAddPhoto = findViewById(R.id.tvAddPhoto)
    }
    
    private fun setupListeners() {
        btnSignUp.setOnClickListener {
            handleSignUp()
        }
        
        tvSignIn.setOnClickListener {
            finish()
        }
        
        ivBack.setOnClickListener {
            finish()
        }
        
        ivPasswordToggle.setOnClickListener {
            togglePasswordVisibility()
        }
        
        ivProfileImage.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }
        
        tvAddPhoto.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }
    }
    
    private fun handleSignUp() {
        val fullName = etFullName.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val phone = etPhone.text.toString().trim() // Not used in backend yet, but kept for UI
        val password = etPassword.text.toString().trim()
        
        if (fullName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }
        
        if (!email.contains("@")) {
            Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show()
            return
        }
        
        if (password.length < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
            return
        }
        
        val call = com.example.axis.api.ApiClient.apiService.signup(fullName, email, password, selectedImageBase64)
        call.enqueue(object : retrofit2.Callback<com.example.axis.api.models.AuthResponse> {
            override fun onResponse(
                call: retrofit2.Call<com.example.axis.api.models.AuthResponse>,
                response: retrofit2.Response<com.example.axis.api.models.AuthResponse>
            ) {
                if (response.isSuccessful) {
                    val authResponse = response.body()
                    if (authResponse?.status == "success") {
                        preferenceManager.setUserEmail(email)
                        preferenceManager.setUserName(fullName)
                        preferenceManager.setLoggedIn(true)
                        
                        // Save User ID
                        authResponse.userId?.let {
                            preferenceManager.setUserId(it)
                        }
                        
                        selectedImageBase64?.let {
                            preferenceManager.setProfileImage(it)
                        }
                        
                        Toast.makeText(this@SignUpActivity, "Account created successfully", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@SignUpActivity, HomeActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@SignUpActivity, authResponse?.message ?: "Signup failed", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@SignUpActivity, "Server error", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: retrofit2.Call<com.example.axis.api.models.AuthResponse>, t: Throwable) {
                Toast.makeText(this@SignUpActivity, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
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
