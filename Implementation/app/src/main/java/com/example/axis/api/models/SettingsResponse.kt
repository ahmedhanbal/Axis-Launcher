package com.example.axis.api.models

import com.google.gson.annotations.SerializedName

data class SettingsResponse(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String?,
    @SerializedName("settings") val settings: UserSettings?
)

data class UserSettings(
    @SerializedName("grid_rows") val gridRows: Int,
    @SerializedName("grid_columns") val gridColumns: Int,
    @SerializedName("icon_size") val iconSize: Int,
    @SerializedName("show_labels") val showLabels: Int, // 0 or 1
    @SerializedName("drawer_mode") val drawerMode: String
)
