package com.example.digileaf

import android.content.Intent
import android.database.sqlite.SQLiteDatabase.deleteDatabase
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.digileaf.database.AppDatabase
import com.example.digileaf.model.Plant

class Home : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var addPlantButton: Button
    private lateinit var plantList: ArrayList<Plant>
    private lateinit var plantAdapter: PlantAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // partially using seed db
        val appDatabase = AppDatabase.getInstance(requireContext())

        val plantDao = appDatabase.plantDao()

        val plants = plantDao.getAll()
        plantList = ArrayList()

        for (plant in plants) {
            plantList.add(Plant(plant.name, "2 days", plant.species, plant.description, R.drawable.image_1))
        }

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

        plantAdapter = PlantAdapter(plantList)

        plantAdapter.onItemClick = {
            val intent = Intent(context, ItemDetailsActivity::class.java)
            intent.putExtra("plant", it)
            startActivity(intent)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Initialize the RecyclerView
        recyclerView = view.findViewById(R.id.plant_RV)

        // Set up the RecyclerView with the adapter and layout manager
        recyclerView.adapter = plantAdapter
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

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