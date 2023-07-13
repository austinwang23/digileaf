package com.example.digileaf

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.digileaf.database.ReminderModelFactory
import com.example.digileaf.database.ReminderViewModel
import com.example.digileaf.databinding.FragmentNewReminderBinding
import com.example.digileaf.entities.Reminder
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.time.LocalDate
import java.time.LocalTime

class NewReminder(var reminderItem: Reminder?) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentNewReminderBinding
    private var dueTime: LocalTime? = null
    private var dueDate: LocalDate? = null
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
            if (reminderItem!!.dueDate() != null) {
                dueDate = reminderItem!!.dueDate()
                updateDateButtonText()
            }
        }
        // creating a new reminder
        else {
            binding.reminderTitle.text = "New Reminder"
        }

        binding.datePickerButton.setOnClickListener{
            openDatePicker()
        }
        binding.timePickerButton.setOnClickListener{
            openTimePicker()
        }
        binding.saveButton.setOnClickListener{
            saveAction()
        }
    }

    private fun openDatePicker() {
        if (dueDate == null) {
            dueDate = LocalDate.now()
        }
        val listener = DatePickerDialog.OnDateSetListener{ _, selectedYear, selectedMonth, selectedDay ->
            dueDate = LocalDate.of(selectedYear, selectedMonth, selectedDay)
            updateDateButtonText()
        }

        //set up time picker dialog
        val dialog = DatePickerDialog(requireContext(), listener, dueDate!!.year, dueDate!!.monthValue, dueDate!!.dayOfMonth)
//        dialog.setTitle("Reminder Time")
        dialog.show()
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
//        dialog.setTitle("Reminder Time")
        dialog.show()
    }

    private fun updateDateButtonText() {
        binding.datePickerButton.text = String.format("%02d-%02d-%04d", dueDate!!.monthValue, dueDate!!.dayOfMonth, dueDate!!.year)
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
        val dueDateString = if (dueDate == null) null else Reminder.dateFormatter.format(dueDate)
        val dueTimeString = if (dueTime == null) null else Reminder.timeFormatter.format(dueTime)

        // editing a reminder
        if (reminderItem != null) {
            reminderItem!!.title = title
            reminderItem!!.desc = desc
            reminderItem!!.dueDateString = dueDateString
            reminderItem!!.dueTimeString = dueTimeString
            reminderViewModel.updateReminder(reminderItem!!)
        }
        // creating a new reminder
        else {
            val newReminder = Reminder(title, desc, dueTimeString, dueDateString, null)
            reminderViewModel.addReminder(newReminder)
        }

        binding.title.setText("")
        binding.desc.setText("")
        dismiss()
    }
}