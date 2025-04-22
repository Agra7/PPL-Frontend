package com.example.becycle

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class IdeaAdapter(private val ideas: List<Idea>) :
    RecyclerView.Adapter<IdeaAdapter.IdeaViewHolder>() {

    class IdeaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ideaText: TextView = view.findViewById(R.id.idea_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IdeaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_idea, parent, false)
        return IdeaViewHolder(view)
    }

    override fun onBindViewHolder(holder: IdeaViewHolder, position: Int) {
        val idea = ideas[position]
        holder.ideaText.text = idea.title

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, IdeaDetailActivity::class.java)
            intent.putExtra("idea_title", idea.title)
            intent.putExtra("idea_description", idea.description)
            intent.putExtra("idea_materials", idea.materials)
            intent.putExtra("idea_steps", idea.steps)
            intent.putExtra("idea_references", idea.references)
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = ideas.size
}


