package com.example.storiesw.utils

import android.content.Context
import android.content.res.Resources
import android.util.Log
import com.example.storiesw.R
import com.example.storiesw.data.model.Story
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions

fun List<Story>.convertToLatLng(): List<LatLng> {
    val listMarker = ArrayList<LatLng>()
    for (story in this) {
        if (story.latitude != null && story.longitude != null) {
            listMarker.add(LatLng(story.latitude.toDouble(), story.longitude.toDouble()))
        }
    }
    return listMarker
}

fun GoogleMap.addMultipleMarker(stories: List<Story>) {
    for (story in stories) {
        if (story.latitude != null && story.longitude != null) {
            val marker = this.addMarker(
                createMarkerOptions(
                    LatLng(story.latitude.toDouble(), story.longitude.toDouble()),
                    story.name
                )
            )
            marker?.tag = story
            marker?.showInfoWindow()
        }
    }
}

fun GoogleMap.setCustomStyle(context: Context){
    try {
        val success =
            this.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style))
        if (!success) {
            Log.d("MapExt", "Style parsing failed.")
        }
    } catch (exception: Resources.NotFoundException) {
        Log.e("MapExt", "Can't find style. Error: ", exception)
    }
}

private fun createMarkerOptions(
    location: LatLng,
    markerName: String
): MarkerOptions {
    val iconDrawable = R.drawable.map_pin
    val icon = BitmapDescriptorFactory.fromResource(iconDrawable)
    return MarkerOptions().position(location)
        .title(markerName)
        .icon(icon)
}

fun GoogleMap.boundsCameraToMarkers(locations: List<LatLng>) {
    val builder = LatLngBounds.builder()
    for (location in locations) {
        builder.include(location)
    }
    val bounds = builder.build()
    val zoomLevel = 1
    val cu = CameraUpdateFactory.newLatLngBounds(bounds, zoomLevel)
    this.animateCamera(cu, 1000, null)
}