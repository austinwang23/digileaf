package com.example.digileaf.adapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.digileaf.R
import com.example.digileaf.entities.Journal
import com.example.digileaf.adapter.JournalAdapter.JournalViewHolder

class JournalAdapter : ListAdapter<Journal, JournalViewHolder>(JOURNAL_COMPARATOR) {

    var onItemClick: ((Journal) -> Unit)? = null

    inner class JournalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val journalImageContainer : CardView = itemView.findViewById(R.id.journal_image_container)
        val journalImage: ImageView = itemView.findViewById(R.id.journal_image)
        val journalEntry: TextView = itemView.findViewById(R.id.journal_entry)
        val journalDate: TextView = itemView.findViewById(R.id.journal_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JournalViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.journal_entry_card, parent, false)

        return JournalViewHolder(view)
    }

    override fun onBindViewHolder(holder: JournalViewHolder, position: Int) {
        val journal = getItem(position)
        holder.journalEntry.text = journal.entry

        holder.journalDate.text = journal.date

        if (journal.imagePath != "") {
            val imageFile = holder.itemView.context.getFileStreamPath(journal.imagePath)
            // If for whatever reason, the file no longer exists
            if (imageFile == null || !imageFile.exists()) {
                holder.journalImage.setImageResource(R.drawable.default_plant)
            } else {
                val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
                holder.journalImage.setImageBitmap(bitmap)
            }
        } else {
            holder.journalImageContainer.visibility = View.GONE
            holder.journalImage.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
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
                return ((oldItem.date == newItem.date) &&
                        (oldItem.entry == newItem.entry) &&
                        (oldItem.plantId == newItem.plantId) &&
                        (oldItem.imagePath == newItem.imagePath))
            }
        }
    }

}