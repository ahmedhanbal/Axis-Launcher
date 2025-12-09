package com.example.axis.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.axis.R
import com.example.axis.models.AppCategory

class CategoriesAdapter(
    private val categories: List<AppCategory>,
    private val onClick: (AppCategory) -> Unit
) : RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>() {
    
    inner class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.findViewById(R.id.ivCategoryIcon)
        val name: TextView = view.findViewById(R.id.tvCategoryName)
        val count: TextView = view.findViewById(R.id.tvAppCount)
        val arrow: ImageView = view.findViewById(R.id.ivArrow)
        
        init {
            view.setOnClickListener {
                onClick(categories[adapterPosition])
            }
        }
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.icon.setImageResource(category.icon)
        holder.name.text = category.name
        holder.count.text = "${category.appCount} apps"
    }
    
    override fun getItemCount() = categories.size
}
