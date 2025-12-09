package com.example.axis.adapters

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.axis.R
import com.example.axis.models.WidgetInfo

class WidgetsAdapter(
    private val widgets: List<WidgetInfo>,
    private val onClick: (WidgetInfo) -> Unit
) : RecyclerView.Adapter<WidgetsAdapter.WidgetViewHolder>() {
    
    inner class WidgetViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val card: CardView = view.findViewById(R.id.widgetCard)
        val title: TextView = view.findViewById(R.id.tvWidgetTitle)
        val content: TextView = view.findViewById(R.id.tvWidgetContent)
        val icon: ImageView = view.findViewById(R.id.ivWidgetIcon)
        
        init {
            view.setOnClickListener {
                onClick(widgets[adapterPosition])
            }
        }
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WidgetViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_widget, parent, false)
        return WidgetViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: WidgetViewHolder, position: Int) {
        val widget = widgets[position]
        holder.title.text = widget.name
        holder.content.text = widget.content
        holder.icon.setImageResource(widget.icon)
        
        // Set background based on widget type
        when (widget.backgroundColor) {
            "weather_blue" -> holder.card.setCardBackgroundColor(Color.parseColor("#4A90E2"))
            "music_purple" -> holder.card.setCardBackgroundColor(Color.parseColor("#9B59B6"))
            else -> holder.card.setCardBackgroundColor(Color.WHITE)
        }
    }
    
    override fun getItemCount() = widgets.size
}
