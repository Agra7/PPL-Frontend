package com.example.becycle.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.becycle.R
import com.example.becycle.data.local.entity.ArticleEntity

class ArticleAdapter(
    private var articleList: List<ArticleEntity>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_SHORT = 0
    private val VIEW_TYPE_LONG = 1

    // Define what makes a "long" article. Here, let's say description or content length > 100 is long.
    private fun isLongArticle(article: ArticleEntity): Boolean {
        // You can adjust this logic as needed.
        return (article.description?.length ?: 0) > 100 || (article.content?.length ?: 0) > 100
    }

    override fun getItemViewType(position: Int): Int {
        return if (isLongArticle(articleList[position])) VIEW_TYPE_LONG else VIEW_TYPE_SHORT
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

    /**
     * Update the list of articles and notify the adapter.
     */
    fun updateArticles(newArticleList: List<ArticleEntity>) {
        this.articleList = newArticleList
        notifyDataSetChanged()
    }

    class ShortArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val website: TextView = itemView.findViewById(R.id.tvSource)
        private val title: TextView = itemView.findViewById(R.id.tvTitle)
        private val dateTime: TextView = itemView.findViewById(R.id.tvDate)

        fun bind(article: ArticleEntity) {
            website.text = article.source?.name ?: "-"
            title.text = article.title ?: "-"
            dateTime.text = article.publishedAt ?: "-"
        }
    }

    class LongArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val website: TextView = itemView.findViewById(R.id.tvSource)
        private val title: TextView = itemView.findViewById(R.id.tvTitle)
        private val dateTime: TextView = itemView.findViewById(R.id.tvDate)
        private val content: TextView = itemView.findViewById(R.id.tvContent)

        fun bind(article: ArticleEntity) {
            website.text = article.source?.name ?: "-"
            title.text = article.title ?: "-"
            dateTime.text = article.publishedAt ?: "-"
            content.text = article.description ?: "-"
        }
    }
}