package com.example.storyapp.ui.view

import android.content.pm.PackageManager
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.example.storyapp.R
import com.example.storyapp.databinding.ActivityLocationBinding
import com.example.storyapp.ui.viewmodel.LocationViewModel
import com.example.storyapp.utils.Constant
import com.example.storyapp.utils.ViewModelFactory

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions



class LocationActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityLocationBinding
    private lateinit var factory: ViewModelFactory
    private val locationViewModel: LocationViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true


        getMyLocation()
        setMapStyle()
        setupLocation()

    }

    private fun setupViewModel() {
        factory = ViewModelFactory.getInstance(this)

        locationViewModel.getUser().observe(this){user ->
            locationViewModel.getAllLocation(user.token)
        }

        locationViewModel.isLoading.observe(this){
            showLoading(it)
        }

        locationViewModel.isError.observe(this){
            showError(it)
        }

    }

    private fun setupLocation() {
        locationViewModel.allLocation.observe(this){location ->
            location.listStory.forEach{ story ->
                mMap.addMarker(
                    MarkerOptions()
                        .position(LatLng(story.lat as Double, story.lon as Double))
                        .title("From: ${story.name}")
                )
            }
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {isGranted : Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun setMapStyle() {
        try {
            val success = mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (e : Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", e)
        }
    }

    private fun showLoading(isLoading : Boolean){
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun showError(isError : Boolean){
        if (isError){
            Toast.makeText(this, Constant.ERROR_MESSAGE, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val TAG = "LocationActivity"
    }
}