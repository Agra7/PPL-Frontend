package com.example.becycle.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.becycle.R
import com.example.becycle.adapters.ArticleAdapter.LongArticleViewHolder
import com.example.becycle.adapters.ArticleAdapter.ShortArticleViewHolder
import com.example.becycle.data.local.entity.ArticleEntity
import com.example.becycle.data.local.entity.HistoryEntity

class HistoryAdapter(
    private var historyList: List<HistoryEntity>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder  {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val history = historyList[position]
        if (holder is HistoryViewHolder) {
            holder.bind(history)
        }
    }

    override fun getItemCount(): Int = historyList.size

    fun updateHistory(newHistoryList: List<HistoryEntity>) {
        this.historyList = newHistoryList
        notifyDataSetChanged()
    }

    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val itemDate: TextView = itemView.findViewById(R.id.history_item_date)
        private val itemTitle: TextView = itemView.findViewById(R.id.history_item_title)
//        private val itemImage: TextView = itemView.findViewById(R.id.history_item_image)
        fun bind(history: HistoryEntity) {
            itemDate.text = history.createdAt?: "-"
            itemTitle.text = history.predictionResult?: "-"
//            itemImage.text = history.imageUrl?: "-"
        }
    }
}