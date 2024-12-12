package com.coral.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.coral.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.database.FirebaseDatabase
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textfield.TextInputEditText

class SignInActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var emailInputLayout: TextInputLayout
    private lateinit var passwordInputLayout: TextInputLayout
    private lateinit var emailInput: TextInputEditText
    private lateinit var passwordInput: TextInputEditText
    private lateinit var signInButton: Button
    private lateinit var signUpButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var welcomeText: TextView
    private lateinit var noAccountText: TextView

    companion object {
        private const val TAG = "SignInActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Initialize views
        initializeViews()

        // Setup UI elements
        setupUI()

        // Check if user is already signed in
        checkAuthState()
    }

    private fun initializeViews() {
        emailInputLayout = findViewById(R.id.emailInputLayout)
        passwordInputLayout = findViewById(R.id.passwordInputLayout)
        emailInput = findViewById(R.id.emailInput)
        passwordInput = findViewById(R.id.passwordInput)
        signInButton = findViewById(R.id.signInButton)
        signUpButton = findViewById(R.id.signUpButton)
        progressBar = findViewById(R.id.progressBar)
        welcomeText = findViewById(R.id.welcomeText)
        noAccountText = findViewById(R.id.noAccountText)

        // Set initial texts
        welcomeText.text = getString(R.string.welcome_coral)
        noAccountText.text = getString(R.string.no_account)
    }

    private fun setupUI() {
        signInButton.setOnClickListener {
            if (validateInputs()) {
                signIn()
            }
        }

        signUpButton.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        // Clear errors on text change
        emailInput.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                emailInputLayout.error = null
            }
        }

        passwordInput.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                passwordInputLayout.error = null
            }
        }
    }

    private fun checkAuthState() {
        auth.currentUser?.let {
            // User is already signed in, navigate to Dashboard
            navigateToDashboard()
        }
    }

    private fun validateInputs(): Boolean {
        val email = emailInput.text.toString().trim()
        val password = passwordInput.text.toString()
        var isValid = true

        // Email validation
        if (email.isEmpty()) {
            emailInputLayout.error = getString(R.string.error_required_field)
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInputLayout.error = getString(R.string.error_invalid_email)
            isValid = false
        } else {
            emailInputLayout.error = null
        }

        // Password validation
        if (password.isEmpty()) {
            passwordInputLayout.error = getString(R.string.error_required_field)
            isValid = false
        } else if (password.length < 6) {
            passwordInputLayout.error = getString(R.string.error_invalid_password)
            isValid = false
        } else {
            passwordInputLayout.error = null
        }

        return isValid
    }

    private fun signIn() {
        showLoading()
        val email = emailInput.text.toString().trim()
        val password = passwordInput.text.toString()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                hideLoading()
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmail:success")
                    Toast.makeText(this, getString(R.string.success_sign_in), Toast.LENGTH_SHORT).show()
                    navigateToDashboard()
                } else {
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    handleAuthError(task.exception)
                }
            }
    }

    private fun handleAuthError(exception: Exception?) {
        val errorMessage = when (exception) {
            is FirebaseAuthInvalidUserException -> getString(R.string.error_user_not_found)
            is FirebaseAuthInvalidCredentialsException -> getString(R.string.error_invalid_password)
            is FirebaseAuthWeakPasswordException -> getString(R.string.error_weak_password)
            is FirebaseAuthUserCollisionException -> getString(R.string.error_user_exists)
            is FirebaseNetworkException -> getString(R.string.error_network)
            else -> getString(R.string.error_auth_failed)
        }
        showError(errorMessage)
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun showLoading() {
        progressBar.visibility = View.VISIBLE
        signInButton.isEnabled = false
        signUpButton.isEnabled = false
        signInButton.text = getString(R.string.loading_sign_in)
    }

    private fun hideLoading() {
        progressBar.visibility = View.GONE
        signInButton.isEnabled = true
        signUpButton.isEnabled = true
        signInButton.text = getString(R.string.sign_in)
    }

    private fun navigateToDashboard() {
        val intent = Intent(this, DashboardActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in when activity starts
        checkAuthState()
    }
}