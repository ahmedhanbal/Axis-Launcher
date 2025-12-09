package com.example.axis.api

import com.example.axis.api.models.AuthResponse
import com.example.axis.api.models.FavoritesResponse
import com.example.axis.api.models.SettingsResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Body

interface ApiService {

    @FormUrlEncoded
    @POST("signup.php")
    fun signup(
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("profile_image") profileImage: String? // Base64 string
    ): Call<AuthResponse>

    @FormUrlEncoded
    @POST("login.php")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<AuthResponse>

    @FormUrlEncoded
    @POST("sync_settings.php")
    fun updateSettings(
        @Field("user_id") userId: Int,
        @Field("grid_rows") gridRows: Int,
        @Field("grid_columns") gridColumns: Int,
        @Field("icon_size") iconSize: Int,
        @Field("show_labels") showLabels: Int,
        @Field("drawer_mode") drawerMode: String
    ): Call<SettingsResponse>

    @GET("sync_settings.php")
    fun getSettings(
        @Query("user_id") userId: Int
    ): Call<SettingsResponse>

    @POST("sync_favorites.php")
    fun updateFavorites(
        @Body body: com.example.axis.api.models.FavoritesRequest
    ): Call<FavoritesResponse>

    @GET("sync_favorites.php")
    fun getFavorites(
        @Query("user_id") userId: Int
    ): Call<FavoritesResponse>
}
