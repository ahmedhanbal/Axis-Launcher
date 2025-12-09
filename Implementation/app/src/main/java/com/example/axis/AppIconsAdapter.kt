package com.example.axis

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AppIconsAdapter(private val appIcons: List<AppIcon>) :
    RecyclerView.Adapter<AppIconsAdapter.AppIconViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppIconViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_app_icon, parent, false)
        return AppIconViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppIconViewHolder, position: Int) {
        val appIcon = appIcons[position]
        if (appIcon.iconDrawable != null) {
            holder.iconImage.setImageDrawable(appIcon.iconDrawable)
        } else {
            holder.iconImage.setImageResource(appIcon.iconResId)
        }
        holder.iconLabel.text = appIcon.label
    }

    override fun getItemCount(): Int = appIcons.size

    // ViewHolder to hold references for app icon and label
    class AppIconViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val iconImage: ImageView = view.findViewById(R.id.appIconImage)
        val iconLabel: TextView = view.findViewById(R.id.appIconLabel)
    }
}
