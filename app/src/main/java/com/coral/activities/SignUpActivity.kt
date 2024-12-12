package com.coral.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.coral.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.concurrent.TimeUnit

class SignUpActivity : AppCompatActivity() {
    private companion object {
        private const val TAG = "SignUpActivity"
        private const val TIMEOUT_DURATION = 15L // seconds
    }

    private lateinit var emailField: EditText
    private lateinit var passwordField: EditText
    private lateinit var signUpButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var auth: FirebaseAuth
    private val timeoutHandler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = FirebaseAuth.getInstance()
        initializeViews()
        setupClickListeners()
    }

    private fun initializeViews() {
        emailField = findViewById(R.id.emailField)
        passwordField = findViewById(R.id.passwordField)
        signUpButton = findViewById(R.id.btnSignUp)
        progressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.GONE
    }

    private fun setupClickListeners() {
        signUpButton.setOnClickListener {
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()

            if (validateInputs(email, password)) {
                createAccount(email, password)
            }
        }
    }

    private fun validateInputs(email: String, password: String): Boolean {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailField.error = "Please enter a valid email"
            return false
        }

        if (password.length < 6) {
            passwordField.error = "Password must be at least 6 characters"
            return false
        }

        return true
    }

    private fun createAccount(email: String, password: String) {
        showLoading(true)

        // Set a timeout to prevent indefinite loading
        val timeoutRunnable = Runnable {
            if (!isFinishing) {
                showLoading(false)
                showError("Sign up is taking too long. Please check your internet connection and try again.")
            }
        }
        timeoutHandler.postDelayed(timeoutRunnable, TimeUnit.SECONDS.toMillis(TIMEOUT_DURATION))

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                // Remove timeout since we got a response
                timeoutHandler.removeCallbacks(timeoutRunnable)

                val user = authResult.user
                if (user != null) {
                    createUserProfile(user.uid, email)
                } else {
                    showLoading(false)
                    showError("Failed to create user profile")
                }
            }
            .addOnFailureListener { exception ->
                // Remove timeout since we got a response
                timeoutHandler.removeCallbacks(timeoutRunnable)

                Log.e(TAG, "Account creation failed", exception)
                showLoading(false)
                showError(exception.message ?: "Failed to create account")
            }
    }

    private fun createUserProfile(userId: String, email: String) {
        val userProfile = hashMapOf(
            "email" to email,
            "createdAt" to System.currentTimeMillis()
        )

        FirebaseDatabase.getInstance().reference
            .child("users")
            .child(userId)
            .setValue(userProfile)
            .addOnSuccessListener {
                Log.d(TAG, "User profile created successfully")
                navigateToDashboard()
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Failed to create user profile", exception)
                showLoading(false)
                showError("Account created but failed to set up profile. Please try signing in.")
                navigateToSignIn()
            }
    }

    private fun showLoading(isLoading: Boolean) {
        runOnUiThread {
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            signUpButton.isEnabled = !isLoading
            signUpButton.text = if (isLoading) "Creating Account..." else "Sign Up"
            emailField.isEnabled = !isLoading
            passwordField.isEnabled = !isLoading
        }
    }

    private fun showError(message: String) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }
    }

    private fun navigateToDashboard() {
        val intent = Intent(this, DashboardActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }

    private fun navigateToSignIn() {
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Clean up any pending timeouts
        timeoutHandler.removeCallbacksAndMessages(null)
    }
}