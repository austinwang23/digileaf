package com.example.digileaf.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.digileaf.R

class CalendarMonthViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {
    val month: TextView = itemView.findViewById(R.id.monthYearTV)
    val dayRecyclerView: RecyclerView = itemView.findViewById(R.id.calendar_day_RV)
}