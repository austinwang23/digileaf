package com.example.digileaf

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
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

        val achievementsContainer: LinearLayout = view.findViewById(R.id.achievements_container)

        val achievementsTotalTip: TextView = view.findViewById(R.id.achievements_total_tip)
        var unlockedAchievements = 0
        var totalAchievements = 15

        // Call a separate function to update the achievementsToolTip text
        fun updateAchievementsNumber() {
            val achievementsText = "You've unlocked $unlockedAchievements out of $totalAchievements achievements!"
            achievementsTotalTip.text = achievementsText

            // Set num trophy icons displayed
            achievementsContainer.removeAllViews()
           for (i in 1..unlockedAchievements) {
               val achievementImageView = ImageView(requireContext())
               achievementImageView.layoutParams = LinearLayout.LayoutParams(
                   ViewGroup.LayoutParams.WRAP_CONTENT,
                   ViewGroup.LayoutParams.WRAP_CONTENT
               )
               achievementImageView.setImageResource(R.drawable.ic_achievement)
               achievementImageView.setColorFilter(ContextCompat.getColor(requireContext(), R.color.gold))
               achievementsContainer.addView(achievementImageView)
           }
           for (i in 1..totalAchievements - unlockedAchievements) {
               val achievementImageView = ImageView(requireContext())
               achievementImageView.layoutParams = LinearLayout.LayoutParams(
                   ViewGroup.LayoutParams.WRAP_CONTENT,
                   ViewGroup.LayoutParams.WRAP_CONTENT
               )
               achievementImageView.setImageResource(R.drawable.ic_achievement)
               achievementImageView.setColorFilter(ContextCompat.getColor(requireContext(), R.color.gray))
               achievementsContainer.addView(achievementImageView)
           }
        }

        plantViewModel.getPlantCount().observe(viewLifecycleOwner, Observer {
            if (it >= 3) {
                view.findViewById<CardView>(R.id.plant_1_card).foreground = null
                unlockedAchievements++
            }
            if (it >= 5) {
                view.findViewById<CardView>(R.id.plant_2_card).foreground = null
                unlockedAchievements++
            }
            if (it >= 10) {
                view.findViewById<CardView>(R.id.plant_3_card).foreground = null
                unlockedAchievements++
            }

            updateAchievementsNumber()
        })

        plantViewModel.getJournalCount().observe(viewLifecycleOwner, Observer {
            if (it >= 10) {
                view.findViewById<CardView>(R.id.journal_1_card).foreground = null
                unlockedAchievements++
            }
            if (it >= 50) {
                view.findViewById<CardView>(R.id.journal_2_card).foreground = null
                unlockedAchievements++
            }
            if (it >= 100) {
                view.findViewById<CardView>(R.id.journal_3_card).foreground = null
                unlockedAchievements++
            }

            updateAchievementsNumber()
        })

        plantViewModel.getWateredCount().observe(viewLifecycleOwner, Observer {
            if (it >= 1) {
                view.findViewById<CardView>(R.id.water_1_card).foreground = null
                unlockedAchievements++
            }
            if (it >= 5) {
                view.findViewById<CardView>(R.id.water_2_card).foreground = null
                unlockedAchievements++
            }
            if (it >= 10) {
                view.findViewById<CardView>(R.id.water_3_card).foreground = null
                unlockedAchievements++
            }

            updateAchievementsNumber()
        })

        plantViewModel.getFertilizedCount().observe(viewLifecycleOwner, Observer {
            if (it >= 1) {
                view.findViewById<CardView>(R.id.fertilize_1_card).foreground = null
                unlockedAchievements++
            }
            if (it >= 3) {
                view.findViewById<CardView>(R.id.fertilize_2_card).foreground = null
                unlockedAchievements++
            }
            if (it >= 5) {
                view.findViewById<CardView>(R.id.fertilize_3_card).foreground = null
                unlockedAchievements++
            }

            updateAchievementsNumber()
        })

        plantViewModel.getGroomedCount().observe(viewLifecycleOwner, Observer {
            if (it >= 1) {
                view.findViewById<CardView>(R.id.groom_1_card).foreground = null
                unlockedAchievements++
            }
            if (it >= 5) {
                view.findViewById<CardView>(R.id.groom_2_card).foreground = null
                unlockedAchievements++
            }
            if (it >= 10) {
                view.findViewById<CardView>(R.id.groom_3_card).foreground = null
                unlockedAchievements++
            }

            updateAchievementsNumber()
        })

        return view
    }
}
