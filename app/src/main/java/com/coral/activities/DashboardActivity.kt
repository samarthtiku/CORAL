package com.coral.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.coral.R
import com.coral.adapters.ActivityAdapter
import com.coral.databinding.ActivityDashboardBinding
import com.coral.models.ActivityLog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var adapter: ActivityAdapter
    private var activities = mutableListOf<ActivityLog>()
    private var valueEventListener: ValueEventListener? = null

    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference
    private val userId = auth.currentUser?.uid ?: ""

    companion object {
        private const val TAG = "DashboardActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        checkAuthState()
    }

    private fun setupUI() {
        setupToolbar()
        setupRecyclerView()
        setupAddActivityButton()
        updateWelcomeText()
    }

    private fun setupToolbar() {
        supportActionBar?.title = getString(R.string.dashboard)
    }

    private fun setupRecyclerView() {
        adapter = ActivityAdapter(activities) { activity ->
            showDeleteDialog(activity)
        }
        binding.activitiesRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@DashboardActivity)
            adapter = this@DashboardActivity.adapter
        }
    }

    private fun setupAddActivityButton() {
        binding.addActivityButton.setOnClickListener {
            startActivity(Intent(this, AddActivityLogActivity::class.java))
        }
    }

    private fun loadActivities() {
        if (auth.currentUser == null) {
            navigateToSignIn()
            return
        }

        valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    activities.clear()
                    var totalEmissions = 0.0
                    snapshot.children.forEach { child ->
                        child.getValue(ActivityLog::class.java)?.let { activity ->
                            activities.add(activity)
                            totalEmissions += activity.emission
                        }
                    }
                    activities.sortByDescending { it.timestamp }
                    updateDashboard(activities, totalEmissions)
                } catch (e: Exception) {
                    Log.e(TAG, "Error processing activities", e)
                    handleError(e)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "Database error", error.toException())
                handleError(error.toException())
            }
        }

        database.child("users").child(userId).child("activities")
            .addValueEventListener(valueEventListener!!)
    }

    private fun handleError(error: Exception) {
        if (!isFinishing) {
            Toast.makeText(
                this,
                "Error loading activities: ${error.message}",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun updateDashboard(activities: List<ActivityLog>, totalEmissions: Double) {
        binding.totalEmissionsText.text = String.format("%.2f kg CO₂", totalEmissions)
        binding.monthlyGoalText.text = String.format(
            "Monthly Goal: %.1f%%",
            calculateMonthlyGoal(totalEmissions)
        )
        adapter.updateActivities(activities)
    }

    private fun calculateMonthlyGoal(totalEmissions: Double): Double {
        val monthlyTarget = 100.0
        return (totalEmissions / monthlyTarget) * 100
    }

    private fun showDeleteDialog(activity: ActivityLog) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.dialog_delete_title))
            .setMessage(getString(R.string.dialog_delete_message))
            .setPositiveButton(getString(R.string.dialog_yes)) { _, _ ->
                deleteActivity(activity)
            }
            .setNegativeButton(getString(R.string.dialog_no), null)
            .show()
    }

    private fun deleteActivity(activity: ActivityLog) {
        database.child("users")
            .child(userId)
            .child("activities")
            .child(activity.id)
            .removeValue()
            .addOnSuccessListener {
                Toast.makeText(this, getString(R.string.success_delete), Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    this,
                    getString(R.string.error_delete, e.message),
                    Toast.LENGTH_LONG
                ).show()
            }
    }

    private fun checkAuthState() {
        if (auth.currentUser == null) {
            navigateToSignIn()
        } else {
            loadActivities()
        }
    }

    private fun showSignOutDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.dialog_sign_out_title))
            .setMessage(getString(R.string.dialog_sign_out_message))
            .setPositiveButton(getString(R.string.dialog_yes)) { _, _ ->
                signOut()
            }
            .setNegativeButton(getString(R.string.dialog_no), null)
            .show()
    }

    private fun signOut() {
        cleanupListener()
        auth.signOut()
        navigateToSignIn()
    }

    private fun cleanupListener() {
        valueEventListener?.let { listener ->
            database.child("users").child(userId).child("activities")
                .removeEventListener(listener)
            valueEventListener = null
        }
    }

    private fun navigateToSignIn() {
        val intent = Intent(this, SignInActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
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

    override fun onResume() {
        super.onResume()
        updateWelcomeText()
        if (auth.currentUser != null && valueEventListener == null) {
            loadActivities()
        }
    }

    private fun updateWelcomeText() {
        val userName = auth.currentUser?.displayName ?: "User"
        binding.welcomeText.text = getString(R.string.welcome_user, userName)
    }

    override fun onDestroy() {
        super.onDestroy()
        cleanupListener()
    }
}