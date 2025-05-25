package com.example.becycle.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.becycle.R
import com.example.becycle.items.HistoryItem

class HistoryAdapter(private val items: List<HistoryItem>) :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    inner class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.history_item_image)
        val title: TextView = itemView.findViewById(R.id.history_item_title)
        val date: TextView = itemView.findViewById(R.id.history_item_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = items[position]
        holder.title.text = item.title
        holder.date.text = item.date
        Glide.with(holder.image.context)
            .load(item.imageUrl)
            .placeholder(R.drawable.plant_placeholder)
            .into(holder.image)
    }

    override fun getItemCount(): Int = items.size
}
