package com.example.axis.api.models

import com.google.gson.annotations.SerializedName

data class FavoritesRequest(
    @SerializedName("user_id") val userId: Int,
    @SerializedName("favorites") val favorites: List<String>
)
