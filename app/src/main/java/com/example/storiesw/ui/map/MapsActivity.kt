package com.example.storiesw.ui.map

import androidx.lifecycle.ViewModelProvider
import com.example.storiesw.R
import com.example.storiesw.databinding.ActivityMapsBinding
import com.example.storiesw.ui.base.BaseActivity
import com.example.storiesw.utils.addMultipleMarker
import com.example.storiesw.utils.boundsCameraToMarkers
import com.example.storiesw.utils.convertToLatLng
import com.example.storiesw.utils.setCustomStyle
import com.example.storiesw.utils.showLoading
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng

class MapsActivity : BaseActivity<ActivityMapsBinding>(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var viewModel: MapsViewModel

    override fun getViewBinding(): ActivityMapsBinding {
        return ActivityMapsBinding.inflate(layoutInflater)
    }

    override fun setUI() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        setActivityLabel(getString(R.string.maps))

    }

    override fun setProcess() {
        viewModel = ViewModelProvider(this)[MapsViewModel::class.java]
        viewModel.setStoriesLocation()
    }



    override fun setObservers() {
        viewModel.getStories().observe(this) {response ->
            response?.let { mMap.addMultipleMarker(it) }
            val listLocations = response?.convertToLatLng()
            if (listLocations != null) {
                mMap.boundsCameraToMarkers(listLocations)
                setButtonBounds(mMap, listLocations)
            }
        }

        viewModel.isLoading.observe(this) {
            showLoading(binding.progressCard, it)
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setCustomStyle(this)
    }

    private fun setButtonBounds(mMap: GoogleMap, listLocations: List<LatLng>) {
        binding.btBounds.setOnClickListener {
            mMap.boundsCameraToMarkers(listLocations)
        }
    }
}