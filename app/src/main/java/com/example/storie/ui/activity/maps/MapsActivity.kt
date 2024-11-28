package com.example.storie.ui.activity.maps

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.storie.R
import com.example.storie.data.Result
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.storie.databinding.ActivityMapsBinding
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.material.snackbar.Snackbar

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var mapsViewModel: MapsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factoryResult: MapsViewModelFactory =
            MapsViewModelFactory.getInstance(this)
        mapsViewModel =
            ViewModelProvider(this, factoryResult)[MapsViewModel::class.java]

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        setupViewModel()
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(-0.7893, 113.9213)))
        setupMapStyle()
    }


    private fun setupViewModel() {
        mapsViewModel.getStoryLocation()
        mapsViewModel.location.observe(this) { storyLocation ->
            when (storyLocation) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    storyLocation.data.forEach { data ->
                        val latLng = LatLng(data?.lat!!, data.lon!!)
                        mMap.addMarker(
                            MarkerOptions()
                                .position(latLng)
                                .title(data.name)
                                .snippet(data.description)
                        )

                    }
                }

                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Snackbar.make(
                        binding.root,
                        getString(R.string.failed_to_get_story_location),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }

            }
        }
    }

    private fun setupMapStyle() {
        try {
            val success = mMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    this,
                    R.raw.map_style_story_app
                )
            )
            if (!success) {
                Log.e(TAG, getString(R.string.style_parsing_failed))
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(TAG, "Error: ", exception)
        }
    }

    companion object {
        private const val TAG = "MapsActivity"
    }
}