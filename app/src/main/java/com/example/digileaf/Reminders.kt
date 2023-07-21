package com.example.digileaf

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.digileaf.adapter.ReminderAdapter
import com.example.digileaf.adapter.ReminderClickListener
import com.example.digileaf.database.ReminderModelFactory
import com.example.digileaf.database.ReminderViewModel
import com.example.digileaf.databinding.FragmentRemindersBinding
import com.example.digileaf.entities.Reminder
import com.example.digileaf.util.isNotificationPermissionGranted

class Reminders : Fragment(), ReminderClickListener {
    private val reminderViewModel: ReminderViewModel by viewModels {
        ReminderModelFactory((activity?.application as DigileafApplication).reminderRepository)
    }
    private lateinit var addReminderButton: CardView
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

        // Initialize addReminderButton & onClick handler
        addReminderButton = view.findViewById(R.id.reminder_add_button)
        addReminderButton.setOnClickListener{
            if(isNotificationPermissionGranted(requireContext())) {
                NewReminder(null).show(parentFragmentManager, "newReminderTag")
            } else {
                // Open dialog telling user that notification permissions are required for reminders
                showNotificationPermissionDialog()
            }

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
        cancelNotification(reminder)
    }

    private fun cancelNotification(reminder: Reminder) {
        val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val applicationContext = requireActivity().applicationContext

        // get previous alarmIntent by reminder id
        val alarmIntent = PendingIntent.getBroadcast(
            applicationContext,
            reminder.id,
            Intent(applicationContext, Notification::class.java),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        alarmManager.cancel(
            alarmIntent
        )
    }

    private fun showNotificationPermissionDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_notification_permission_required, null)
        val dialogBuilder = AlertDialog.Builder(requireContext())
            .setView(dialogView)
        val alertDialog = dialogBuilder.create()

        val btnGotoSettings = dialogView.findViewById<Button>(R.id.btn_goto_settings)
        btnGotoSettings.setOnClickListener {
            alertDialog.dismiss()
            navigateToAppNotificationSettings()
        }

        val btnCloseDialog = dialogView.findViewById<Button>(R.id.btn_close_dialog)
        btnCloseDialog.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    private fun navigateToAppNotificationSettings() {
        // Open the app's notification settings page to allow the user to grant notification permissions.
        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, requireContext().packageName)
        startActivity(intent)
    }
}