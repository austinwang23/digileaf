package com.example.digileaf

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
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
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)


        val plant = intent.getParcelableExtra<Plant>("plant")
        if(plant != null){
            val plantName : TextView = findViewById(R.id.detailed_plant_name)
            val plantImage : ImageView = findViewById(R.id.detailed_plant_image)
            val plantSpecies : TextView = findViewById(R.id.detailed_plant_species)
            val plantDescription: TextView = findViewById(R.id.detailed_plant_description)

            plantName.text = plant.name
            plantSpecies.text = plant.species
            // TODO - replace with image path
            plantImage.setImageResource(R.drawable.image_1)
            plantDescription.text = plant.description

            journalViewModel

            journalViewModel.allJournalsByPlantId(plant.id).observe(this, Observer {
                it.let { journalAdapter.submitList(it) }
            })
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