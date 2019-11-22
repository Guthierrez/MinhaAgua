package com.guthierrez.minhaagua.ui.leaks

import android.Manifest.permission.*
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.guthierrez.minhaagua.exception.ValidationException
import com.guthierrez.minhaagua.util.SecurityPreferences
import kotlinx.android.synthetic.main.activity_leak_form.*
import java.text.SimpleDateFormat
import java.util.*
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.guthierrez.minhaagua.BuildConfig
import com.guthierrez.minhaagua.R
import com.guthierrez.minhaagua.business.ImageUploaderBusiness
import com.guthierrez.minhaagua.business.LeakBusiness
import com.guthierrez.minhaagua.constants.AppConstants
import com.guthierrez.minhaagua.listeners.ImageListEventListener
import com.guthierrez.minhaagua.model.Leak
import com.guthierrez.minhaagua.model.LeakImage
import com.guthierrez.minhaagua.ui.adapter.ImageListAdapter
import java.io.File
import java.io.IOException
import java.lang.Exception


@SuppressLint("SetTextI18n")
class LeakFormActivity : AppCompatActivity(), View.OnClickListener, OnMapReadyCallback {

    private val locationRequestCode = 101
    private val imagePickRequestCode = 102
    private val imageCaptureRequestCode = 103
    private val defaultOnError: (ex: Exception) -> Unit = {
        showFailure(it)
    }

    private lateinit var leakBusiness: LeakBusiness
    private lateinit var securityPreferences: SecurityPreferences
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var imageListEventListener: ImageListEventListener
    private lateinit var imageUploaderBusiness: ImageUploaderBusiness
    private lateinit var recyclerImageList: RecyclerView

    private val images: MutableList<LeakImage> = mutableListOf()

    private var currentLocation: LatLng = LatLng(0.0, 0.0)
    private var marker: Marker? = null
    private var currentPhotoPath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leak_form)

        leakBusiness = LeakBusiness()
        securityPreferences = SecurityPreferences(this)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        imageListEventListener = object : ImageListEventListener {
            override fun onImageDelete(leakImage: LeakImage) {
                imageUploaderBusiness.deleteUploadedImage(leakImage) {
                    images.remove(it)
                    recyclerLeakList.adapter = ImageListAdapter(images, imageListEventListener)
                }
            }

        }
        recyclerImageList = findViewById(R.id.recyclerLeakList)
        imageUploaderBusiness = ImageUploaderBusiness()

        recyclerLeakList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerLeakList.adapter = ImageListAdapter(images, imageListEventListener)

        setListeners()
        loadDataFromActivity()
        checkLocationPermission()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.buttonSave -> handleSave()
            R.id.buttonPickImage -> checkPickImagePermission()
            R.id.buttonTakeImage -> checkCaptureImagePermission()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        updateMapCurrentLocation(googleMap)
        googleMap.setOnMapClickListener {
            currentLocation = it
            updateMapCurrentLocation(googleMap)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        val isPermissionGranted: (IntArray) -> Boolean = { results -> results.isNotEmpty() && results.all { it == PackageManager.PERMISSION_GRANTED } }

        when (requestCode) {
            locationRequestCode -> {
                if (isPermissionGranted(grantResults)) {
                    findCurrentLocation()
                } else {
                    Toast.makeText(this, "Não foi possível obter a localização atual.", Toast.LENGTH_LONG).show()
                }
            }
            imagePickRequestCode -> {
                if (isPermissionGranted(grantResults)) {
                    openImagePick()
                } else {
                    Toast.makeText(this, "Não foi possível iniciar a escolha de imagens.", Toast.LENGTH_LONG).show()
                }
            }
            imageCaptureRequestCode -> {
                if (isPermissionGranted(grantResults)) {
                    openImageCapture()
                } else {
                    Toast.makeText(this, "Não foi possível iniciar a captura de imagens.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                imagePickRequestCode -> {
                    val selectedImage = data?.data
                    if (selectedImage != null) {
                        addImageToList(selectedImage)
                    }
                }
                imageCaptureRequestCode -> {
                    addImageToList(Uri.parse(currentPhotoPath))
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun setListeners() {
        buttonSave.setOnClickListener(this)
        buttonPickImage.setOnClickListener(this)
        buttonTakeImage.setOnClickListener(this)
    }

    private fun handleSave() {
        try {
            val description = editDescription.text.toString()
            val user = securityPreferences.getStoredString(AppConstants.KEY.USER_ID)
            val leak = Leak(description, images, currentLocation, user!!)
            leakBusiness.saveLeak(leak, defaultOnError) {
                finish()
            }
        } catch (exception: ValidationException) {
            Toast.makeText(this, exception.message, Toast.LENGTH_LONG).show()
        } catch (exception: Exception) {
            showFailure(exception)
        }
    }

    private fun loadDataFromActivity() {
        val bundle = intent.extras
        if (bundle != null) {
            buttonSave.text = "Alterar"
        }
    }

    private fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION), locationRequestCode)
        } else {
            findCurrentLocation()
        }
    }

    private fun findCurrentLocation() {
        val task = fusedLocationProviderClient.lastLocation
        task.addOnSuccessListener { location ->
            if (location != null) {
                currentLocation = LatLng(location.latitude, location.longitude)
                val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment?
                mapFragment!!.getMapAsync(this)
            }
        }
    }

    private fun updateMapCurrentLocation(googleMap: GoogleMap) {
        if (marker != null) {
            marker!!.position = currentLocation
        } else {
            marker = googleMap.addMarker(MarkerOptions().position(currentLocation))
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15F))
        }
    }

    private fun checkPickImagePermission() {
        if (ActivityCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(READ_EXTERNAL_STORAGE, INTERNET), imagePickRequestCode)
        } else {
            openImagePick()
        }
    }

    private fun openImagePick() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val mimeTypes = arrayOf("image/jpeg", "image/png")
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        }
        startActivityForResult(intent, imagePickRequestCode)
    }

    private fun checkCaptureImagePermission() {
        if (ActivityCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(WRITE_EXTERNAL_STORAGE, INTERNET), imageCaptureRequestCode)
        } else {
            openImageCapture()
        }
    }

    private fun openImageCapture() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", createImageFile()))
        startActivityForResult(intent, imageCaptureRequestCode)
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
                "JPEG_${timeStamp}_",
                ".jpg",
                storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    private fun addImageToList(imageUri: Uri) {
        val leakImage = LeakImage(imageUri)
        imageUploaderBusiness.uploadImage(leakImage)
        images.add(leakImage)
        recyclerLeakList.adapter = ImageListAdapter(images, imageListEventListener)
    }

    private fun showFailure(ex: Exception) {
        Toast.makeText(this, "Ocorreu um erro inesperado.", Toast.LENGTH_LONG).show()
        Log.e(null, "Erro ao salvar vazamento.", ex)
    }
}
