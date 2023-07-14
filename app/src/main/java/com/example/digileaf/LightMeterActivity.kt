package com.example.digileaf

import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.text.bold

class LightMeterActivity: AppCompatActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private lateinit var lightSensor: Sensor
    private lateinit var lightMeterText: TextView
    private lateinit var lightMeterCard: CardView
    private lateinit var lightMeterLoading: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_light_meter)

        lightMeterText = findViewById(R.id.light_meter_text)
        lightMeterCard = findViewById(R.id.light_meter_card)
        lightMeterLoading = findViewById(R.id.light_meter_loading)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

        val backButton : ImageButton = findViewById(R.id.back_button)
        backButton.setOnClickListener{
            finish()
        }
    }

    override fun onSensorChanged(p0: SensorEvent?) {
        var lux = 0f
        if (p0 != null) {
            lux = p0.values[0]
        } else {
            return
        }
        var backgroundColor: Int = if (lux > 5000) {
            255
        } else {
            (255f * lux / 5000).toInt()
        }

        var textColor: Int = if (backgroundColor > 150) {
            0
        } else {
            255
        }

        lightMeterCard.setCardBackgroundColor(Color.rgb(backgroundColor, backgroundColor, backgroundColor))
        lightMeterText.setTextColor(Color.rgb(textColor, textColor, textColor))

        var s = SpannableStringBuilder()
        s = s.append("Luminosity: ").bold{append(lux.toString())}.append(" lx")

        var lightDescription = ""
        var plantDescription = ""
        if (lux < 500) {
            lightDescription = "low light"
            plantDescription = "This environment is not suitable for most plants!"
        } else if (lux < 2000) {
            lightDescription = "partially lit"
            plantDescription = "This environment is great for plants that need shade or indirect light!"
        } else if (lux < 4000) {
            lightDescription = "bright"
            plantDescription = "This environment is great for plants that need good and consistent light!"
        } else {
            lightDescription = "bright"
            plantDescription = "This environment is great for plants that need strong light!"
        }
        s = s.append("\nThis is considered a ").bold{append(lightDescription)}.append(" environment.")
        s = s.append("\n").append(plantDescription)

        lightMeterText.text = s
        lightMeterLoading.visibility = View.GONE
        lightMeterCard.visibility = View.VISIBLE
    }

    // override function for light sensor accuracy -> not needed but must be implemented
    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        Log.d("light meter", "accuracy changed")
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_FASTEST)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }
}