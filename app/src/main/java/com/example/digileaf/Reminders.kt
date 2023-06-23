package com.example.digileaf

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.digileaf.model.Plant
import com.example.digileaf.model.Reminder

/**
 * A simple [Fragment] subclass.
 * Use the [Reminders.newInstance] factory method to
 * create an instance of this fragment.
 */
class Reminders : Fragment() {
    private lateinit var reminderList: ArrayList<Reminder>
    private lateinit var createReminder: AppCompatButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var reminderAdapter: ReminderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Initialize plants
        reminderList = ArrayList<Reminder>()
        reminderList.add(Reminder("Timothy", "06-23-2023", "1:29 PM"))
        reminderList.add(Reminder("Johnny",  "06-24-2023", "1:29 PM"))
        reminderList.add(Reminder("Carla", "06-25-2023", "1:29 PM"))
        reminderList.add(Reminder("Timothy",  "06-26-2023", "1:29 PM"))
        reminderList.add(Reminder("Johnny",  "06-27-2023", "1:29 PM"))
        reminderList.add(Reminder("Carla",  "06-28-2023", "1:29 PM"))
        reminderList.add(Reminder("Timothy",  "06-29-2023", "1:29 PM"))
        reminderList.add(Reminder("Johnny",  "06-30-2023", "1:29 PM"))
        reminderList.add(Reminder("Carla",  "07-01-2023", "1:29 PM"))

        plantAdapter = PlantAdapter(plantList)

        plantAdapter.onItemClick = {
            val intent = Intent(context, ItemDetailsActivity::class.java)
            intent.putExtra("plant", it)
            startActivity(intent)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_reminders, container, false)

        // Initialize the RecyclerView
        recyclerView = view.findViewById(R.id.reminder_RV)

        // Set up the RecyclerView with the adapter and layout manager
        recyclerView.adapter = plantAdapter
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        return view
    }
}