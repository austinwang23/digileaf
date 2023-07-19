package com.example.digileaf

import android.R
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.viewModels
import com.example.digileaf.database.ReminderModelFactory
import com.example.digileaf.database.ReminderViewModel
import com.example.digileaf.databinding.FragmentNewReminderBinding
import com.example.digileaf.entities.Reminder
import com.example.digileaf.entities.RepetitionType
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.time.LocalDate
import java.time.LocalTime
import java.util.Calendar

class NewReminder(var reminderItem: Reminder?) : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentNewReminderBinding
    private lateinit var repetitionSpinner: Spinner
    private var dueTime: LocalTime? = null
    private var dueDate: LocalDate? = null
    private val reminderViewModel: ReminderViewModel by viewModels {
        ReminderModelFactory((activity?.application as DigileafApplication).reminderRepository)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        // initialize repetition spinner
        repetitionSpinner = binding.repetitionSpinner
        val repetitionTypes = RepetitionType.values()
        val adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, repetitionTypes.map{ it.text })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        repetitionSpinner.adapter = adapter

        createNotificationChannel()

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
            dueDate = LocalDate.of(selectedYear, selectedMonth + 1, selectedDay)
            updateDateButtonText()
        }

        //set up date picker dialog
        val dialog = DatePickerDialog(requireContext(), listener, dueDate!!.year, dueDate!!.monthValue - 1, dueDate!!.dayOfMonth)
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

            // cancel current notification and schedule a new one
            cancelNotification()
            scheduleNotification(reminderItem!!.id)
        }
        // creating a new reminder
        else {
            reminderItem = Reminder(title, desc, dueTimeString, dueDateString, null)
            reminderViewModel.addReminder(reminderItem!!) { id ->
                scheduleNotification(id.toInt())
            }
        }

        binding.title.setText("")
        binding.desc.setText("")
        dismiss()
    }

    private fun scheduleNotification(id: Int) {
        Log.d("reminder-notification", reminderItem!!.repetitionType.text)

        if (dueDate == null && dueTime == null) {
            return
        }

        // defaults for date and time if not set
        if (dueDate == null) {
            dueDate = LocalDate.now()
        }
        else if (dueTime == null) {
            dueTime = LocalTime.of(9, 0)
        }

        val applicationContext = requireActivity().applicationContext
        val intent = Intent(applicationContext, Notification::class.java)
        val title = reminderItem?.title.toString()
        val message = reminderItem?.desc.toString()

        intent.putExtra(titleExtra, title)
        intent.putExtra(messageExtra, message)

        // define intent to send a notification
        val alarmIntent = PendingIntent.getBroadcast(
            applicationContext,
            id,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time = getTime()

        if (reminderItem!!.repetitionType == RepetitionType.NEVER) {
            Log.d("reminder-notification", "non-repeating reminder")
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                time,
                alarmIntent
            )
        }
        else {
            Log.d("reminder-notification", "repeating reminder")
    //        alarmManager.setRepeating(
    //            AlarmManager.RTC_WAKEUP,
    //            time,
    //            AlarmManager.INTERVAL_HOUR,
    //            alarmIntent
    //        )
        }
    }

    private fun cancelNotification() {
        val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val applicationContext = requireActivity().applicationContext

        // get previous alarmIntent by reminder id
        val alarmIntent = PendingIntent.getBroadcast(
            applicationContext,
            reminderItem!!.id,
            Intent(applicationContext, Notification::class.java),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        alarmManager.cancel(
            alarmIntent
        )
    }

    private fun getTime(): Long {
        val minute = dueTime!!.minute
        val hour = dueTime!!.hour
        val day = dueDate!!.dayOfMonth
        val month = dueDate!!.monthValue - 1
        val year = dueDate!!.year

        val calendar = Calendar.getInstance()
        calendar.set(year, month, day, hour, minute)
        return calendar.timeInMillis
    }

    private fun createNotificationChannel() {
        val name = "Reminder Channel"
        val desc = "Send notifications for plant reminders"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelID, name, importance)
        channel.description = desc

        val notificationManager = requireActivity().getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}