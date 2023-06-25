package com.example.digileaf

import android.app.Activity
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import com.example.digileaf.entities.Plant


class AddPlantActivity : AppCompatActivity() {
    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE = 2
        private const val TAKE_PICTURE_REQUEST = 3
    }

    private lateinit var selectImageButton: Button
    private lateinit var takePictureButton: Button
    private lateinit var backButton: AppCompatImageButton
    private lateinit var addPlantButton: Button
    private lateinit var imageView: ImageView

    private lateinit var plantName : EditText
    private lateinit var plantSpecies : EditText
    private lateinit var plantDescription : EditText

    private val PICK_IMAGE_REQUEST = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) {
        }
        setContentView(R.layout.activity_add_plant)
        selectImageButton = findViewById(R.id.select_image_button)
        takePictureButton = findViewById(R.id.take_picture_button)

        addPlantButton = findViewById(R.id.add_plant)
        backButton = findViewById(R.id.back_button)
        imageView = findViewById(R.id.image_view)

        selectImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

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

        plantName = findViewById(R.id.editName)
        plantSpecies = findViewById(R.id.editType)
        plantDescription = findViewById(R.id.editDescription)

        backButton.setOnClickListener {
            val addPlantIntent = Intent()
            setResult(Activity.RESULT_CANCELED, addPlantIntent)
            finish()
        }

        addPlantButton.setOnClickListener {
            val addPlantIntent = Intent()
            if (!(TextUtils.isEmpty(plantName.text) ||
                   TextUtils.isEmpty(plantSpecies.text) ||
                   TextUtils.isEmpty(plantDescription.text))) {
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

                val plant = Plant(plantName.text.toString(), plantSpecies.text.toString(), plantDescription.text.toString(), imagePath)
                addPlantIntent.putExtra("plant", plant)
                setResult(Activity.RESULT_OK, addPlantIntent)
                finish()
            }
        }
    }

    private fun startCameraActivity() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, TAKE_PICTURE_REQUEST)
        }

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
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            val imageUri = data.data
            val imageBitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(imageUri!!)) // prob handle this null better
            imageView.setImageBitmap(imageBitmap)
        }
        else if (requestCode == TAKE_PICTURE_REQUEST && resultCode == RESULT_OK && data != null) {
            val imageBitmap = data.extras?.get("data") as Bitmap
            imageView.setImageBitmap(imageBitmap)
        }
    }
}