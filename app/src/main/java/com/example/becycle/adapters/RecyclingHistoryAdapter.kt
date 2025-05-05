package com.example.becycle.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.becycle.R
import com.example.becycle.items.RecyclingItem

class RecyclingHistoryAdapter(private val items: List<RecyclingItem>) :
    RecyclerView.Adapter<RecyclingHistoryAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val img: ImageView = view.findViewById(R.id.imageView)
        val title: TextView = view.findViewById(R.id.titleView)
        val date: TextView = view.findViewById(R.id.dateView)
        val viewBtn: Button = view.findViewById(R.id.btnViewDetails)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recycling_history, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.img.setImageResource(item.imageRes)
        holder.title.text = item.title
        holder.date.text = item.date
        holder.viewBtn.setOnClickListener {
            Toast.makeText(holder.itemView.context, "Viewing details for ${item.title}", Toast.LENGTH_SHORT).show()
            // Intent to detail page can go here
        }
    }
}
