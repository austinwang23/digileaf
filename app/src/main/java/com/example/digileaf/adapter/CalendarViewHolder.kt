package com.example.digileaf.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.digileaf.R

class CalendarViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    val dayOfMonth: TextView = itemView.findViewById(R.id.cellDayText)
}