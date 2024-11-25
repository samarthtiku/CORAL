package com.example.coral

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CarbonActivityAdapter(private val activities: List<CarbonActivity>) :
    RecyclerView.Adapter<CarbonActivityAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val activityName: TextView = view.findViewById(R.id.activity_name)
        val activityAmount: TextView = view.findViewById(R.id.activity_amount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_carbon_activity, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val activity = activities[position]
        holder.activityName.text = activity.name
        holder.activityAmount.text = "${String.format("%.2f", activity.amount)} kg CO2"
    }

    override fun getItemCount() = activities.size
}
