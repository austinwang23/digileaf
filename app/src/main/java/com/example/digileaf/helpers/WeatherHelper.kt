package com.example.digileaf.helpers

val WeatherSunMessage = "Sunny today! Any plants that need sunshine can be brought outside."
val WeatherCloudMessage = "Chance of clouds! Make sure your plants are getting enough light."
val WeatherRainMessage = "Chance of rain! Make sure to check up on your outdoor plants."
val WeatherSnowMessage = "Chance of snow! Make sure to check up on your outdoor plants."


val WeatherCodeMappings = mapOf(
    1000 to WeatherSunMessage,
    1003 to WeatherCloudMessage,
    1006 to WeatherCloudMessage,
    1009 to WeatherCloudMessage,
    1063 to WeatherRainMessage,
    1087 to WeatherRainMessage,
    1114 to WeatherSnowMessage,
    1117 to WeatherSnowMessage,
    1150 to WeatherRainMessage,
    1153 to WeatherRainMessage,
    1180 to WeatherRainMessage,
    1183 to WeatherRainMessage,
    1186 to WeatherRainMessage,
    1189 to WeatherRainMessage,
    1192 to WeatherRainMessage,
    1195 to WeatherRainMessage,
    1210 to WeatherSnowMessage,
    1213 to WeatherSnowMessage,
    1216 to WeatherSnowMessage,
    1219 to WeatherSnowMessage,
    1222 to WeatherSnowMessage,
    1225 to WeatherSnowMessage,
    1240 to WeatherRainMessage,
    1243 to WeatherRainMessage,
    1255 to WeatherSnowMessage,
    1273 to WeatherRainMessage,
    1276 to WeatherRainMessage
)