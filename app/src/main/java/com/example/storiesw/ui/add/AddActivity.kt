package com.example.storiesw.ui.add

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.net.Uri
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.example.storiesw.R
import com.example.storiesw.databinding.ActivityAddBinding
import com.example.storiesw.ui.base.BaseActivity
import com.example.storiesw.ui.home.HomeActivity
import com.example.storiesw.utils.reduceFileImage
import com.example.storiesw.utils.showLoading
import com.example.storiesw.utils.uriToFile
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class AddActivity : BaseActivity<ActivityAddBinding>() {
    private lateinit var viewModel: AddViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    private var imageFile: File? = null
    private var mUri: Uri? = null
    private var myLocation: Location? = null
    private var allowLocation: Boolean = false

    override fun getViewBinding(): ActivityAddBinding {
        return ActivityAddBinding.inflate(layoutInflater)
    }

    override fun setUI() {
        with(binding) {
            cameraButton.setOnClickListener {
                takePicture()
            }
            galleryButton.setOnClickListener {
                openGallery()
            }
        }
    }

    override fun setActions() {
        binding.apply {
            locationCheck.setOnCheckedChangeListener { _, isChecked ->
                allowLocation = isChecked
                if(allowLocation) {
                    getCurrentLocation()
                }
            }

            uploadButton.setOnClickListener {
                imageFile = mUri?.let { uriToFile(it, this@AddActivity).reduceFileImage() }
                val description = edDescriptionStory.text.toString()
                if (imageFile != null && description.isNotEmpty()) {
                    viewModel.uploadStory(
                        imageFile!!,
                        description,
                        if (allowLocation) myLocation?.latitude?.toFloat() else null,
                        if (allowLocation) myLocation?.longitude?.toFloat() else null
                    )
                } else {
                    showToast(getString(R.string.fill_required))
                }
            }
        }
    }

    override fun setProcess() {
        viewModel = ViewModelProvider(this)[AddViewModel::class.java]
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

    }

    override fun setObservers() {
        viewModel.uploadComplete.observe(this) {
            if (it) {
                showToast(getString(R.string.upload_success))
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        viewModel.isLoading.observe(this) {
            showLoading(binding.progressCard, it)
        }

        viewModel.isError.observe(this) { isError ->
            if(isError) {
                showErrorDialog()
            }
        }
    }

    private fun takePicture() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        resultLauncher.launch(takePictureIntent)
    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        resultLauncher.launch(galleryIntent)
    }

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            val imageUri: Uri? = if (data?.data != null) {
                data.data
            } else {
                val imageBitmap = data?.extras?.get("data") as? Bitmap
                imageBitmap?.let { saveBitmapToUri(it) }
            }

            if (imageUri != null) {
                binding.ivAddPhoto.setImageURI(imageUri)
                mUri = imageUri
            }
        }
    }


    private fun saveBitmapToUri(bitmap: Bitmap): Uri? {
        val file = File(cacheDir, "camera_image_${System.currentTimeMillis()}.jpg")
        try {
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
            return Uri.fromFile(file)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                myLocation = location
                Log.d("AddActivity", "Current location: ${location.latitude}, ${location.longitude}")
            } else {
                startLocationUpdates()
            }
        }
    }

    private fun startLocationUpdates() {
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY, 10000
        ).apply {
            setMinUpdateIntervalMillis(5000)
        }.build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.locations.forEach { location ->
                    myLocation = location
                    Log.d("AddActivity", "Location updated: ${location.latitude}, ${location.longitude}")
                }
            }
        }

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }

}