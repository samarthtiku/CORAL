package com.coral.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.coral.R
import com.coral.models.ActivityLog
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ActivityAdapter(
    private var activities: List<ActivityLog>,
    private val onDeleteClick: (ActivityLog) -> Unit
) : RecyclerView.Adapter<ActivityAdapter.ViewHolder>() {

    private val dateFormat = SimpleDateFormat("MMM dd, yyyy 'at' hh:mm a", Locale.getDefault())

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val typeText: TextView = view.findViewById(R.id.activityTypeText)
        val emissionText: TextView = view.findViewById(R.id.emissionText)
        val dateText: TextView = view.findViewById(R.id.dateText)
        val deleteButton: ImageButton = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_activity, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val activity = activities[position]

        // Set activity type
        holder.typeText.text = activity.type

        // Format emission value
        val emissionText = when {
            activity.emission < 0 -> String.format("%.2f kg CO₂ saved", -activity.emission)
            activity.emission == 0.0 -> "No emissions"
            else -> String.format("%.2f kg CO₂", activity.emission)
        }
        holder.emissionText.text = emissionText

        // Format timestamp with date and time
        try {
            val date = Date(activity.timestamp)
            holder.dateText.text = dateFormat.format(date)
        } catch (e: Exception) {
            holder.dateText.text = "Date not available"
        }

        // Setup delete button
        holder.deleteButton.apply {
            contentDescription = "Delete ${activity.type} activity"
            setOnClickListener { onDeleteClick(activity) }
        }
    }

    override fun getItemCount() = activities.size

    fun updateActivities(newActivities: List<ActivityLog>) {
        activities = newActivities.sortedByDescending { it.timestamp }
        notifyDataSetChanged()
    }

    companion object {
        private const val TAG = "ActivityAdapter"
    }
}