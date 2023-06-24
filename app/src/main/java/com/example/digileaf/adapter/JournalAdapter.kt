package com.example.digileaf.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.digileaf.R
import com.example.digileaf.entities.Journal
import com.example.digileaf.adapter.JournalAdapter.JournalViewHolder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class JournalAdapter: ListAdapter<Journal, JournalViewHolder>(JOURNAL_COMPARATOR) {

    var onItemClick : ((Journal) -> Unit)? = null

    inner class JournalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val journalTimestamp: TextView = itemView.findViewById(R.id.journal_date)
        val journalEntry: TextView = itemView.findViewById(R.id.journal_entry)
        val journalDate: TextView = itemView.findViewById(R.id.journal_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JournalViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.journal_entry_card, parent, false)

        return JournalViewHolder(view)
    }

    override fun onBindViewHolder(holder: JournalViewHolder, position: Int) {
        val journal = getItem(position)

        holder.journalEntry.text = journal.entry

        val timestamp = journal.timestamp
        val dateFormat = SimpleDateFormat("MMMM d yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(Date(timestamp))

        holder.journalDate.text = formattedDate


        holder.itemView.setOnClickListener{
            onItemClick?.invoke(journal)
        }
    }

    companion object {
        private val JOURNAL_COMPARATOR = object : DiffUtil.ItemCallback<Journal>() {
            override fun areItemsTheSame(oldItem: Journal, newItem: Journal): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Journal, newItem: Journal): Boolean {
                // TODO - Find a better way to do this
                return ((oldItem.timestamp == newItem.timestamp) &&
                        (oldItem.entry == newItem.entry) &&
                        (oldItem.plantId == newItem.plantId) &&
                        (oldItem.imagePath == newItem.imagePath))
            }
        }
    }

}