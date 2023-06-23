package com.example.digileaf

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.digileaf.adapter.PlantAdapter
import com.example.digileaf.database.PlantViewModel
import com.example.digileaf.database.PlantViewModelFactory

class Home : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var addPlantButton: Button
    private val plantViewModel: PlantViewModel by viewModels {
        PlantViewModelFactory((activity?.application as DigileafApplication).plantRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // hardcode
        //Initialize plants
        /* plantList = ArrayList()
        plantList.add(Plant("Timothy", "2 days", "Spiky Plant", "asdlfkjaslkdfjasd", R.drawable.image_1))
        plantList.add(Plant("Johnny", "1 days", "Pretty Plant", "asdlfkjaslkdfjasd", R.drawable.image_2))
        plantList.add(Plant("Carla", "2 mos", "Ugly Plant", "asdlfkjaslkdfjasd", R.drawable.image_3))
        plantList.add(Plant("Timothy", "2 days", "Spiky Plant", "asdlfkjaslkdfjasd", R.drawable.image_1))
        plantList.add(Plant("Johnny", "1 days", "Pretty Plant", "asdlfkjaslkdfjasd", R.drawable.image_2))
        plantList.add(Plant("Carla", "2 mos", "Ugly Plant", "asdlfkjaslkdfjasd", R.drawable.image_3))
        plantList.add(Plant("Timothy", "2 days", "Spiky Plant", "asdlfkjaslkdfjasd", R.drawable.image_1))
        plantList.add(Plant("Johnny", "1 days", "Pretty Plant", "asdlfkjaslkdfjasd", R.drawable.image_2))
        plantList.add(Plant("Carla", "2 mos", "Ugly Plant", "asdlfkjaslkdfjasd", R.drawable.image_3)) */
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

        // Initialize addPlantButton & onClick handler
        addPlantButton = view.findViewById(R.id.plant_add_button)
        addPlantButton.setOnClickListener(addPlantHandler)

        return view
    }

    private val addPlantHandler= View.OnClickListener { view ->
        when (view.getId()) {
            R.id.plant_add_button -> {
                val intent = Intent(context, AddPlantActivity::class.java)
//                intent.putExtra("plant", it)
                startActivity(intent)
            }
        }
    }

}