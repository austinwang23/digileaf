package com.example.digileaf

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.digileaf.adapter.JournalAdapter
import com.example.digileaf.database.JournalViewModel
import com.example.digileaf.database.JournalViewModelFactory
import com.example.digileaf.entities.Journal
import com.example.digileaf.entities.Plant


class ItemDetailsActivity : AppCompatActivity() {

    private lateinit var journalAdapter: JournalAdapter
    private val journalViewModel: JournalViewModel by viewModels {
        JournalViewModelFactory((this.application as DigileafApplication).journalRepository)
    }
    private lateinit var addJournalActivityLauncher : ActivityResultLauncher<Intent>

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
            startActivity(intent)
        }

        addJournalActivityLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                if (intent != null && intent.hasExtra("journal")) {
                    Log.e("insertion", "inserting new journal into db")
                    val journal = intent.getParcelableExtra<Journal>("journal")
                    if (plant != null && journal != null) {
                        journalViewModel.insert(journal)
                    }
                }
            }
        }

        val backButton : ImageButton = findViewById(R.id.back_button)
        backButton.setOnClickListener{
            finish()
        }

        val addJournalButton: AppCompatButton = findViewById(R.id.add_journal_button)
        addJournalButton.setOnClickListener {
            if (plant != null) {
                launchAddJournalActivity(plant.id, plant.name)
            }
        }
    }

    override fun onBackPressed() {
        finish()
    }

    private fun launchAddJournalActivity(plantId: Int, plantName: String) {
        val intent = Intent(this, AddJournalEntryActivity::class.java)
        intent.putExtra("plantId", plantId)
        intent.putExtra("plantName", plantName)
        addJournalActivityLauncher.launch(intent)
    }
}