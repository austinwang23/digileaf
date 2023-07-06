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
import androidx.recyclerview.widget.RecyclerView
import com.example.digileaf.adapter.CalendarAdapter
import com.example.digileaf.database.JournalViewModel
import com.example.digileaf.database.JournalViewModelFactory
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

class Calendar : Fragment(), CalendarAdapter.OnItemListener {

    private lateinit var monthYearText: TextView
    private lateinit var calendarRecyclerView: RecyclerView
    private lateinit var selectedDate: LocalDate
    private val journalViewModel: JournalViewModel by viewModels {
        JournalViewModelFactory((activity?.application as DigileafApplication).journalRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)
        initWidgets(view)
        selectedDate = LocalDate.now()
        setMonthView()
        return view
    }

    private fun initWidgets(view: View) {
        calendarRecyclerView = view.findViewById(R.id.calendar_RV)
        monthYearText = view.findViewById(R.id.monthYearTV)
        view.findViewById<View>(R.id.previousMonthButton).setOnClickListener { previousMonthAction() }
        view.findViewById<View>(R.id.nextMonthButton).setOnClickListener { nextMonthAction() }
    }

    private fun setMonthView() {
        monthYearText.text = monthYearFromDate(selectedDate)
        val daysInMonth = daysInMonthArray(selectedDate)

        val isCurMonth = monthYearFromDate(selectedDate) == monthYearFromDate(LocalDate.now())

        val calendarAdapter = CalendarAdapter(daysInMonth, isCurMonth,this)

        val layoutManager = GridLayoutManager(context, 7)
        calendarRecyclerView.layoutManager = layoutManager
        calendarRecyclerView.adapter = calendarAdapter
    }

    private fun daysInMonthArray(date: LocalDate): ArrayList<String> {
        val daysInMonthArray = ArrayList<String>()
        val yearMonth = YearMonth.from(date)

        val daysInMonth = yearMonth.lengthOfMonth()

        val firstOfMonth = selectedDate.withDayOfMonth(1)
        val dayOfWeek = firstOfMonth.dayOfWeek.value

        for (i in 1..42) {
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
                daysInMonthArray.add("")
            } else {
                daysInMonthArray.add((i - dayOfWeek).toString())
            }
        }
        return daysInMonthArray
    }

    private fun monthYearFromDate(date: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern("MMMM yyyy")
        return date.format(formatter)
    }

    private fun previousMonthAction() {
        selectedDate = selectedDate.minusMonths(1)
        setMonthView()
    }

    private fun nextMonthAction() {
        selectedDate = selectedDate.plusMonths(1)
        setMonthView()
    }

    override fun onItemClick(position: Int, dayText: String) {
        if (dayText != "") {
            val monthFormat = DateTimeFormatter.ofPattern("MMMM", Locale.getDefault())
            val yearFormat = DateTimeFormatter.ofPattern("yyyy", Locale.getDefault())
            val formattedDate = monthFormat.format(selectedDate) + " " + dayText + " " + yearFormat.format(selectedDate)

            Log.e("calendar", "opening date $formattedDate")

            val bottomSheetFragment = JournalListBottom()
            val bundle = Bundle()
            bundle.putString("date", formattedDate)
            bottomSheetFragment.arguments = bundle
            bottomSheetFragment.show(parentFragmentManager, "journal_list_bottom_sheet")
        }
    }
}