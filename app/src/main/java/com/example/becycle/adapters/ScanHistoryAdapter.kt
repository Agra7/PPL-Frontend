package com.example.becycle.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.becycle.R
import com.example.becycle.items.ScanHistoryItem

class ScanHistoryAdapter(private val items: List<ScanHistoryItem>) :
    RecyclerView.Adapter<ScanHistoryAdapter.ScanViewHolder>() {

    inner class ScanViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgThumb: ImageView = view.findViewById(R.id.imageThumb)
        val txtLabel: TextView = view.findViewById(R.id.textLabel)
        val txtTime: TextView = view.findViewById(R.id.textTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScanViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_scan_history, parent, false)
        return ScanViewHolder(view)
    }

    override fun onBindViewHolder(holder: ScanViewHolder, position: Int) {
        val item = items[position]
        Glide.with(holder.itemView.context).load(item.imageUrl).into(holder.imgThumb)
        holder.txtLabel.text = item.label
        holder.txtTime.text = item.timestamp
    }

    override fun getItemCount() = items.size
}
