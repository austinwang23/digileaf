package com.example.digileaf

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.digileaf.model.Plant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity


class Home : Fragment(), PlantAdapter.OnItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var plantList: ArrayList<Plant>
    private lateinit var plantAdapter: PlantAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView()
        //Initialize plants
        plantList = ArrayList()
        plantList.add(Plant("Timothy", "2 days old", "Timothy is questionable", R.drawable.image_1))
        plantList.add(Plant("Johnny", "1 days old", "Johnny is <3", R.drawable.image_2))
        plantList.add(Plant("Carla", "4 days old", "Carla is *****", R.drawable.image_3))
        plantList.add(Plant("Carla", "4 days old", "Carla is *****", R.drawable.image_3))
        plantList.add(Plant("Carla", "4 days old", "Carla is *****", R.drawable.image_3))
        plantList.add(Plant("Carla", "4 days old", "Carla is *****", R.drawable.image_3))
        plantList.add(Plant("Carla", "4 days old", "Carla is *****", R.drawable.image_3))
        plantList.add(Plant("Carla", "4 days old", "Carla is *****", R.drawable.image_3))
        plantList.add(Plant("Carla", "4 days old", "Carla is *****", R.drawable.image_3))
        plantList.add(Plant("Carla", "4 days old", "Carla is *****", R.drawable.image_3))

        plantAdapter = PlantAdapter(plantList, this)


        Button newButton =
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Initialize the RecyclerView
        recyclerView = view.findViewById(R.id.home_RV)

        // Set up the RecyclerView with the adapter and layout manager
        recyclerView.adapter = plantAdapter
        recyclerView.layoutManager = GridLayoutManager(context, 2)

        return view
    }

    override fun onItemClick(position: Int) {
        Toast.makeText(activity, "Item $position clicked", Toast.LENGTH_SHORT).show()
    }
}