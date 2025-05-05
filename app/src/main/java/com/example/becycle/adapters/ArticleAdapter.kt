package com.example.becycle.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.becycle.items.Article
import com.example.becycle.R

class ArticleAdapter(private val articleList: List<Article>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_SHORT = 0
    private val VIEW_TYPE_LONG = 1

    override fun getItemViewType(position: Int): Int {
        return if (articleList[position].isLongArticle) VIEW_TYPE_LONG else VIEW_TYPE_SHORT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_TYPE_LONG) {
            val view = layoutInflater.inflate(R.layout.item_article_long, parent, false)
            LongArticleViewHolder(view)
        } else {
            val view = layoutInflater.inflate(R.layout.item_article_short, parent, false)
            ShortArticleViewHolder(view)
        }
    }

    override fun getItemCount(): Int = articleList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val article = articleList[position]
        if (holder is LongArticleViewHolder) {
            holder.bind(article)
        } else if (holder is ShortArticleViewHolder) {
            holder.bind(article)
        }
    }

    class ShortArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val website: TextView = itemView.findViewById(R.id.tvSource)
        private val title: TextView = itemView.findViewById(R.id.tvTitle)
        private val dateTime: TextView = itemView.findViewById(R.id.tvDate)

        fun bind(article: Article) {
            website.text = article.website
            title.text = article.title
            dateTime.text = article.dateTime
        }
    }

    class LongArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val website: TextView = itemView.findViewById(R.id.tvSource)
        private val title: TextView = itemView.findViewById(R.id.tvTitle)
        private val dateTime: TextView = itemView.findViewById(R.id.tvDate)
        private val content: TextView = itemView.findViewById(R.id.tvContent)

        fun bind(article: Article) {
            website.text = article.website
            title.text = article.title
            dateTime.text = article.dateTime
            content.text = article.description
        }
    }
}
