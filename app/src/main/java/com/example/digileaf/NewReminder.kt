package com.example.digileaf

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.NotificationManager
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.digileaf.database.ReminderModelFactory
import com.example.digileaf.database.ReminderViewModel
import com.example.digileaf.databinding.FragmentNewReminderBinding
import com.example.digileaf.entities.Reminder
import com.example.digileaf.entities.RepetitionType
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.time.LocalDate
import java.time.LocalTime
import com.example.digileaf.helpers.NotificationHelper


class NewReminder(private var reminderItem: Reminder?) : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentNewReminderBinding
    private lateinit var repetitionDropdown: AutoCompleteTextView
    private var dueTime: LocalTime? = null
    private var dueDate: LocalDate? = null
    private val reminderViewModel: ReminderViewModel by viewModels {
        ReminderModelFactory((activity?.application as DigileafApplication).reminderRepository)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize repetition spinner
        repetitionDropdown = binding.repetitionDropdown
        val repetitionTypes = RepetitionType.values()
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, repetitionTypes.map { it.text })
        repetitionDropdown.setAdapter(adapter)

        createNotificationChannel()

        if (reminderItem != null) {
            // Editing a reminder
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
            val selectedRepetitionType = reminderItem!!.repetitionType.text
            repetitionDropdown.setText(selectedRepetitionType, false)
        } else {
            // Creating a new reminder
            binding.reminderTitle.text = "New Reminder"
        }

        binding.datePickerButton.setOnClickListener {
            openDatePicker()
        }
        binding.timePickerButton.setOnClickListener {
            openTimePicker()
        }
        binding.saveButton.setOnClickListener {
            saveAction()
        }
    }


    private fun openDatePicker() {
        if (dueDate == null) {
            dueDate = LocalDate.now()
        }
        val listener = DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = LocalDate.of(selectedYear, selectedMonth + 1, selectedDay)
            val currentTime = LocalTime.of(LocalTime.now().hour, LocalTime.now().minute)

            if (dueTime != null && dueTime!!.isBefore(currentTime) && selectedDate.isEqual(LocalDate.now())) {
                Toast.makeText(requireContext(), "Please select a future time.", Toast.LENGTH_SHORT).show()
            } else {
                dueDate = selectedDate
                updateDateButtonText()
            }
        }

        // Set up date picker dialog
        val dialog = DatePickerDialog(requireContext(), listener, dueDate!!.year, dueDate!!.monthValue - 1, dueDate!!.dayOfMonth)
        dialog.datePicker.minDate = System.currentTimeMillis() - 1000;
        dialog.show()
    }
    private fun openTimePicker() {
        if (dueTime == null) {
            dueTime = LocalTime.now()
        }
        val listener = TimePickerDialog.OnTimeSetListener { _, selectedHour, selectedMinute ->
            val selectedTime = LocalTime.of(selectedHour, selectedMinute)
            val currentTime = LocalTime.of(LocalTime.now().hour, LocalTime.now().minute)

            if (dueDate != null && dueDate!!.isEqual(LocalDate.now()) && selectedTime.isBefore(currentTime)) {
                Toast.makeText(requireContext(), "Please select a future time.", Toast.LENGTH_SHORT).show()
            } else {
                dueTime = selectedTime
                updateTimeButtonText()
            }
        }

        // Set up time picker dialog
        val dialog = TimePickerDialog(requireContext(), listener, dueTime!!.hour, dueTime!!.minute, true)
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
    ): View {
        binding = FragmentNewReminderBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun saveAction() {
        val title = binding.title.text.toString()
        val desc = binding.desc.text.toString()
        val dueDateString = if (dueDate == null) null else Reminder.dateFormatter.format(dueDate)
        val dueTimeString = if (dueTime == null) null else Reminder.timeFormatter.format(dueTime)
        val selectedRepetitionType = repetitionDropdown.text.toString()
        val repetitionType =
            RepetitionType.values().firstOrNull { it.text == selectedRepetitionType } ?: RepetitionType.NEVER

        // Editing a reminder
        if (reminderItem != null) {
            reminderItem!!.title = title
            reminderItem!!.desc = desc
            reminderItem!!.dueDateString = dueDateString
            reminderItem!!.dueTimeString = dueTimeString
            reminderItem!!.repetitionType = repetitionType
            reminderViewModel.updateReminder(reminderItem!!)

            // Cancel current notification and schedule a new one
            cancelNotification()
            NotificationHelper.scheduleNotification(requireContext(), reminderItem!!)
        }
        // Creating a new reminder
        else {
            reminderItem = Reminder(title, desc, dueTimeString, dueDateString, null, repetitionType)
            reminderViewModel.addReminder(reminderItem!!) {
                NotificationHelper.scheduleNotification(requireContext(), reminderItem!!)
            }
        }

        binding.title.setText("")
        binding.desc.setText("")
        dismiss()
    }



    private fun cancelNotification() {
        val context: Context = requireContext()
        val notificationId = reminderItem!!.id
        NotificationHelper.cancelNotification(context, notificationId)
    }

    private fun createNotificationChannel() {
        val context: Context = requireContext()
        NotificationHelper.createNotificationChannel(
            context,
            channelID,
            "Reminder Channel",
            "Send notifications for plant reminders",
            NotificationManager.IMPORTANCE_DEFAULT
        )
    }
}