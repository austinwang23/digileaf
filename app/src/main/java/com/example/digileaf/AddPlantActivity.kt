package com.example.digileaf

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import java.io.File

class AddPlantActivity : AppCompatActivity() {
    private lateinit var selectImageButton: Button
    private lateinit var takePhotoButton: Button
    private lateinit var imageView: ImageView

    private val PICK_IMAGE_REQUEST = 1
    private val TAKE_PHOTO_REQUEST = 2

    private var imageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) {
        }
        setContentView(R.layout.activity_add_plant)

        selectImageButton = findViewById(R.id.select_image_button)
        takePhotoButton = findViewById(R.id.take_photo_button)
        imageView = findViewById(R.id.image_view)

        selectImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        takePhotoButton.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val photoFile = createImageFile()
            imageUri = Uri.fromFile(photoFile)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            startActivityForResult(intent, TAKE_PHOTO_REQUEST)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                PICK_IMAGE_REQUEST -> {
                    if (data != null) {
                        imageUri = data.data
                        displayImage()
                    }
                }
                TAKE_PHOTO_REQUEST -> {
                    if (imageUri != null) {
                        displayImage()
                    }
                }
            }
        }
    }

    private fun createImageFile(): File {
        // Create a file to save the image
        val storageDir = getExternalFilesDir(null)
        return File.createTempFile("image_", ".jpg", storageDir)
    }

    private fun displayImage() {
        val imageBitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(imageUri!!))
        imageView.setImageBitmap(imageBitmap)
    }
}