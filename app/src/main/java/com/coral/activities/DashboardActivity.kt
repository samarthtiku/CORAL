package com.coral.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.coral.R
import com.coral.adapters.ActivityAdapter
import com.coral.models.ActivityLog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class DashboardActivity : AppCompatActivity() {
    private lateinit var welcomeText: TextView
    private lateinit var totalEmissionsText: TextView
    private lateinit var monthlyGoalText: TextView
    private lateinit var addActivityButton: Button
    private lateinit var activitiesRecyclerView: RecyclerView
    private lateinit var adapter: ActivityAdapter
    private var activities = mutableListOf<ActivityLog>()

    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference
    private val userId = auth.currentUser?.uid ?: ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // Set up action bar
        supportActionBar?.apply {
            title = "CORAL Dashboard"
            setDisplayHomeAsUpEnabled(false)
        }

        initializeViews()
        setupRecyclerView()
        setupAddActivityButton()
        loadActivities()
        updateWelcomeText()
        checkAuthState()
    }

    private fun initializeViews() {
        welcomeText = findViewById(R.id.welcomeText)
        totalEmissionsText = findViewById(R.id.totalEmissionsText)
        monthlyGoalText = findViewById(R.id.monthlyGoalText)
        addActivityButton = findViewById(R.id.addActivityButton)
        activitiesRecyclerView = findViewById(R.id.activitiesRecyclerView)
    }

    private fun updateWelcomeText() {
        val userName = auth.currentUser?.displayName ?: "User"
        welcomeText.text = "Welcome, $userName!"
    }

    private fun setupRecyclerView() {
        adapter = ActivityAdapter(activities) { activity ->
            deleteActivity(activity)
        }

        activitiesRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@DashboardActivity)
            adapter = this@DashboardActivity.adapter
        }
    }

    private fun setupAddActivityButton() {
        addActivityButton.setOnClickListener {
            startActivity(Intent(this, AddActivityLogActivity::class.java))
        }
    }

    private fun checkAuthState() {
        if (auth.currentUser == null) {
            navigateToSignIn()
        }
    }

    private fun navigateToSignIn() {
        val intent = Intent(this, SignInActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.dashboard_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_sign_out -> {
                showSignOutDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showSignOutDialog() {
        AlertDialog.Builder(this)
            .setTitle("Sign Out")
            .setMessage("Are you sure you want to sign out?")
            .setPositiveButton("Yes") { _, _ ->
                signOut()
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun signOut() {
        FirebaseAuth.getInstance().signOut()
        navigateToSignIn()
    }

    private fun loadActivities() {
        database.child("users").child(userId).child("activities")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    activities.clear()
                    var totalEmissions = 0.0

                    snapshot.children.forEach { child ->
                        child.getValue(ActivityLog::class.java)?.let { activity ->
                            activities.add(activity)
                            totalEmissions += activity.emission
                        }
                    }

                    // Sort activities by timestamp (most recent first)
                    activities.sortByDescending { it.timestamp }

                    updateDashboard(activities, totalEmissions)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@DashboardActivity,
                        "Error loading activities: ${error.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
    }

    private fun deleteActivity(activity: ActivityLog) {
        AlertDialog.Builder(this)
            .setTitle("Delete Activity")
            .setMessage("Are you sure you want to delete this activity?")
            .setPositiveButton("Delete") { _, _ ->
                performDelete(activity)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun performDelete(activity: ActivityLog) {
        database.child("users")
            .child(userId)
            .child("activities")
            .child(activity.id)
            .removeValue()
            .addOnSuccessListener {
                Toast.makeText(this, "Activity deleted", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    this,
                    "Failed to delete: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
    }

    private fun updateDashboard(activities: List<ActivityLog>, totalEmissions: Double) {
        // Update total emissions display
        totalEmissionsText.text = String.format("%.2f kg CO₂", totalEmissions)

        // Calculate and update monthly goal
        // You can implement your own logic for calculating the monthly goal
        val monthlyGoal = calculateMonthlyGoal(totalEmissions)
        monthlyGoalText.text = String.format("Monthly Goal: %.1f%%", monthlyGoal)

        // Update RecyclerView
        adapter.updateActivities(activities)
    }

    private fun calculateMonthlyGoal(totalEmissions: Double): Double {
        // Implement your monthly goal calculation logic here
        // This is a placeholder implementation
        val monthlyTarget = 100.0 // kg CO₂
        return (totalEmissions / monthlyTarget) * 100
    }

    override fun onResume() {
        super.onResume()
        // Refresh activities when returning to dashboard
        loadActivities()
        updateWelcomeText()
    }

    companion object {
        private const val TAG = "DashboardActivity"
    }
}