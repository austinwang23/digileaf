package com.example.digileaf

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import com.example.digileaf.entities.Journal
import java.util.Calendar


class AddJournalEntryActivity : AppCompatActivity() {
    private lateinit var editDescription: EditText
    private lateinit var selectImageButton: Button
    private lateinit var imageView: ImageView

    private lateinit var backButton: AppCompatImageButton
    private lateinit var addJournalButton: Button

    private val SELECT_IMAGE_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_journal_entry)

        // Initialize views
        editDescription = findViewById(R.id.editDescription)
        selectImageButton = findViewById(R.id.select_image_button)
        imageView = findViewById(R.id.image_view)

        // Select image button click listener
        selectImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, SELECT_IMAGE_REQUEST_CODE)
        }

        // Set up back button listener
        backButton = findViewById(R.id.back_button)
        backButton.setOnClickListener {
            val addJournalIntent = Intent()
            setResult(Activity.RESULT_CANCELED, addJournalIntent)
            finish()
        }

        // Set up save button listener
        addJournalButton = findViewById(R.id.add_journal_entry)
        addJournalButton.setOnClickListener {
            val plantId = intent.getIntExtra("plantId", -1)
            val addJournalIntent = Intent()
            if (!(TextUtils.isEmpty(editDescription.text))) {
                val currentDate = Calendar.getInstance()

                var imagePath = ""
                val drawable = imageView.drawable
                if (drawable != null) {
                    val bitmap = (drawable as BitmapDrawable).bitmap
                    val fileName = String.format("%d.jpg", System.currentTimeMillis())
                    val fOut = openFileOutput(fileName, MODE_PRIVATE)

                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                    fOut.flush()
                    fOut.close()
                    imagePath = fileName
                }

                val journalEntry = Journal(currentDate.time.time, editDescription.text.toString(), imagePath, plantId)
                addJournalIntent.putExtra("journal", journalEntry)
                setResult(Activity.RESULT_OK, addJournalIntent)
                finish()
            }
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