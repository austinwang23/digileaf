package com.example.digileaf

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.digileaf.helpers.WeatherBackgroundMappings

class WeatherPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_page)

        val maxTempC = intent.getFloatExtra("maxTempC", 0f)
        val weatherIcon = intent.getStringExtra("weatherIcon")
        val weatherDescription = intent.getStringExtra("weatherDescription")
        val weatherCode = intent.getIntExtra("weatherCode", 0)
        val weatherLocation = intent.getStringExtra("weatherLocation")
        val weatherWindSpeed = intent.getFloatExtra("weatherWindSpeed", 0f)
        val weatherAverageTemp = intent.getFloatExtra("weatherAverageTemp", 0f)
        val weatherTotalPrecip = intent.getFloatExtra("weatherTotalPrecip", 0f)
        val weatherSunrise = intent.getStringExtra("weatherSunrise")
        val weatherSunset = intent.getStringExtra("weatherSunset")
        val weatherMoonrise = intent.getStringExtra("weatherMoonrise")
        val weatherTip = intent.getStringExtra("weatherTip")

        val locationTextView : TextView = findViewById(R.id.location_name)
        val weatherIconImageView : ImageView = findViewById(R.id.weather_icon)
        val weatherTemperatrueTextView : TextView = findViewById(R.id.weather_temperature)
        val weatherDescriptionTextView : TextView = findViewById(R.id.weather_description)
        val weatherTipTextView : TextView = findViewById(R.id.weather_tip)
        val weatherWindSpeedTextView : TextView = findViewById(R.id.wind_speed)
        val weatherTotalPrecipTextView : TextView = findViewById(R.id.total_precip)
        val weatherAvgTempTextView : TextView = findViewById(R.id.avg_temp)
        val weatherSunriseView : TextView = findViewById(R.id.sunrise)
        val weatherSunsetTextView : TextView = findViewById(R.id.sunset)
        val weatherMoonriseTextView : TextView = findViewById(R.id.moonrise)
        val weatherBackgroundImageView : ImageView = findViewById(R.id.weather_card_background)



        locationTextView.text = weatherLocation
        Glide.with(this)
            .load("https:${weatherIcon}")
            .into(weatherIconImageView)
        weatherTemperatrueTextView.text = "${maxTempC?.toString()}°C"
        weatherDescriptionTextView.text = weatherDescription
        weatherTipTextView.text = weatherTip
        weatherWindSpeedTextView.text = weatherWindSpeed.toString() + "km/h"
        weatherTotalPrecipTextView.text = weatherTotalPrecip.toString() + "mm"
        weatherAvgTempTextView.text = weatherAverageTemp.toString()
        weatherSunriseView.text = weatherSunrise
        weatherSunsetTextView.text = weatherSunset
        weatherMoonriseTextView.text = weatherMoonrise
        weatherBackgroundImageView.setImageResource(
            WeatherBackgroundMappings.getOrDefault(
                weatherCode as Int,
                R.drawable.bg_sunny
            )
        )

        val backButton : ImageButton = findViewById(R.id.back_button)
        backButton.setOnClickListener{
            finish()
        }
    }
}