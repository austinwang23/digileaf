package com.example.digileaf

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class AddPlantActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) {
        }
        setContentView(R.layout.activity_add_plant)

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