package com.example.digileaf

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
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
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.bumptech.glide.Glide


private const val BASE_URL = "http://api.weatherapi.com/v1/"
class Home : Fragment() {
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val weatherApiService = retrofit.create(WeatherApiService::class.java)

    private lateinit var recyclerView: RecyclerView
    private lateinit var addPlantButton: Button
    private lateinit var addPlantActivityLauncher : ActivityResultLauncher<Intent>
    private val plantViewModel: PlantViewModel by viewModels {
        PlantViewModelFactory((activity?.application as DigileafApplication).plantRepository)
    }
    private lateinit var weatherTemperatureTextView: TextView
    private lateinit var weatherLocationTextView: TextView
    private lateinit var weatherIconImageView: ImageView
    private lateinit var weatherDescriptionTextView: TextView

    private fun getWeatherData() {
        val call = weatherApiService.getWeatherData(
            apiKey = "f5a80a80e75940b0bc1192359232406",
            location = "43.464256,-80.520409",
            days = 1,
            includeAqi = "no",
            includeAlerts = "no"
        )

        call.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>
            ) {
                if (response.isSuccessful) {
                    val weatherData = response.body()
                    val forecastDay = weatherData?.forecast?.forecastday?.get(0)
                    val maxTempC = forecastDay?.day?.maxtemp_c
                    val weatherIcon = forecastDay?.day?.condition?.icon
                    val weatherDescription = forecastDay?.day?.condition?.text

                    weatherTemperatureTextView.text = "${maxTempC?.toString()}°C"
                    weatherLocationTextView.text = weatherData?.location?.name
                    Glide.with(requireContext())
                        .load("https:${weatherIcon}")
                        .into(weatherIconImageView)
                    weatherDescriptionTextView.text = weatherDescription
//                    Log.d("Weather Data", weatherData.toString())
                } else {
                    Log.e("API", "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Log.e("API", "Failure: ${t.message}")
            }
        })
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

        getWeatherData()

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        weatherTemperatureTextView = view.findViewById(R.id.weather_temperature)
        weatherLocationTextView = view.findViewById(R.id.weather_location)
        weatherIconImageView = view.findViewById(R.id.weather_icon)
        weatherDescriptionTextView = view.findViewById(R.id.weather_description)
    }

    private fun launchAddPlantActivity() {
        val intent = Intent(context, AddPlantActivity::class.java)
        addPlantActivityLauncher.launch(intent)
    }

}