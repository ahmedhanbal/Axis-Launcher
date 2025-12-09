package com.example.axis.adapters

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.axis.R
import com.example.axis.models.WallpaperItem

class WallpapersAdapter(
    private val wallpapers: List<WallpaperItem>,
    private val onClick: (WallpaperItem) -> Unit
) : RecyclerView.Adapter<WallpapersAdapter.WallpaperViewHolder>() {
    
    inner class WallpaperViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val card: CardView = view.findViewById(R.id.wallpaperCard)
        val name: TextView = view.findViewById(R.id.tvWallpaperName)
        
        init {
            view.setOnClickListener {
                onClick(wallpapers[adapterPosition])
            }
        }
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WallpaperViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_wallpaper, parent, false)
        return WallpaperViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: WallpaperViewHolder, position: Int) {
        val wallpaper = wallpapers[position]
        
        holder.name.text = wallpaper.name
        
        // Create gradient background
        val gradient = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            intArrayOf(
                Color.parseColor(wallpaper.colorStart),
                Color.parseColor(wallpaper.colorEnd)
            )
        )
        gradient.cornerRadius = 16 * holder.itemView.context.resources.displayMetrics.density
        holder.card.background = gradient
    }
    
    override fun getItemCount() = wallpapers.size
}
