package com.example.axis.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.axis.R
import com.example.axis.models.ThemeItem

class ThemesAdapter(
    private val themes: List<ThemeItem>,
    private val onClick: (ThemeItem) -> Unit
) : RecyclerView.Adapter<ThemesAdapter.ThemeViewHolder>() {
    
    inner class ThemeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val card: CardView = view.findViewById(R.id.themeCard)
        val colorsContainer: LinearLayout = view.findViewById(R.id.colorsContainer)
        val name: TextView = view.findViewById(R.id.tvThemeName)
        val description: TextView = view.findViewById(R.id.tvThemeDescription)
        val badge: TextView = view.findViewById(R.id.tvPremiumBadge)
        val checkmark: ImageView = view.findViewById(R.id.ivCheckmark)
        
        init {
            view.setOnClickListener {
                onClick(themes[adapterPosition])
            }
        }
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThemeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_theme, parent, false)
        return ThemeViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: ThemeViewHolder, position: Int) {
        val theme = themes[position]
        
        holder.name.text = theme.name
        holder.description.text = theme.description
        holder.badge.visibility = if (theme.isPremium) View.VISIBLE else View.GONE
        holder.checkmark.visibility = if (theme.isActive) View.VISIBLE else View.GONE
        
        // Add color circles
        holder.colorsContainer.removeAllViews()
        theme.colors.take(6).forEach { colorHex ->
            val colorView = View(holder.itemView.context)
            val size = (40 * holder.itemView.context.resources.displayMetrics.density).toInt()
            val margin = (8 * holder.itemView.context.resources.displayMetrics.density).toInt()
            
            val params = LinearLayout.LayoutParams(size, size).apply {
                setMargins(margin, 0, margin, 0)
            }
            colorView.layoutParams = params
            
            val drawable = holder.itemView.context.getDrawable(R.drawable.circle_shape)
            DrawableCompat.setTint(drawable!!, Color.parseColor(colorHex))
            colorView.background = drawable
            
            holder.colorsContainer.addView(colorView)
        }
    }
    
    override fun getItemCount() = themes.size
}
