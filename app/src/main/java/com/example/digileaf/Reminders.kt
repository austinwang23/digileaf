package com.example.digileaf

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.digileaf.adapter.ReminderAdapter
import com.example.digileaf.adapter.ReminderClickListener
import com.example.digileaf.database.PlantViewModelFactory
import com.example.digileaf.database.ReminderModelFactory
import com.example.digileaf.database.ReminderViewModel
import com.example.digileaf.databinding.FragmentRemindersBinding
import com.example.digileaf.entities.Reminder

class Reminders : Fragment(), ReminderClickListener {
    private val reminderViewModel: ReminderViewModel by viewModels {
        ReminderModelFactory((activity?.application as DigileafApplication).reminderRepository)
    }
    private lateinit var addReminderButton: Button
    private lateinit var binding: FragmentRemindersBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRemindersBinding.inflate(inflater, container, false)
        val view = binding.root

        val activity = requireActivity()

        // Initialize addReminderButton & onClick handler
        addReminderButton = view.findViewById(R.id.reminder_add_button)
        addReminderButton.setOnClickListener{
            NewReminder(null).show(parentFragmentManager, "newReminderTag")
        }

        setRecyclerView()
        return view
    }

    private fun setRecyclerView() {
        val activity = requireActivity()
        reminderViewModel.reminderItems.observe(activity) {
            binding.reminderRV.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = ReminderAdapter(it as ArrayList<Reminder>, this@Reminders)
            }
        }
    }

    override fun editReminder(reminder: Reminder) {
        NewReminder(reminder).show(parentFragmentManager, "newReminderTag")
    }

    override fun completeReminder(reminder: Reminder) {
        reminderViewModel.setCompleted(reminder)
    }

    override fun deleteReminder(reminder: Reminder) {
        reminderViewModel.deleteReminder(reminder)
    }
}