package com.example.digileaf

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.example.digileaf.databinding.FragmentPlantRecomendationBinding
import com.google.android.material.button.MaterialButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.random.Random

data class PlantRecommendationParams (
    val isIndoors: Boolean,
    val waterFrequency: Float,
    val sunlight: Float,
    val isPoisonous: Boolean
)

interface PlantRecommendationDialogListener {
    fun seeNearbyVendors()
}

private const val BASE_URL = "https://perenual.com/api/"

class PlantRecommendation(params: PlantRecommendationParams, listener: PlantRecommendationDialogListener): DialogFragment() {
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val plantApiService = retrofit.create(PlantApiService::class.java)
    private lateinit var binding: FragmentPlantRecomendationBinding
    private lateinit var progressView: LinearLayout
    private lateinit var resultView: LinearLayout
    private lateinit var plantImage: ImageView
    private lateinit var plantSpecies: TextView
    private lateinit var plants: List<Plant>
    private lateinit var seeAnotherPlant: MaterialButton
    private var plantApiParams: PlantRecommendationParams
    private var callbackListener: PlantRecommendationDialogListener

    init {
        this.plantApiParams = params
        this.callbackListener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlantRecomendationBinding.inflate(inflater, container, false)
        val view = binding.root
        isCancelable = false
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressView = binding.plantRecommendationLoading
        resultView = binding.plantRecommendationResult
        plantImage = binding.plantIcon
        plantSpecies = binding.plantQuizSpecies
        seeAnotherPlant = binding.showAnotherPlant
        seeAnotherPlant.setOnClickListener{
            showPlantResults()
        }

        binding.plantRecommendationDismiss.setOnClickListener{
            dismiss()
        }

        binding.plantQuizNearbyVendorsButton.setOnClickListener{
            callbackListener.seeNearbyVendors()
            dismiss()
        }

        getPlantRecommendations()
    }

    private fun getPlantRecommendations() {

        var waterFrequency: String = when (this.plantApiParams.waterFrequency) {
            0f -> "minimum"
            1f -> "average"
            2f -> "frequent"
            else -> "average"
        }

        var sunlight: String = when (this.plantApiParams.sunlight) {
            0f -> "part_shade"
            1f -> "part_shade"
            2f -> "full_sun"
            else -> "part_shade"
        }

        val call = plantApiService.getPlantData(
            apiKey = "sk-EGj464a4a692b203a1476",
            page = 1,
            indoor = if (this.plantApiParams.isIndoors) 1 else 0,
            waterFrequency = waterFrequency,
            sunlight = sunlight,
            poisonous = if (this.plantApiParams.isPoisonous) 1 else null
        )

        Log.e("PLANT API", "making api call")

        call.enqueue(object : Callback<PlantResponse> {
            override fun onResponse(
                call: Call<PlantResponse>,
                response: Response<PlantResponse>
            ) {
                if (response.isSuccessful) {
                    val plantData = response.body()
                    if (plantData != null) {
                        plants = plantData.data
                    }

                    showPlantResults()
                } else {
                    // Add callback to display failure toast
                    Log.e("PLANT API", "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<PlantResponse>, t: Throwable) {
                // Add callback to display failure toast
                Log.e("PLANT API", "Failure: ${t.message}")
            }
        })
    }

    private fun showPlantResults() {
        if (plants.isEmpty()) {
            // TODO - add some handling here (no plant matches ur requirements??)
            dismiss()
            return
        }
        if (plants.size == 1) {
            seeAnotherPlant.visibility = View.GONE
        }
        Log.e("PLANT", "showing another plant")
        val randomIndex = Random.nextInt(plants.size);
        val plant = plants[randomIndex]
        if(plant.default_image == null || plant.default_image.original_url.contains("upgrade_access")) {
            plantImage.setImageResource(R.drawable.`default_plant`)
        } else {
            Glide.with(requireContext())
                .load(plant.default_image.thumbnail)
                .into(plantImage)
        }
        plantSpecies.text = plant.common_name
        progressView.visibility = View.GONE
        resultView.visibility = View.VISIBLE
    }
}