package com.example.digileaf

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.digileaf.adapter.JournalAdapter
import com.example.digileaf.database.JournalViewModel
import com.example.digileaf.database.JournalViewModelFactory
import com.example.digileaf.entities.Plant


class ItemDetailsActivity : AppCompatActivity() {

    private lateinit var journalAdapter: JournalAdapter
    private val journalViewModel: JournalViewModel by viewModels {
        JournalViewModelFactory((this.application as DigileafApplication).journalRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_details)

        val recyclerView: RecyclerView = findViewById(R.id.journal_RV)
        journalAdapter = JournalAdapter()
        recyclerView.adapter = journalAdapter
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val plant = intent.getParcelableExtra<Plant>("plant")
        if (plant != null) {
            val plantName: TextView = findViewById(R.id.detailed_plant_name)
            val plantImage: ImageView = findViewById(R.id.detailed_plant_image)
            val plantSpecies: TextView = findViewById(R.id.detailed_plant_species)
            val plantDescription: TextView = findViewById(R.id.detailed_plant_description)

            plantName.text = plant.name
            plantSpecies.text = plant.species
            if (plant.imagePath != "") {
                val imageFile = getFileStreamPath(plant.imagePath)
                // If for whatever reason, the file no longer exists
                if (imageFile == null || !imageFile.exists()) {
                    plantImage.setImageResource(R.drawable.default_plant)
                } else {
                    val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
                    plantImage.setImageBitmap(bitmap)
                }
            }
            plantDescription.text = plant.description

            journalViewModel

            journalViewModel.allJournalsByPlantId(plant.id).observe(this, Observer {
                it.let { journalAdapter.submitList(it) }
            })
        }

        journalAdapter.onItemClick = {
            val intent = Intent(this, JournalActivity::class.java)
            intent.putExtra("journal", it)
            if (plant != null) {
                intent.putExtra("plant", plant)
            }
            startActivity(intent)
        }

        val backButton: ImageButton = findViewById(R.id.back_button)
        backButton.setOnClickListener {
            finish()
        }
    }

    override fun onBackPressed() {
        finish()
    }
}