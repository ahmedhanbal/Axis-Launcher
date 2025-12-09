package com.example.axis.api.models

import com.google.gson.annotations.SerializedName

data class FavoritesResponse(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String?,
    @SerializedName("favorites") val favorites: List<String>?
)
