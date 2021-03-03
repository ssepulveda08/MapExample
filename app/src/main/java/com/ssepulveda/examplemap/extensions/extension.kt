package com.ssepulveda.examplemap.extensions

import android.content.Context
import android.location.Location
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.maps.model.LatLng

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Location.toLatLng() = LatLng(
    this.latitude,
    this.longitude
)