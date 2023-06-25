package com.example.digileaf

import android.app.TimePickerDialog
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.digileaf.database.ReminderModelFactory
import com.example.digileaf.database.ReminderViewModel
import com.example.digileaf.databinding.FragmentNewReminderBinding
import com.example.digileaf.entities.Reminder
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.time.LocalTime

class NewReminder(var reminderItem: Reminder?) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentNewReminderBinding
//    private lateinit var reminderViewModel: ReminderViewModel
    private var dueTime: LocalTime? = null
    private val reminderViewModel: ReminderViewModel by viewModels {
        ReminderModelFactory((activity?.application as DigileafApplication).reminderRepository)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity()

        // editing a reminder
        if (reminderItem != null) {
            binding.reminderTitle.text = "Edit Reminder"
            val editable = Editable.Factory.getInstance()
            binding.title.text = editable.newEditable(reminderItem!!.title)
            binding.desc.text = editable.newEditable(reminderItem!!.desc)
            if (reminderItem!!.dueTime() != null) {
                dueTime = reminderItem!!.dueTime()
                updateTimeButtonText()
            }
        }
        // creating a new reminder
        else {
            binding.reminderTitle.text = "New Reminder"
        }
//        reminderViewModel = ViewModelProvider(activity)[ReminderViewModel::class.java]
        binding.timePickerButton.setOnClickListener{
            openTimePicker()
        }
        binding.saveButton.setOnClickListener{
            saveAction()
        }
    }

    private fun openTimePicker() {
        if (dueTime == null) {
            dueTime = LocalTime.now()
        }
        val listener = TimePickerDialog.OnTimeSetListener{ _, selectedHour, selectedMinute ->
            dueTime = LocalTime.of(selectedHour, selectedMinute)
            updateTimeButtonText()
        }

        //set up time picker dialog
        val dialog = TimePickerDialog(activity, listener, dueTime!!.hour, dueTime!!.minute, true)
        dialog.setTitle("Reminder Time")
        dialog.show()
    }

    private fun updateTimeButtonText() {
        binding.timePickerButton.text = String.format("%02d:%02d", dueTime!!.hour, dueTime!!.minute)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewReminderBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    private fun saveAction() {
        val title = binding.title.text.toString()
        val desc = binding.desc.text.toString()
        val dueTimeString = if (dueTime == null) null else Reminder.timeFormatter.format(dueTime)

        // editing a reminder
        if (reminderItem != null) {
            reminderItem!!.title = title
            reminderItem!!.desc = desc
            reminderItem!!.dueTimeString = dueTimeString
            reminderViewModel.updateReminder(reminderItem!!)
        }
        // creating a new reminder
        else {
            val newReminder = Reminder(title, desc, dueTimeString, null)
            reminderViewModel.addReminder(newReminder)
        }

        binding.title.setText("")
        binding.desc.setText("")
        dismiss()
    }
}