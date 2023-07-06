package com.example.digileaf

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PlantApiService {

    @GET("species-list")
    fun getPlantData(
        @Query("key") apiKey: String,
        @Query("page") page: Int = 1,
        @Query("indoor") indoor: Int,
        @Query("watering") waterFrequency: String,
        @Query("sunlight") sunlight: String,
        @Query("poisonous") poisonous: Int? = null,
    ): Call<PlantResponse>
}

data class PlantResponse(
    val data: List<Plant>
)

data class Plant(
    val common_name: String,
    val default_image: PlantImage
)

data class PlantImage(
    val original_url: String,
    val small_url: String,
    val thumbnail: String
)