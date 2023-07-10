package com.example.digileaf

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.digileaf.database.PlantViewModel
import com.example.digileaf.database.PlantViewModelFactory

class Achievements: Fragment() {
    private val plantViewModel: PlantViewModel by viewModels {
        PlantViewModelFactory((activity?.application as DigileafApplication).plantRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_achievement, container, false)

        plantViewModel.getWateredCount().observe(viewLifecycleOwner, Observer {
            if (it > 0) {
                view.findViewById<ImageView>(R.id.water_badge_1).foreground = null
            }
            if (it > 2) {
                view.findViewById<ImageView>(R.id.water_badge_1).foreground = null
            }
        })

        return view
    }
}