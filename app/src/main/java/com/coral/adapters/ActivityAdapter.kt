// app/src/main/java/com/coral/adapters/ActivityAdapter.kt
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

        holder.typeText.text = activity.type
        holder.emissionText.text = String.format("%.2f kg CO₂", activity.emission)
        holder.dateText.text = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            .format(Date(activity.timestamp))

        holder.deleteButton.setOnClickListener {
            onDeleteClick(activity)
        }
    }

    override fun getItemCount() = activities.size

    fun updateActivities(newActivities: List<ActivityLog>) {
        activities = newActivities
        notifyDataSetChanged()
    }
}