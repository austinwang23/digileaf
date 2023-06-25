package com.example.digileaf

import android.app.Activity
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.digileaf.entities.Journal
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale


class AddJournalEntryActivity : AppCompatActivity() {
    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE = 2
        private const val TAKE_PICTURE_REQUEST = 3
    }

    private lateinit var editDescription: EditText
    private lateinit var selectImageButton: Button
    private lateinit var takePictureButton: Button

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
        takePictureButton = findViewById(R.id.take_picture_button)
        imageView = findViewById(R.id.image_view)

        // Select image button click listener
        selectImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, SELECT_IMAGE_REQUEST_CODE)
        }

        // Camera Button listener
        takePictureButton.setOnClickListener {
            // Check if the camera permission is granted
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Request the camera permission if it is not granted
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    CAMERA_PERMISSION_REQUEST_CODE
                )
            } else {
                // Start the camera activity to take a picture
                startCameraActivity()
            }
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
            val plantName = intent.getStringExtra("plantName")
            val addJournalIntent = Intent()
            if (!(TextUtils.isEmpty(editDescription.text))) {
                val currentDate = LocalDate.now()
                val dateFormat = DateTimeFormatter.ofPattern("MMMM d yyyy", Locale.getDefault())
                val formattedDate = dateFormat.format(currentDate)

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

                val journalEntry = Journal(formattedDate, editDescription.text.toString(), imagePath, plantId, plantName)
                addJournalIntent.putExtra("journal", journalEntry)
                setResult(Activity.RESULT_OK, addJournalIntent)
                finish()
            }
        }
    }


    private fun startCameraActivity() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(takePictureIntent, TAKE_PICTURE_REQUEST)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Camera permission granted, start the camera activity
                startCameraActivity()
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    //    Probably should combine this with the other image stuff into one component
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SELECT_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            val selectedImageUri = data.data
            // Handle selected image URI here
            imageView.setImageURI(selectedImageUri)
        }
        else if (requestCode == TAKE_PICTURE_REQUEST && resultCode == RESULT_OK && data != null) {
            val imageBitmap = data.extras?.get("data") as Bitmap
            imageView.setImageBitmap(imageBitmap)
        }
    }
}
