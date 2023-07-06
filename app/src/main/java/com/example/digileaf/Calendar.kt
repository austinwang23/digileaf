package com.example.digileaf

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.digileaf.adapter.CalendarAdapter
import com.example.digileaf.adapter.CalendarMonthAdapter
import com.example.digileaf.database.JournalViewModel
import com.example.digileaf.database.JournalViewModelFactory
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

class Calendar : Fragment() {

//    private lateinit var monthYearText: TextView
    private lateinit var calendarRecyclerView: RecyclerView
    private lateinit var selectedDate: LocalDate

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)
        initWidgets(view)
        selectedDate = LocalDate.now()

        val monthDataList = getMonthDataList(selectedDate)

        calendarRecyclerView = view.findViewById(R.id.calendar_RV)

        calendarRecyclerView.adapter = CalendarMonthAdapter(monthDataList, parentFragmentManager)
        calendarRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

//        calendarRecyclerView.layoutManager = LinearLayoutManager(context)

        return view
    }

    private fun initWidgets(view: View) {
        calendarRecyclerView = view.findViewById(R.id.calendar_RV)
    }


    private fun getMonthDataList(startDate: LocalDate): ArrayList<String> {
        val monthDataList = ArrayList<String>()
        val currentDate = LocalDate.now()
        var currentDateIterator = startDate

        while (currentDateIterator.isBefore(currentDate.plusMonths(6))) {
            val monthYearText = monthYearFromDate(currentDateIterator)
            monthDataList.add(monthYearText)
            currentDateIterator = currentDateIterator.plusMonths(1)
        }

        return monthDataList
    }

    private fun monthYearFromDate(date: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern("MMMM yyyy")
        return date.format(formatter)
    }

}