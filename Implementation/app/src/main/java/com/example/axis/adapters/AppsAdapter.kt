package com.example.axis.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.axis.R
import com.example.axis.models.AppInfo

class AppsAdapter(
    private val apps: List<AppInfo>,
    private val iconSize: Int = 70,
    private val showLabels: Boolean = true,
    private val iconPack: String = "classic",
    private val onAppClick: (AppInfo) -> Unit,
    private val onAppLongClick: (AppInfo) -> Boolean = { false }
) : RecyclerView.Adapter<AppsAdapter.AppViewHolder>() {

    inner class AppViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val appIcon: ImageView = view.findViewById(R.id.appIconImage)
        val appLabel: TextView = view.findViewById(R.id.appIconLabel)

        fun bind(appInfo: AppInfo) {
            // Apply Icon Size
            val density = itemView.context.resources.displayMetrics.density
            val sizePx = (iconSize * density).toInt()
            appIcon.layoutParams.width = sizePx
            appIcon.layoutParams.height = sizePx
            appIcon.requestLayout()

            // Apply Icon Pack (Simulation)
            if (iconPack == "flat") {
                // Simulate flat by just setting the icon for now
                appIcon.setImageDrawable(appInfo.icon)
            } else {
                appIcon.setImageDrawable(appInfo.icon)
            }

            // Apply Labels
            if (showLabels) {
                appLabel.text = appInfo.appName
                appLabel.visibility = View.VISIBLE
            } else {
                appLabel.visibility = View.GONE
            }

            itemView.setOnClickListener {
                onAppClick(appInfo)
            }

            itemView.setOnLongClickListener {
                onAppLongClick(appInfo)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_app_icon, parent, false)
        return AppViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        holder.bind(apps[position])
    }

    override fun getItemCount(): Int = apps.size
}
