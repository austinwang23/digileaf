package com.example.digileaf.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.digileaf.R
import java.time.LocalDate

class CalendarAdapter(
    private val daysOfMonth: ArrayList<String>
) :
    RecyclerView.Adapter<CalendarViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.calendar_cell, parent, false)
        val layoutParams = view.layoutParams
        layoutParams.height = (parent.height * 0.166666666).toInt()
        return CalendarViewHolder(view)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {

        val day = daysOfMonth[position]
        Log.e("day adapter", "$position, $day")
        holder.dayOfMonth.text = "1"

        // Get the current day
//        val currentDate = LocalDate.now().dayOfMonth.toString()
//
//        if (isCurMonth && day == currentDate) {
//            holder.dayOfMonth.setTextColor(ContextCompat.getColor(holder.itemView.context,
//                R.color.white
//            ))
//            holder.dayOfMonth.setBackgroundResource(R.drawable.bg_circle)
//        } else {
//            holder.dayOfMonth.setTextColor(ContextCompat.getColor(holder.itemView.context,
//                R.color.black
//            ))
//        }
    }

    override fun getItemCount(): Int {
        return daysOfMonth.size
    }

    interface OnItemListener {
        fun onItemClick(position: Int, dayText: String)
    }
}