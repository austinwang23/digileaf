package com.example.digileaf

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.digileaf.adapter.PlantAdapter
import com.example.digileaf.database.PlantViewModel
import com.example.digileaf.database.PlantViewModelFactory
import com.example.digileaf.entities.Journal
import com.example.digileaf.entities.Plant
import com.example.digileaf.helpers.WeatherBackgroundMappings
import com.example.digileaf.helpers.WeatherCodeMappings
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


private const val BASE_URL = "http://api.weatherapi.com/v1/"

class Home : Fragment() {
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val weatherApiService = retrofit.create(WeatherApiService::class.java)

    private lateinit var recyclerView: RecyclerView
    private lateinit var addPlantButton: CardView
    private lateinit var addSelectLocationActivityLauncher : ActivityResultLauncher<Intent>
    private lateinit var addPlantActivityLauncher: ActivityResultLauncher<Intent>
    private val plantViewModel: PlantViewModel by viewModels {
        PlantViewModelFactory((activity?.application as DigileafApplication).plantRepository)
    }

    private var maxTempC: Float = 0f
    private var weatherIcon: String? = null
    private var weatherDescription: String? = null
    private var weatherCode: Int = 0
    private var weatherLocation: String? = null
    private var weatherWindSpeed: Float = 0f
    private var weatherAverageTemp: Float = 0f
    private var weatherTotalPrecip: Float = 0f
    private var weatherSunrise: String? = null
    private var weatherSunset: String? = null
    private var weatherMoonrise: String? = null

    private lateinit var weatherCardView: CardView
    private lateinit var weatherTemperatureTextView: TextView
    private lateinit var weatherLocationTextView: TextView
    private lateinit var weatherIconImageView: ImageView
    private lateinit var weatherDescriptionTextView: TextView
    private lateinit var weatherTipTextView: TextView
    private lateinit var weatherBackgroundImageView: ImageView
    private lateinit var plantQuiz: CardView
    private lateinit var lightMeter: CardView // TO IMPLEMENT

    private val LOCATION_PERMISSION_REQUEST_CODE = 1

    private lateinit var sensorManager: SensorManager
    private lateinit var lightSensor: Sensor

