package com.example.digileaf

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.digileaf.adapter.AchievementAdapter
import com.example.digileaf.database.JournalViewModel
import com.example.digileaf.database.JournalViewModelFactory
import com.example.digileaf.database.PlantViewModel
import com.example.digileaf.database.PlantViewModelFactory
import com.example.digileaf.entities.Achievement

class Achievements: Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AchievementAdapter
    private lateinit var achievements: MutableList<Achievement>
    private val plantViewModel: PlantViewModel by viewModels {
        PlantViewModelFactory((activity?.application as DigileafApplication).plantRepository)
    }

    private val journalViewModel: JournalViewModel by viewModels {
        JournalViewModelFactory((activity?.application as DigileafApplication).journalRepository)
    }


    private var plantCount: Int = 0
    private var journalCount: Int = 0
    private var wateredCount: Int = 0
    private var fertilizedCount: Int = 0
    private var groomedCount: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_achievement, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.achievements_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Initialize achievements list
        achievements = mutableListOf()

        adapter = AchievementAdapter(achievements)
        recyclerView.adapter = adapter

        val achievementsContainer: LinearLayout = view.findViewById(R.id.achievements_container)


        val achievementsTotalTip: TextView = view.findViewById(R.id.achievements_total_tip)
        var unlockedAchievements = 0
        val totalAchievements = 15

        // Call a separate function to update the achievementsToolTip text
        fun updateAchievementsNumber() {
            val achievementsText = "You've unlocked $unlockedAchievements out of $totalAchievements achievements!"
            achievementsTotalTip.text = achievementsText

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

        plantViewModel.getPlantCount().observe(viewLifecycleOwner) { count ->
            plantCount = count
            updateAchievements()
            if (count >= 3) {
                unlockedAchievements++
            }
            if (count >= 5) {
                unlockedAchievements++
            }
            if (count >= 10) {
                unlockedAchievements++
            }
            updateAchievementsNumber()
        }

        journalViewModel.getJournalCount().observe(viewLifecycleOwner) { count ->
            journalCount = count
            updateAchievements()
            if (count >= 10) {
                unlockedAchievements++
            }
            if (count >= 50) {
                unlockedAchievements++
            }
            if (count >= 100) {
                unlockedAchievements++
            }

            updateAchievementsNumber()
        }

        plantViewModel.getWateredCount().observe(viewLifecycleOwner) { count ->
            wateredCount = count
            updateAchievements()
            if (count >= 1) {
                unlockedAchievements++
            }
            if (count >= 5) {
                unlockedAchievements++
            }
            if (count >= 10) {
                unlockedAchievements++
            }

            updateAchievementsNumber()
        }

        plantViewModel.getFertilizedCount().observe(viewLifecycleOwner) { count ->
            fertilizedCount = count
            updateAchievements()
            if (count >= 1) {
                unlockedAchievements++
            }
            if (count >= 3) {
                unlockedAchievements++
            }
            if (count >= 5) {
                unlockedAchievements++
            }

            updateAchievementsNumber()
        }

        plantViewModel.getGroomedCount().observe(viewLifecycleOwner) { count ->
            groomedCount = count
            updateAchievements()
            if (count >= 1) {
                unlockedAchievements++
            }
            if (count >= 5) {
                unlockedAchievements++
            }
            if (count >= 10) {
                unlockedAchievements++
            }

            updateAchievementsNumber()
        }
    }

    private fun getAchievements(): List<Achievement> {
        val achievementsMap = createMap()
        val achievements = mutableListOf<Achievement>()

        fun unlockAchievement(key: String) {
            val achievement = achievementsMap[key] ?: return
            achievements.add(Achievement(true, achievement.image, achievement.title, achievement.description))
            achievementsMap.remove(key)
        }

        // Add unlocked achievements to the list
        if (plantCount >= 3) {
            unlockAchievement("plant1")
        }
        if (plantCount >= 5) {
            unlockAchievement("plant2")
        }
        if (plantCount >= 10) {
            unlockAchievement("plant3")
        }

        if (journalCount >= 10) {
            unlockAchievement("journal1")
        }
        if (journalCount >= 50) {
            unlockAchievement("journal2")
        }
        if (journalCount >= 100) {
            unlockAchievement("journal3")
        }

        if (wateredCount >= 1) {
            unlockAchievement("water1")
        }
        if (wateredCount >= 5) {
            unlockAchievement("water2")
        }
        if (wateredCount >= 10) {
            unlockAchievement("water3")
        }

        if (fertilizedCount >= 1) {
            unlockAchievement("fertilize1")
        }
        if (fertilizedCount >= 3) {
            unlockAchievement("fertilize2")
        }
        if (fertilizedCount >= 5) {
            unlockAchievement("fertilize3")
        }

        if (groomedCount >= 1) {
            unlockAchievement("groom1")
        }
        if (groomedCount >= 5) {
            unlockAchievement("groom2")
        }
        if (groomedCount >= 10) {
            unlockAchievement("groom3")
        }

        for (entry in achievementsMap.entries) {
            val achievement = entry.value
            achievements.add(achievement)
        }

        return achievements
    }
    private fun createMap(): MutableMap<String, Achievement> {
        val myMap: MutableMap<String, Achievement> = LinkedHashMap()
        myMap["plant1"] = Achievement(false, R.drawable.plant_badge_1, "Plant Level 1", "Add 3 plant")
        myMap["plant2"] = Achievement(false, R.drawable.plant_badge_2, "Plant Level 2", "Add 5 plants")
        myMap["plant3"] = Achievement(false, R.drawable.plant_badge_3, "Plant Level 3", "Add 10 plants")

        myMap["journal1"] = Achievement(false, R.drawable.journal_badge_1, "Journal Level 1", "Add 10 journal")
        myMap["journal2"] = Achievement(false, R.drawable.journal_badge_2, "Journal Level 2", "Add 50 journals")
        myMap["journal3"] = Achievement(false, R.drawable.journal_badge_3, "Journal Level 3", "Add 100 journals")

        myMap["water1"] = Achievement(false, R.drawable.water_badge_1, "Water Level 1", "Water 1 plant")
        myMap["water2"] = Achievement(false, R.drawable.water_badge_2, "Water Level 2", "Water 5 plants")
        myMap["water3"] = Achievement(false, R.drawable.water_badge_3, "Water Level 3", "Water 10 plants")

        myMap["fertilize1"] = Achievement(false, R.drawable.fertilize_badge_1, "Fertilize Level 1", "Fertilize 1 plant")
        myMap["fertilize2"] = Achievement(false, R.drawable.fertilize_badge_2, "Fertilize Level 2", "Fertilize 3 plants")
        myMap["fertilize3"] = Achievement(false, R.drawable.fertilize_badge_3, "Fertilize Level 3", "Fertilize 5 plants")

        myMap["groom1"] = Achievement(false, R.drawable.groom_badge_1, "Groom Level 1", "Groom 1 plant")
        myMap["groom2"] = Achievement(false, R.drawable.groom_badge_2, "Groom Level 2", "Groom 5 plants")
        myMap["groom3"] = Achievement(false, R.drawable.groom_badge_3, "Groom Level 3", "Groom 10 plants")

        return myMap
    }

    private fun updateAchievements() {
        achievements.clear()
        achievements.addAll(getAchievements())
        // Todo: It will always be more efficient to use more specific change events if you can. Rely on notifyDataSetChanged as a last resort.
        adapter.notifyDataSetChanged()
    }
}
