package com.example.digileaf

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView

class AddPlantActivity : AppCompatActivity() {
    private lateinit var selectImageButton: Button
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
        imageView = findViewById(R.id.image_view)

        selectImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            val imageUri = data.data
            val imageBitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(imageUri!!)) // prob handle this null better
            imageView.setImageBitmap(imageBitmap)
        }
        plantName = findViewById(R.id.add_plant_title)

//        val plant = intent.getParcelableExtra<Plant>("plant")
//        if(plant != null){
//            val plantName : TextView = findViewById(R.id.detailed_plant_name)
//            val plantImage : ImageView = findViewById(R.id.detailed_plant_image)
//            val plantSpecies : TextView = findViewById(R.id.detailed_plant_species)
//
//            plantName.text = plant.plantName
//            plantSpecies.text = plant.plantSpecies
//            plantImage.setImageResource(plant.plantImage)
//        }
    }
}