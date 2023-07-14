package com.example.digileaf

data class WeatherResponse(
    val current: CurrentWeather,
    val location: Location,
    val forecast: Forecast
)

data class CurrentWeather(
    val temp_c: Float
)

data class Location(
    val name: String
)

data class Forecast(
    val forecastday: List<ForecastDay>
)

data class ForecastDay(
    val day: ForecastDayInfo,
    val astro : Astro
)

data class ForecastDayInfo(
    val maxtemp_c: Float,
    val maxwind_kph: Float,
    val avgtemp_c: Float,
    val condition : Condition,
    val daily_chance_of_rain : Float,
    val totalprecip_mm: Float,
)

data class Astro(
    val sunrise : String,
    val moonrise : String,
    val sunset : String
)

data class Condition(
    val icon: String,
    val text: String,
    val code: Int
)
