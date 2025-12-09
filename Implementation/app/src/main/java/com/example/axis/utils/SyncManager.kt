package com.example.axis.utils

import android.content.Context
import android.util.Log
import com.example.axis.api.ApiClient
import com.example.axis.api.models.FavoritesResponse
import com.example.axis.api.models.SettingsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SyncManager(private val context: Context) {
    private val preferenceManager = PreferenceManager(context)

    fun syncSettings(callback: ((Boolean) -> Unit)? = null) {
        val userId = preferenceManager.getUserId()
        if (userId == -1) {
            callback?.invoke(false)
            return 
        }

        val call = ApiClient.apiService.updateSettings(
            userId,
            preferenceManager.gridRows,
            preferenceManager.gridColumns,
            preferenceManager.iconSize,
            if (preferenceManager.showLabels) 1 else 0,
            if (preferenceManager.drawerMode) "DRAWER" else "CLASSIC"
        )

        call.enqueue(object : Callback<SettingsResponse> {
            override fun onResponse(call: Call<SettingsResponse>, response: Response<SettingsResponse>) {
                if (response.isSuccessful) {
                    Log.d("SyncManager", "Settings synced successfully")
                    callback?.invoke(true)
                } else {
                    Log.e("SyncManager", "Failed to sync settings: ${response.message()}")
                    callback?.invoke(false)
                }
            }

            override fun onFailure(call: Call<SettingsResponse>, t: Throwable) {
                Log.e("SyncManager", "Error syncing settings", t)
                callback?.invoke(false)
            }
        })
    }

    fun syncFavorites(callback: ((Boolean) -> Unit)? = null) {
        val userId = preferenceManager.getUserId()
        if (userId == -1) {
            callback?.invoke(false)
            return 
        }

        val favorites = preferenceManager.getFavoriteApps().toList()
        val body = com.example.axis.api.models.FavoritesRequest(userId, favorites)

        val call = ApiClient.apiService.updateFavorites(body)

        call.enqueue(object : Callback<FavoritesResponse> {
            override fun onResponse(call: Call<FavoritesResponse>, response: Response<FavoritesResponse>) {
                if (response.isSuccessful) {
                    Log.d("SyncManager", "Favorites synced successfully")
                    callback?.invoke(true)
                } else {
                    Log.e("SyncManager", "Failed to sync favorites: ${response.message()}")
                    callback?.invoke(false)
                }
            }

            override fun onFailure(call: Call<FavoritesResponse>, t: Throwable) {
                Log.e("SyncManager", "Error syncing favorites", t)
                callback?.invoke(false)
            }
        })
    }
    
    fun fetchSettings() {
        val userId = preferenceManager.getUserId()
        if (userId == -1) return
        
        val call = ApiClient.apiService.getSettings(userId)
        call.enqueue(object : Callback<SettingsResponse> {
            override fun onResponse(call: Call<SettingsResponse>, response: Response<SettingsResponse>) {
                if (response.isSuccessful) {
                    val settings = response.body()?.settings
                    if (settings != null) {
                        preferenceManager.gridRows = settings.gridRows
                        preferenceManager.gridColumns = settings.gridColumns
                        preferenceManager.iconSize = settings.iconSize
                        preferenceManager.showLabels = settings.showLabels == 1
                        preferenceManager.drawerMode = settings.drawerMode == "DRAWER"
                        Log.d("SyncManager", "Settings fetched and updated")
                    }
                }
            }

            override fun onFailure(call: Call<SettingsResponse>, t: Throwable) {
                Log.e("SyncManager", "Error fetching settings", t)
            }
        })
    }
    
    fun fetchFavorites() {
        val userId = preferenceManager.getUserId()
        if (userId == -1) return
        
        val call = ApiClient.apiService.getFavorites(userId)
        call.enqueue(object : Callback<FavoritesResponse> {
            override fun onResponse(call: Call<FavoritesResponse>, response: Response<FavoritesResponse>) {
                if (response.isSuccessful) {
                    val favorites = response.body()?.favorites
                    if (favorites != null) {
                        // Clear current favorites and add new ones
                        // PreferenceManager doesn't have clearFavorites, so we iterate
                        // Or we can just overwrite the set.
                        // PreferenceManager has add/remove. I should add a setFavorites method or just use the key directly if I could.
                        // But I can't access prefs directly here.
                        // I'll add setFavoriteApps to PreferenceManager.
                        
                        // For now, let's assume I added setFavoriteApps
                        preferenceManager.setFavoriteApps(favorites.toSet())
                        Log.d("SyncManager", "Favorites fetched and updated")
                    }
                }
            }

            override fun onFailure(call: Call<FavoritesResponse>, t: Throwable) {
                Log.e("SyncManager", "Error fetching favorites", t)
            }
        })
    }
}
