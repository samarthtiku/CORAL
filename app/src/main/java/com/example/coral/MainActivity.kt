package com.example.coral

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var activitiesList: RecyclerView
    private lateinit var inputActivity: EditText
    private lateinit var inputAmount: EditText
    private lateinit var addButton: Button
    private lateinit var totalFootprint: TextView
    private val activities = mutableListOf<CarbonActivity>()
    private lateinit var adapter: CarbonActivityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        activitiesList = findViewById(R.id.activities_list)
        inputActivity = findViewById(R.id.input_activity)
        inputAmount = findViewById(R.id.input_amount)
        addButton = findViewById(R.id.add_button)
        totalFootprint = findViewById(R.id.total_footprint)

        // Setup RecyclerView
        adapter = CarbonActivityAdapter(activities)
        activitiesList.layoutManager = LinearLayoutManager(this)
        activitiesList.adapter = adapter

        // Setup add button click listener
        addButton.setOnClickListener {
            val activity = inputActivity.text.toString()
            val amount = inputAmount.text.toString().toDoubleOrNull() ?: 0.0

            if (activity.isNotEmpty()) {
                val carbonActivity = CarbonActivity(activity, amount)
                activities.add(carbonActivity)
                adapter.notifyItemInserted(activities.size - 1)
                updateTotalFootprint()

                // Clear inputs
                inputActivity.text.clear()
                inputAmount.text.clear()
            }
        }
    }

    private fun updateTotalFootprint() {
        val total = activities.sumOf { it.amount }
        totalFootprint.text = "Total Carbon Footprint: ${String.format("%.2f", total)} kg CO2"
    }
}