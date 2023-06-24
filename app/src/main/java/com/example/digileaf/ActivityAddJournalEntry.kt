package com.example.digileaf

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar


class ActivityAddJournalEntry : AppCompatActivity() {

    private lateinit var datePicker: DatePicker
    private lateinit var editName: EditText
    private lateinit var editType: EditText
    private lateinit var editDescription: EditText
    private lateinit var selectImageButton: Button
    private lateinit var imageView: ImageView

    private val SELECT_IMAGE_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_journal_entry)

        // Initialize views
        datePicker = findViewById(R.id.datePicker)
        editDescription = findViewById(R.id.editDescription)
        selectImageButton = findViewById(R.id.select_image_button)
        imageView = findViewById(R.id.image_view)

        // Set current date as default in the custom date picker
        val currentDate = Calendar.getInstance()
        val currentYear = currentDate.get(Calendar.YEAR)
        val currentMonth = currentDate.get(Calendar.MONTH)
        val currentDay = currentDate.get(Calendar.DAY_OF_MONTH)
        datePicker.init(currentYear, currentMonth, currentDay, null)

        // Date picker dialog listener
        datePicker.setOnDateChangedListener { view, year, monthOfYear, dayOfMonth ->
            // Handle date change here
            // You can access the selected date using year, monthOfYear, and dayOfMonth
        }

        // Select image button click listener
        selectImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, SELECT_IMAGE_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SELECT_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            val selectedImageUri = data.data
            // Handle selected image URI here
            imageView.setImageURI(selectedImageUri)
        }
    }
}
