package com.example.digileaf

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.example.digileaf.model.Plant

class ItemDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_details)

        val plant = intent.getParcelableExtra<Plant>("plant")
        if(plant != null){
            val plantName : TextView = findViewById(R.id.detailed_plant_name)
            val plantImage : ImageView = findViewById(R.id.detailed_plant_image)
            val plantSpecies : TextView = findViewById(R.id.detailed_plant_species)

            plantName.text = plant.plantName
            plantSpecies.text = plant.plantSpecies
            plantImage.setImageResource(plant.plantImage)
        }

        val backButton : ImageButton = findViewById(R.id.back_button)
        backButton.setOnClickListener{
            finish()
        }
    }
    override fun onBackPressed() {
        finish()
    }
}