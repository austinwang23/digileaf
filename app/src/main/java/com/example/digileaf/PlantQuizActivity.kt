package com.example.digileaf

import android.os.Bundle
import android.util.TypedValue
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.slider.Slider

class PlantQuizActivity: AppCompatActivity() {
    private lateinit var indoorTextButton: TextView
    private lateinit var outdoorTextButton: TextView
    private lateinit var waterHelperText : TextView
    private lateinit var waterSlider: Slider
    private lateinit var sunSlider: Slider
    private lateinit var sunlightHelperText: TextView
    private lateinit var childrenCheckbox: CheckBox
    private lateinit var petsCheckBox: CheckBox
    private lateinit var submitQuiz: Button
    private var isIndoors = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plant_quiz)

        indoorTextButton = findViewById(R.id.indoor_plant_text_button)
        outdoorTextButton = findViewById(R.id.outdoor_plant_text_button)
        waterHelperText = findViewById(R.id.water_helper_text)
        waterSlider = findViewById(R.id.water_slider)
        sunlightHelperText = findViewById(R.id.sun_helper_text)
        sunSlider = findViewById(R.id.sun_slider)
        childrenCheckbox = findViewById(R.id.checkbox_children)
        petsCheckBox = findViewById(R.id.checkbox_pet)
        submitQuiz = findViewById(R.id.submit_plant_quiz)

        indoorTextButton.setOnClickListener {
            updatePlantIndoorOutdoorStatus(true)
        }

        outdoorTextButton.setOnClickListener {
            updatePlantIndoorOutdoorStatus(false)
        }

        waterSlider.addOnChangeListener { _, value, _ ->
            when (value) {
                0f -> waterHelperText.text = "little"
                1f -> waterHelperText.text = "some"
                2f -> waterHelperText.text = "lots of"
                else -> waterHelperText.text = "little"
            }
        }

        sunSlider.addOnChangeListener { _, value, _ ->
            when (value) {
                0f -> sunlightHelperText.text = "little"
                1f -> sunlightHelperText.text = "some"
                2f -> sunlightHelperText.text = "lots of"
                else -> waterHelperText.text = "little"
            }
        }

        submitQuiz.setOnClickListener{
            submitPlantQuiz()
        }

        val backButton : ImageButton = findViewById(R.id.back_button)
        backButton.setOnClickListener{
            finish()
        }
    }

    private fun updatePlantIndoorOutdoorStatus(isIndoor: Boolean) {
        if (isIndoor) {
            isIndoors = true
            indoorTextButton.setTextColor(ContextCompat.getColor(this, R.color.teal))
            indoorTextButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f)

            outdoorTextButton.setTextColor(ContextCompat.getColor(this, R.color.gray))
            outdoorTextButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
        } else {
            isIndoors = false
            outdoorTextButton.setTextColor(ContextCompat.getColor(this, R.color.teal))
            outdoorTextButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f)

            indoorTextButton.setTextColor(ContextCompat.getColor(this, R.color.gray))
            indoorTextButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
        }
    }

    private fun submitPlantQuiz() {
        val plantRecommendationParams = PlantRecommendationParams(isIndoors, waterSlider.value, sunSlider.value, childrenCheckbox.isChecked, petsCheckBox.isChecked)
        val plantRecommendationDialog = PlantRecommendation(plantRecommendationParams)
        plantRecommendationDialog.show(this.supportFragmentManager, "plantRecommendationDialog")
    }
}