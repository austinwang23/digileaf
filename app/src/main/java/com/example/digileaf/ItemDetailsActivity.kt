package com.example.digileaf

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.digileaf.adapter.JournalAdapter
import com.example.digileaf.database.JournalViewModel
import com.example.digileaf.database.JournalViewModelFactory
import com.example.digileaf.database.PlantViewModel
import com.example.digileaf.database.PlantViewModelFactory
import com.example.digileaf.entities.Journal
import com.example.digileaf.entities.Plant
import com.example.digileaf.entities.PlantStatus
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.Locale



class ItemDetailsActivity : AppCompatActivity(), UpdatePlantStatus.UpdatePlantStatusDialogListener {

    private lateinit var journalAdapter: JournalAdapter
    private val journalViewModel: JournalViewModel by viewModels {
        JournalViewModelFactory((this.application as DigileafApplication).journalRepository)
    }
    private val plantViewModel: PlantViewModel by viewModels {
        PlantViewModelFactory((this.application as DigileafApplication).plantRepository)
    }
    private lateinit var addJournalActivityLauncher : ActivityResultLauncher<Intent>

    private lateinit var updatePlantStatusButton: CardView
    lateinit var updatePlantStatus: UpdatePlantStatus
    lateinit var updatePlantStatusCallbackListener: UpdatePlantStatus.UpdatePlantStatusDialogListener
    lateinit var plantStatusWater: TextView
    lateinit var plantStatusFertilize: TextView
    lateinit var plantStatusGroom: TextView
    lateinit var plant: Plant
    private lateinit var emptyJournal: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_details)

        updatePlantStatusCallbackListener = this

        val recyclerView: RecyclerView = findViewById(R.id.journal_RV)
        journalAdapter = JournalAdapter()
        recyclerView.adapter = journalAdapter
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        plant = intent.getParcelableExtra("plant")!!
        val plantName: TextView = findViewById(R.id.detailed_plant_name)
        val plantImage: ImageView = findViewById(R.id.detailed_plant_image)
        val plantSpecies: TextView = findViewById(R.id.detailed_plant_species)
        val plantDescription: TextView = findViewById(R.id.detailed_plant_description)
        emptyJournal = findViewById(R.id.detailed_plant_empty)
        plantStatusWater = findViewById(R.id.plant_last_water)
        plantStatusFertilize = findViewById(R.id.plant_last_fertilize)
        plantStatusGroom = findViewById(R.id.plant_last_groom)

        plantName.text = plant.name
        plantSpecies.text = plant.species
        if (plant.imagePath != "") {
            val imageFile = getFileStreamPath(plant.imagePath)
            // If for whatever reason, the file no longer exists
            if (imageFile == null || !imageFile.exists()) {
                plantImage.setImageResource(R.drawable.`default_plant`)
            } else {
                val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
                plantImage.setImageBitmap(bitmap)
            }
        }
        plantDescription.text = plant.description

        // Plant status
        val currentDate = LocalDate.now()
        val dateFormatter =  DateTimeFormatter.ofPattern("MMMM d yyyy", Locale.getDefault())
        if (plant.lastWater != "") {
            val lastWaterDate = LocalDate.parse(plant.lastWater, dateFormatter)
            val daysBetween = Period.between(lastWaterDate, currentDate).days
            when (daysBetween) {
                0 -> plantStatusWater.text = "Watered today"
                1 -> plantStatusWater.text = "Watered yesterday"
                else -> plantStatusWater.text = "Watered $daysBetween days ago"
            }
        }
        if (plant.lastFertilize != "") {
            val lastFertilizeDate = LocalDate.parse(plant.lastFertilize, dateFormatter)
            val daysBetween = Period.between(lastFertilizeDate, currentDate).days
            when (daysBetween) {
                0 -> plantStatusFertilize.text = "Fertilized today"
                1 -> plantStatusFertilize.text = "Fertilized yesterday"
                else -> plantStatusFertilize.text = "Fertilized $daysBetween days ago"
            }
        }
        if (plant.lastGroom != "") {
            val lastGroomDate = LocalDate.parse(plant.lastGroom, dateFormatter)
            val daysBetween = Period.between(lastGroomDate, currentDate).days
            when (daysBetween) {
                0 -> plantStatusGroom.text = "Groomed today"
                1 -> plantStatusGroom.text = "Groomed yesterday"
                else -> plantStatusGroom.text = "Groomed $daysBetween days ago"
            }
        }

        journalViewModel.allJournalsByPlantId(plant.id).observe(this, Observer { journals ->
            if (journals.isNotEmpty()) {
                emptyJournal.visibility = View.GONE
                journalAdapter.submitList(journals)
            } else {
                emptyJournal.visibility = View.VISIBLE
            }
        })


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

        val addJournalButton: CardView = findViewById(R.id.add_journal_button)
        addJournalButton.setOnClickListener {
            if (plant != null) {
                launchAddJournalActivity(plant.id, plant.name)
            }
        }

        updatePlantStatusButton = findViewById(R.id.update_plant_status_button)
        updatePlantStatusButton.setOnClickListener{
            updatePlantStatus = UpdatePlantStatus(updatePlantStatusCallbackListener)
            updatePlantStatus.show(this.supportFragmentManager, "updatePlantStatus")
        }
    }

    override fun onBackPressed() {
        finish()
    }

    override fun updatePlantStatus(watered: Boolean, fertilized: Boolean, groomed: Boolean) {
        // Do plant status update here
        val currentDate = LocalDate.now()
        val dateFormat = DateTimeFormatter.ofPattern("MMMM d yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(currentDate)
        val plantStatus = PlantStatus(plant.id, plant.lastWater, plant.lastFertilize, plant.lastGroom)
        if (watered) {
            plantStatusWater.text = "Watered today"
            plantStatus.lastWater = formattedDate
        }
        if (fertilized) {
            plantStatusFertilize.text = "Fertilized today"
            plantStatus.lastFertilize = formattedDate
        }
        if (groomed) {
            plantStatusGroom.text = "Groomed today"
            plantStatus.lastGroom = formattedDate
        }

        plantViewModel.updatePlantStatus(plantStatus)
    }

    private fun launchAddJournalActivity(plantId: Int, plantName: String) {
        val intent = Intent(this, AddJournalEntryActivity::class.java)
        intent.putExtra("plantId", plantId)
        intent.putExtra("plantName", plantName)
        addJournalActivityLauncher.launch(intent)
    }
}