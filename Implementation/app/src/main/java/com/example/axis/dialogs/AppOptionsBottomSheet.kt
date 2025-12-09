package com.example.axis.dialogs

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.axis.R
import com.example.axis.models.AppInfo
import com.example.axis.utils.PreferenceManager
import com.example.axis.utils.SyncManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AppOptionsBottomSheet(
    private val appInfo: AppInfo,
    private val onFavoriteToggle: (() -> Unit)? = null
) : BottomSheetDialogFragment() {

    private lateinit var preferenceManager: PreferenceManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_app_options, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        preferenceManager = PreferenceManager(requireContext())
        
        val appNameText: TextView = view.findViewById(R.id.appName)
        val btnFavorite: TextView = view.findViewById(R.id.btnFavorite)
        val btnAppInfo: TextView = view.findViewById(R.id.btnAppInfo)
        val btnUninstall: TextView = view.findViewById(R.id.btnUninstall)
        
        appNameText.text = appInfo.appName
        
        // Set favorite button text
        val isFavorite = preferenceManager.isFavorite(appInfo.packageName)
        btnFavorite.text = if (isFavorite) "Remove from Favorites" else "Add to Favorites"
        
        btnFavorite.setOnClickListener {
            if (isFavorite) {
                preferenceManager.removeFavoriteApp(appInfo.packageName)
            } else {
                preferenceManager.addFavoriteApp(appInfo.packageName)
            }
            SyncManager(requireContext()).syncFavorites()
            onFavoriteToggle?.invoke()
            dismiss()
        }
        
        btnAppInfo.setOnClickListener {
            openAppInfo()
            dismiss()
        }
        
        btnUninstall.setOnClickListener {
            uninstallApp()
            dismiss()
        }
    }

    private fun openAppInfo() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse("package:${appInfo.packageName}")
        startActivity(intent)
    }

    private fun uninstallApp() {
        val intent = Intent(Intent.ACTION_DELETE)
        intent.data = Uri.parse("package:${appInfo.packageName}")
        startActivity(intent)
    }

    companion object {
        const val TAG = "AppOptionsBottomSheet"
    }
}
