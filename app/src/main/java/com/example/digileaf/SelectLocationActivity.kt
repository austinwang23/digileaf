package com.example.digileaf

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener

class SelectLocationActivity : AppCompatActivity() {

    private var TAG = "Info :"
    private lateinit var placesClient: PlacesClient
    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    private lateinit var latLong: String
    private var latitude = -1.0 as Double
    private var longitude = -1.0 as Double


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_location)
        val confirmLocationButton: Button = findViewById(R.id.confirm_location)

        mapView = findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { googleMap ->
            this.googleMap = googleMap

            val latitude = intent.getDoubleExtra("lat", 37.3861)
            val longitude = intent.getDoubleExtra("long", -122.0839)

            val latLng = LatLng(latitude, longitude)
            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10f)
            googleMap.moveCamera(cameraUpdate)
        }

        Places.initialize(this, resources.getString(R.string.googlemap_key))
        placesClient = Places.createClient(this)

        // Initialize the AutocompleteSupportFragment.
        val autocompleteFragment = supportFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment

        autocompleteFragment.setLocationBias(
            RectangularBounds.newInstance(
                LatLng(-33.880490, 151.184363),
                LatLng(-33.858754, 151.229596)
            )
        )

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG))

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: ${place.name}, ${place.id}")
                latLong = place.latLng?.latitude.toString() + "," + place.latLng?.longitude.toString()
                if (googleMap != null) {
                    val cameraUpdate = CameraUpdateFactory.newLatLngZoom(place.latLng, 10f)
                    googleMap.moveCamera(cameraUpdate)
                }
                Toast.makeText(applicationContext, "Set Location to ${place.name}", Toast.LENGTH_SHORT).show()
                confirmLocationButton.isEnabled = true
                if (place.latLng != null) {
                    latitude =  place.latLng!!.latitude
                    longitude = place.latLng!!.longitude
                }
            }

            override fun onError(status: Status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: $status")
            }
        })

        val backButton : ImageButton = findViewById(R.id.back_button)
        backButton.setOnClickListener{
            finish()
        }

        confirmLocationButton.setOnClickListener{
            val intent = Intent()
            intent.putExtra("coordinates",latLong)
            intent.putExtra("lat", latitude)
            intent.putExtra("long", longitude)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}