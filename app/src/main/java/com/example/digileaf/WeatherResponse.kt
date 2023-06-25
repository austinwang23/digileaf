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
    val day: ForecastDayInfo
)

data class ForecastDayInfo(
    val maxtemp_c: Float,
    val condition : Condition
)

data class Condition(
    val icon: String,
    val text: String,
    val code: Int
)