    private fun getWeatherData(latLong: String) {
        val call = weatherApiService.getWeatherData(
            apiKey = "f5a80a80e75940b0bc1192359232406",
            location = latLong,
            days = 1,
            includeAqi = "no",
            includeAlerts = "no"
        )

        call.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>
            ) {
                if (isAdded) {
                    if (response.isSuccessful) {
                        val weatherData = response.body()
                        val forecastDay = weatherData?.forecast?.forecastday?.get(0)

                        // Store the weather data in member variables
                        maxTempC = forecastDay?.day?.maxtemp_c ?: 0f
                        weatherIcon = forecastDay?.day?.condition?.icon
                        weatherDescription = forecastDay?.day?.condition?.text
                        weatherCode = forecastDay?.day?.condition?.code ?: 0
                        weatherLocation = weatherData?.location?.name
                        weatherWindSpeed = forecastDay?.day?.maxwind_kph ?: 0f
                        weatherAverageTemp = forecastDay?.day?.avgtemp_c ?: 0f
                        weatherTotalPrecip = forecastDay?.day?.totalprecip_mm ?: 0f
                        weatherSunrise = forecastDay?.astro?.sunrise
                        weatherSunset = forecastDay?.astro?.sunset
                        weatherMoonrise = forecastDay?.astro?.moonrise

                        weatherTemperatureTextView.text = "${maxTempC?.toString()}°C"
                        weatherLocationTextView.text = weatherLocation
                        Glide.with(requireContext())
                            .load("https:${weatherIcon}")
                            .into(weatherIconImageView)
                        weatherDescriptionTextView.text = weatherDescription
                        weatherTipTextView.text =
                            WeatherCodeMappings.getOrDefault(weatherCode as Int, "")
                        weatherBackgroundImageView.setImageResource(
                            WeatherBackgroundMappings.getOrDefault(
                                weatherCode as Int,
                                R.drawable.bg_sunny
                            )
                        )
                        registerWeatherClickListener()
                    } else {
                        Log.e("API", "Error: ${response.code()}")
                    }
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                if (isAdded) {
                    Log.e("API", "Failure: ${t.message}")
                }
            }
        })
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        val priority = Priority.PRIORITY_BALANCED_POWER_ACCURACY
        val cancellationTokenSource = CancellationTokenSource()
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        fusedLocationClient.getCurrentLocation(priority, cancellationTokenSource.token)
            .addOnSuccessListener { location ->
                val latLong = location.latitude.toString() + "," + location.longitude.toString()
                getWeatherData(latLong)
            }
            .addOnFailureListener { exception ->
                Log.d("Location", "Oops location failed with exception: $exception")
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addPlantActivityLauncher =
            registerForActivityResult(StartActivityForResult()) { result: ActivityResult ->
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

        addSelectLocationActivityLauncher =
            registerForActivityResult(StartActivityForResult()) { result: ActivityResult ->
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

        weatherCardView = view.findViewById(R.id.weather_card)

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request the camera permission if it is not granted
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            getLocation()
        }

        val plantAdapter = PlantAdapter()

        // Initialize the RecyclerView
        recyclerView = view.findViewById(R.id.plant_RV)

        // Set up the RecyclerView with the adapter and layout manager
        recyclerView.adapter = plantAdapter
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

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
        addPlantButton.setOnClickListener {
            launchAddPlantActivity()
        }

        plantQuiz = view.findViewById(R.id.plant_quiz_card)
        plantQuiz.setOnClickListener {
            val intent = Intent(context, PlantQuizActivity::class.java)
            startActivity(intent)
        }

        lightMeter = view.findViewById(R.id.light_meter_card)
        sensorManager = requireActivity().getSystemService(AppCompatActivity.SENSOR_SERVICE) as SensorManager
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

        if (lightSensor == null) {
            lightMeter.visibility = View.GONE
        } else {
            lightMeter.setOnClickListener {
                val intent = Intent(context, LightMeterActivity::class.java)
                startActivity(intent)
            }
        }
        return view
    }

    private fun registerWeatherClickListener() {
        weatherCardView.setOnClickListener {

            Log.e("weather card", "clicked on weather card")
            val intent = Intent(context, WeatherPageActivity::class.java)
            intent.putExtra("maxTempC", maxTempC)
            intent.putExtra("weatherIcon", weatherIcon)
            intent.putExtra("weatherDescription", weatherDescription)
            intent.putExtra("weatherCode", weatherCode)
            intent.putExtra("weatherLocation", weatherLocation)
            intent.putExtra("weatherWindSpeed", weatherWindSpeed)
            intent.putExtra("weatherAverageTemp", weatherAverageTemp)
            intent.putExtra("weatherTotalPrecip", weatherTotalPrecip)
            intent.putExtra("weatherSunrise", weatherSunrise)
            intent.putExtra("weatherSunset", weatherSunset)
            intent.putExtra("weatherMoonrise", weatherMoonrise)
            intent.putExtra("weatherTip", weatherTipTextView.text.toString())
            startActivity(intent)

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        weatherTemperatureTextView = view.findViewById(R.id.weather_temperature)
        weatherLocationTextView = view.findViewById(R.id.weather_location)
        weatherLocationTextView.setOnClickListener {
            launchAddPlantActivity()
        }
        weatherIconImageView = view.findViewById(R.id.weather_icon)
        weatherDescriptionTextView = view.findViewById(R.id.weather_description)
        weatherTipTextView = view.findViewById(R.id.weather_tip)
        weatherBackgroundImageView = view.findViewById(R.id.weather_background)

    }

    private fun launchAddPlantActivity() {
        val intent = Intent(context, AddPlantActivity::class.java)
        addPlantActivityLauncher.launch(intent)
    }

    private fun launchSelectLocationActivity() {
        val intent = Intent(context, SelectLocationActivity::class.java)
        addSelectLocationActivityLauncher.launch(intent)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && (grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                getLocation()
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }
}