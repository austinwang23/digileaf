package com.example.digileaf.adapter


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.digileaf.JournalListBottom
import com.example.digileaf.R
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale


class CalendarMonthAdapter(
    private val monthDataList: ArrayList<String>,
    private val fragmentManager: FragmentManager
) :
    RecyclerView.Adapter<CalendarMonthViewHolder>(), CalendarAdapter.OnItemListener {

    private lateinit var selectedDate: LocalDate
    private val viewPool = RecyclerView.RecycledViewPool()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarMonthViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.calendar_row, parent, false)
        selectedDate = LocalDate.now()

        return CalendarMonthViewHolder(view)
    }

    private fun monthYearFromDate(date: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern("MMMM yyyy")
        return date.format(formatter)
    }

    private fun daysInMonthArray(date: LocalDate): ArrayList<String> {
        val daysInMonthArray = ArrayList<String>()
        val yearMonth = YearMonth.from(date)

        val daysInMonth = yearMonth.lengthOfMonth()

        val firstOfMonth = date.withDayOfMonth(1)
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

    override fun onBindViewHolder(holder: CalendarMonthViewHolder, position: Int) {
        val month = monthDataList[position]
        holder.month.text = month

        val isCurMonth = monthYearFromDate(selectedDate) == monthYearFromDate(LocalDate.now())

        val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy")
        val monthAsDate = LocalDate.parse("1 $month", formatter)
        val daysOfMonth = daysInMonthArray(monthAsDate)

        Log.d("CalendarMonthAdapter", "daysOfMonth: $daysOfMonth")

        val calendarAdapter = CalendarAdapter(daysOfMonth)
        val childLayoutManager = LinearLayoutManager(holder.itemView.context, LinearLayoutManager.HORIZONTAL, false)
        childLayoutManager.isItemPrefetchEnabled = true
        childLayoutManager.initialPrefetchItemCount = daysOfMonth.size
        childLayoutManager.scrollToPosition(position)
        holder.dayRecyclerView.layoutManager =childLayoutManager
        holder.dayRecyclerView.adapter = calendarAdapter
        calendarAdapter.notifyDataSetChanged()
        holder.dayRecyclerView.setRecycledViewPool(viewPool)
    }

    override fun getItemCount(): Int {
        return monthDataList.size
    }

    override fun onItemClick(position: Int, dayText: String) {
        if (dayText != "") {
            val monthFormat = DateTimeFormatter.ofPattern("MMMM", Locale.getDefault())
            val yearFormat = DateTimeFormatter.ofPattern("yyyy", Locale.getDefault())
            val formattedDate =
                monthFormat.format(selectedDate) + " " + dayText + " " + yearFormat.format(
                    selectedDate
                )

            Log.e("calendar", "opening date $formattedDate")

            val bottomSheetFragment = JournalListBottom()
            val bundle = Bundle()
            bundle.putString("date", formattedDate)
            bottomSheetFragment.arguments = bundle
            bottomSheetFragment.show(fragmentManager, "journal_list_bottom_sheet")
        }
    }
}