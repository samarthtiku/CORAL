package com.coral.activities

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.coral.R
import com.coral.utils.CarbonCalculator
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue

class AddActivityLogActivity : AppCompatActivity() {
    // View declarations
    private lateinit var activityTypeLayout: TextInputLayout
    private lateinit var activityTypeDropdown: AutoCompleteTextView
    private lateinit var valueInputLayout: TextInputLayout
    private lateinit var valueInput: TextInputEditText
    private lateinit var previewEmissionsText: TextInputEditText
    private lateinit var btnSaveActivity: MaterialButton

    // Firebase instances
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_activity_log)

        // Setup action bar
        supportActionBar?.apply {
            title = getString(R.string.add_activity_log)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        initializeViews()
        setupActivityDropdown()
        setupValueInput()
        setupSaveButton()
    }

    private fun initializeViews() {
        activityTypeLayout = findViewById(R.id.activityTypeLayout)
        activityTypeDropdown = findViewById(R.id.activityTypeDropdown)
        valueInputLayout = findViewById(R.id.valueInputLayout)
        valueInput = findViewById(R.id.valueInput)
        previewEmissionsText = findViewById(R.id.previewEmissionsText)
        btnSaveActivity = findViewById(R.id.btnSaveActivity)

        // Set initial state of preview
        previewEmissionsText.setText("0.00 kg CO₂")
    }

    private fun setupActivityDropdown() {
        // Get activities list and sort them
        val activities = CarbonCalculator.getAllActivities().sorted()
        val adapter = ArrayAdapter(this, R.layout.dropdown_item, activities)
        activityTypeDropdown.setAdapter(adapter)

        activityTypeDropdown.setOnItemClickListener { _, _, _, _ ->
            updateInputHint()
            updateEmissionPreview()
            // Clear any previous error
            activityTypeLayout.error = null
        }
    }

    private fun setupValueInput() {
        valueInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Clear error when user starts typing
                valueInputLayout.error = null
            }
            override fun afterTextChanged(s: Editable?) {
                updateEmissionPreview()
            }
        })
    }

    private fun updateInputHint() {
        val selectedActivity = activityTypeDropdown.text.toString()
        val hint = CarbonCalculator.getInputHint(selectedActivity)
        valueInputLayout.hint = hint
        valueInput.hint = hint  // Also update EditText hint
    }

    private fun updateEmissionPreview() {
        val activityType = activityTypeDropdown.text.toString()
        val valueStr = valueInput.text.toString()

        if (activityType.isNotEmpty() && valueStr.isNotEmpty()) {
            try {
                val value = valueStr.toDouble()
                if (value < 0) {
                    previewEmissionsText.setText("Please enter a positive value")
                    return
                }
                val emission = CarbonCalculator.calculateEmission(activityType, value)
                val impact = CarbonCalculator.getImpactDescription(activityType, emission)
                previewEmissionsText.setText(String.format("%.2f kg CO₂\n%s", emission, impact))
            } catch (e: NumberFormatException) {
                previewEmissionsText.setText("Invalid input")
            }
        } else {
            previewEmissionsText.setText("0.00 kg CO₂")
        }
    }

    private fun setupSaveButton() {
        btnSaveActivity.setOnClickListener {
            if (validateInputs()) {
                saveActivity()
            }
        }
    }

    private fun validateInputs(): Boolean {
        var isValid = true

        val activityType = activityTypeDropdown.text.toString()
        if (activityType.isEmpty()) {
            activityTypeLayout.error = "Please select an activity"
            isValid = false
        }

        val valueStr = valueInput.text.toString()
        if (valueStr.isEmpty()) {
            valueInputLayout.error = "Please enter a value"
            isValid = false
        } else {
            try {
                val value = valueStr.toDouble()
                if (value <= 0) {
                    valueInputLayout.error = "Please enter a positive value"
                    isValid = false
                }
            } catch (e: NumberFormatException) {
                valueInputLayout.error = "Please enter a valid number"
                isValid = false
            }
        }

        return isValid
    }

    private fun saveActivity() {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            Toast.makeText(this, "Please sign in to save activities", Toast.LENGTH_LONG).show()
            return
        }

        // Disable save button while saving
        btnSaveActivity.isEnabled = false

        val activityType = activityTypeDropdown.text.toString()
        val value = valueInput.text.toString().toDouble()
        val emission = CarbonCalculator.calculateEmission(activityType, value)

        // Generate activity ID
        val activityId = database.child("users").child(userId).child("activities").push().key
        if (activityId == null) {
            Toast.makeText(this, "Error generating activity ID", Toast.LENGTH_LONG).show()
            btnSaveActivity.isEnabled = true
            return
        }

        // Create activity data matching Firebase rules
        val activityData = hashMapOf(
            "id" to activityId,
            "activityType" to activityType,
            "value" to value,
            "emission" to emission,
            "timestamp" to ServerValue.TIMESTAMP
        )

        // Save to Firebase
        database.child("users").child(userId).child("activities").child(activityId)
            .setValue(activityData)
            .addOnSuccessListener {
                Toast.makeText(this, "Activity saved successfully", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                btnSaveActivity.isEnabled = true
            }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}