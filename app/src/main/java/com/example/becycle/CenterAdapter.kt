package com.example.becycle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CenterAdapter(private val centers: List<String>) :
    RecyclerView.Adapter<CenterAdapter.CenterViewHolder>() {

    class CenterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val centerText: TextView = view.findViewById(R.id.idea_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CenterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_idea, parent, false)
        return CenterViewHolder(view)
    }

    override fun onBindViewHolder(holder: CenterViewHolder, position: Int) {
        holder.centerText.text = centers[position]
    }

    override fun getItemCount() = centers.size
}
