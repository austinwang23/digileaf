package com.example.digileaf

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.digileaf.adapter.PlantAdapter
import com.example.digileaf.database.PlantViewModel
import com.example.digileaf.database.PlantViewModelFactory
import com.example.digileaf.entities.Plant

class Home : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var addPlantButton: Button
    private lateinit var addPlantActivityLauncher : ActivityResultLauncher<Intent>
    private val plantViewModel: PlantViewModel by viewModels {
        PlantViewModelFactory((activity?.application as DigileafApplication).plantRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addPlantActivityLauncher = registerForActivityResult(StartActivityForResult()) {result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                if (intent != null && intent.hasExtra("plant")) {
                    Log.e("insertion", "inserting new plant into db")
                    val plant = intent.getParcelableExtra<Plant>("plant")
                    if (plant != null) {
                        plantViewModel.insert(plant)
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val plantAdapter = PlantAdapter()

        // Initialize the RecyclerView
        recyclerView = view.findViewById(R.id.plant_RV)

        // Set up the RecyclerView with the adapter and layout manager
        recyclerView.adapter = plantAdapter
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        plantViewModel.allPlants.observe(viewLifecycleOwner, Observer {
            it.let { plantAdapter.submitList(it) }
        })

        plantAdapter.onItemClick = {
            val intent = Intent(context, ItemDetailsActivity::class.java)
            intent.putExtra("plant", it)
            startActivity(intent)
        }

        plantAdapter.onDeleteClick = {
            Log.e("deletion", "deleting plant from db")
            Toast.makeText(activity, "deleting plant ${it.name}", Toast.LENGTH_SHORT).show()
            plantViewModel.delete(it)
        }

        // Initialize addPlantButton & onClick handler
        addPlantButton = view.findViewById(R.id.plant_add_button)
        addPlantButton.setOnClickListener{
            launchAddPlantActivity()
        }

        return view
    }

    private fun launchAddPlantActivity() {
        val intent = Intent(context, AddPlantActivity::class.java)
        addPlantActivityLauncher.launch(intent)
    }

}