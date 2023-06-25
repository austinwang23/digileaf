package com.example.digileaf

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.digileaf.adapter.ReminderAdapter
import com.example.digileaf.adapter.ReminderClickListener
import com.example.digileaf.database.ReminderViewModel
import com.example.digileaf.databinding.FragmentRemindersBinding
import com.example.digileaf.model.Reminder

class Reminders : Fragment(), ReminderClickListener {
    private lateinit var reminderViewModel: ReminderViewModel
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
        reminderViewModel = ViewModelProvider(activity)[ReminderViewModel::class.java]

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
}