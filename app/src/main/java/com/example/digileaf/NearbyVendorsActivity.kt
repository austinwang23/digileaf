package com.example.digileaf

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.osmdroid.bonuspack.location.NominatimPOIProvider
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.FolderOverlay
import org.osmdroid.views.overlay.Marker

class NearbyVendorsActivity: AppCompatActivity() {
    private lateinit var map: MapView
    private lateinit var currentLocation: GeoPoint
    private lateinit var loadingView: LinearLayout

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nearby_vendors)
        Configuration.getInstance().userAgentValue = this.packageName

        map = findViewById(R.id.nearby_vendors_map)
        map.setMultiTouchControls(true)

        val priority = Priority.PRIORITY_BALANCED_POWER_ACCURACY
        val cancellationTokenSource = CancellationTokenSource()
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        val backButton : ImageButton = findViewById(R.id.back_button)
        backButton.setOnClickListener{
            val nearbyVendorsIntent = Intent()
            setResult(Activity.RESULT_OK, nearbyVendorsIntent)
            finish()
        }

        loadingView = findViewById(R.id.nearby_vendors_loading)

        // To do - make sure permission is granted
        fusedLocationClient.getCurrentLocation(priority, cancellationTokenSource.token)
            .addOnSuccessListener { location ->
                // add current location
                currentLocation = GeoPoint(location.latitude, location.longitude)
                val mapController = map.controller
                mapController.setZoom(12.0)
                mapController.setCenter(currentLocation)
                val currentLocationMarker = Marker(map)
                currentLocationMarker.position = currentLocation
                currentLocationMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                currentLocationMarker.title = "My location"
                currentLocationMarker.icon = AppCompatResources.getDrawable(this, R.drawable.ic_current_location)
                map.overlays.add(currentLocationMarker)
                loadVendors(this)
            }
            .addOnFailureListener { exception ->
                Log.d("Location", "Oops location failed with exception: $exception")
            }
    }

    private fun loadVendors(context: Context){
        lifecycleScope.launch(Dispatchers.Main){
            addVendorsToMap(context)
        }
    }

    private fun addVendorsToMap(context: Context) {
        lifecycleScope.launch(Dispatchers.IO) {
            // add nearby vendors
            val poiProvider = NominatimPOIProvider(context.packageName)
            val nearbyVendors = poiProvider.getPOICloseTo(currentLocation, "garden center", 50, 0.2)
            val nearbyVendorMarkers = FolderOverlay()
            val vendorIcon = AppCompatResources.getDrawable(context, R.drawable.ic_vendor_location)
            for (vendor in nearbyVendors) {
                val vendorMarker = Marker(map)
                vendorMarker.title = "Garden Center"
                vendorMarker.snippet = vendor.mDescription
                vendorMarker.position = vendor.mLocation
                vendorMarker.icon = vendorIcon
                if (vendor.mThumbnail != null) {
                    vendorMarker.image = BitmapDrawable(vendor.mThumbnail)
                }
                nearbyVendorMarkers.add(vendorMarker)
            }

            // display map
            runOnUiThread {
                map.overlays.add(nearbyVendorMarkers)
                loadingView.visibility = View.GONE
                map.invalidate()
                map.visibility = View.VISIBLE
            }
        }
    }
}

