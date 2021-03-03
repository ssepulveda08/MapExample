package com.ssepulveda.examplemap.ui.activitys

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolygonOptions
import com.ssepulveda.examplemap.R
import com.ssepulveda.examplemap.databinding.ActivityMainBinding
import com.ssepulveda.examplemap.extensions.toLatLng
import com.ssepulveda.examplemap.extensions.toast
import com.ssepulveda.examplemap.utils.PermissionRequester

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private val coarsePermission = PermissionRequester(this)

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var binding: ActivityMainBinding

    private var googlemap: GoogleMap? = null

    private var arrayPoints = mutableListOf<LatLng>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        setupLocation()
        setupMap()
        addListener()
    }

    private fun setupLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    private fun setupMap() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
    }

    private fun addListener() {
        binding.fab.setOnClickListener {
            coarsePermission.runWithPermission(Manifest.permission.ACCESS_FINE_LOCATION) {
                if (this::fusedLocationClient.isInitialized) {
                    addMarkerCurrentLocation()
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun addMarkerCurrentLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let { currentLocation ->
                    addMarker(currentLocation.toLatLng(), true)
                }
            }
    }

    private fun addMarker(latLng: LatLng, centerMarker: Boolean = false) {
        googlemap?.apply {
            arrayPoints.add(latLng)
            clear()
            if (centerMarker) {
                moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        latLng,
                        12.0f
                    )
                )
            }
            arrayPoints.forEach { location ->
                addMarker(MarkerOptions().apply {
                    position(
                        location
                    )
                    title(getString(R.string.copy_location))
                    snippet(getString(R.string.copy_location))
                })
            }
            addPolygon(
                PolygonOptions()
                    .clickable(true)
                    .fillColor(R.color.black)
                    .addAll(arrayPoints)
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            R.id.action_location -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onMapReady(map: GoogleMap?) {
        googlemap = map
        googlemap?.setOnMapClickListener {
            addMarker(it)
        }
    }

}