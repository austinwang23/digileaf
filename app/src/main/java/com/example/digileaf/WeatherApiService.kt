package com.example.digileaf

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET("forecast.json")
    fun getWeatherData(
        @Query("key") apiKey: String,
        @Query("q") location: String,
        @Query("days") days: Int,
        @Query("aqi") includeAqi: String,
        @Query("alerts") includeAlerts: String
    ): Call<WeatherResponse>
}