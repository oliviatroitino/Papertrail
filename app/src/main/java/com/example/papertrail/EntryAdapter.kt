package com.example.papertrail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class EntryAdapter (var entries: List<JournalEntry>,
                    var context: Context,
                    val onEntryClick: (JournalEntry) -> Unit):
    RecyclerView.Adapter<EntryAdapter.EntryViewHolder>() {

    inner class EntryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleText: TextView = itemView.findViewById(R.id.cardEntryTitle)
        val dateText: TextView = itemView.findViewById(R.id.cardEntryDate)
        val previewText: TextView = itemView.findViewById(R.id.cardEntryText)
        val card: CardView = itemView.findViewById(R.id.entryCard)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_entry, parent, false)
        return EntryViewHolder(view)
    }

    override fun onBindViewHolder(holder: EntryViewHolder, position: Int) {
        val entry = entries[position]
        holder.titleText.text = entry.title
        holder.dateText.text = entry.dateTime
        holder.previewText.text = entry.content.take(100) + "..."
        holder.card.setOnClickListener {
            onEntryClick(entry)
        }
    }

    override fun getItemCount(): Int = entries.size

}