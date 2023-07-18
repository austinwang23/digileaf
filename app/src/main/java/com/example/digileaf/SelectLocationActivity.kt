package com.example.digileaf

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import android.widget.SearchView
class SelectLocationActivity : AppCompatActivity() {

    private val LOCATION_PERMISSION_REQUEST_CODE = 1

    private lateinit var searchView: SearchView
    private lateinit var listView: ListView

    private lateinit var arrayList: ArrayList<String>
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_switch_location)

        searchView = findViewById(R.id.location_search)
        listView = findViewById(R.id.location_list_view)

        listView.visibility = View.GONE

        arrayList = ArrayList()
        arrayList.add("Toronto")
        arrayList.add("Kitchener")
        arrayList.add("Waterloo")
        arrayList.add("Mountain View")

        adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList)
        listView.adapter = adapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                listView.visibility = View.VISIBLE
                adapter.filter.filter(newText)
                return false
            }
        })

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                this,
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

        }

        val backButton : ImageButton = findViewById(R.id.back_button)
        backButton.setOnClickListener{
            finish()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && (grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1] == PackageManager.PERMISSION_GRANTED)) {

            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }
}