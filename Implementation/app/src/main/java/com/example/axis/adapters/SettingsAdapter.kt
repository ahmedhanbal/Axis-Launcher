package com.example.axis.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.axis.R

class SettingsAdapter(
    private val items: List<SettingsListItem>,
    private val onSettingClick: (SettingItem) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_SETTING = 1
    }

    sealed class SettingsListItem {
        data class Header(val title: String) : SettingsListItem()
        data class Setting(val item: SettingItem) : SettingsListItem()
    }

    data class SettingItem(
        val title: String,
        val description: String?,
        val type: SettingType
    )

    enum class SettingType {
        GRID_SIZE,
        ICON_SIZE,
        SHOW_LABELS,
        DRAWER_MODE,
        APPEARANCE,

        PROFILE,
        BACKUP_RESTORE,
        CLOUD_SYNC,
        NOTIFICATIONS,
        PRIVACY_SECURITY,
        ABOUT,

        WALLPAPERS
    }

    inner class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleText: TextView = view.findViewById(R.id.headerTitle)

        fun bind(header: SettingsListItem.Header) {
            titleText.text = header.title
        }
    }

    inner class SettingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleText: TextView = view.findViewById(R.id.settingTitle)
        val descText: TextView = view.findViewById(R.id.settingDescription)

        fun bind(setting: SettingsListItem.Setting) {
            val item = setting.item
            titleText.text = item.title
            if (item.description != null) {
                descText.text = item.description
                descText.visibility = View.VISIBLE
            } else {
                descText.visibility = View.GONE
            }

            itemView.setOnClickListener {
                onSettingClick(item)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is SettingsListItem.Header -> TYPE_HEADER
            is SettingsListItem.Setting -> TYPE_SETTING
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_settings_header, parent, false)
                HeaderViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_setting, parent, false)
                SettingViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is SettingsListItem.Header -> (holder as HeaderViewHolder).bind(item)
            is SettingsListItem.Setting -> (holder as SettingViewHolder).bind(item)
        }
    }

    override fun getItemCount(): Int = items.size
}
