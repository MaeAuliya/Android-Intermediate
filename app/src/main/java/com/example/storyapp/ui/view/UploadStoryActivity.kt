package com.example.storyapp.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.storyapp.databinding.ActivityUploadStoryBinding
import java.io.File
import android.Manifest
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.storyapp.ui.viewmodel.UploadStoryViewModel
import com.example.storyapp.utils.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody


class UploadStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadStoryBinding
    private lateinit var factory: ViewModelFactory
    private val uploadStoryViewModel: UploadStoryViewModel by viewModels { factory }

    private lateinit var currentPhotoPath: String

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var getFile: File? = null

    private var currentLat: Double? = null
    private var currentLon: Double? = null

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE_PERMISSIONS){
            if (!allPermissionsGranted()){
                Toast.makeText(this, "Tidak mendapatkan permission", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()){
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        setupView()
        setupViewModel()
        setupLocation()
        setupAction()

    }

    private fun setupLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getMyLastLocation()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else{
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupViewModel() {
        factory = ViewModelFactory.getInstance(this)

        uploadStoryViewModel.uploadData.observe(this){story ->
            if (!story.error){
                Toast.makeText(this, story.message, Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@UploadStoryActivity, MainActivity::class.java))
            }
        }

        uploadStoryViewModel.isLoading.observe(this){
            showLoading(it)
        }

        uploadStoryViewModel.isError.observe(this){
            showError(it)
        }

    }

    private fun startCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createCustomTempFile(application).also {
            val photoURI : Uri = FileProvider.getUriForFile(
                this@UploadStoryActivity,
                "com.example.storyapp",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"

        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun uploadImage(lat: Double? = null, lon: Double? = null) {

        uploadStoryViewModel.getUser().observe(this){user ->
            if (getFile != null){
                val file = reduceFileImage(getFile as File)

                val description = binding.edtDescStory.text.toString().toRequestBody("text/plain".toMediaType())
                val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
                val imageMultiPart : MultipartBody.Part = MultipartBody.Part.createFormData(
                    "photo",
                    file.name,
                    requestImageFile
                )

                uploadStoryViewModel.postStory(
                    user.token,
                    imageMultiPart,
                    description,
                    lat,
                    lon
                )
            } else {
                Toast.makeText(this@UploadStoryActivity, "Silakan masukkan berkas gambar terlebih dahulu.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupAction() {

        binding.btnCamera.setOnClickListener { startCamera() }
        binding.btnGallery.setOnClickListener { startGallery() }
        binding.btnUpload.setOnClickListener {

            val desc = binding.edtDescStory.text.toString()
            val checkBox = binding.myCheckbox

            when {
                desc.isEmpty() -> {
                    Toast.makeText(this, "Isi deskripsi dulu", Toast.LENGTH_SHORT).show()
                }
                checkBox.isChecked -> {
                    uploadImage(currentLat, currentLon)
                }
                else -> {
                    uploadImage(null, null)
                }
            }
        }
    }

    private fun checkPermission(permission: String) : Boolean{
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    getMyLastLocation()
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    getMyLastLocation()
                }
                else -> {

                }
            }
        }

    private fun getMyLastLocation() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) && checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)){
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null){
                    currentLat = location.latitude
                    currentLon = location.longitude
                } else {
                    Toast.makeText(this, "Location is not found, Try again", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
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

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if (it.resultCode == RESULT_OK){
            val myFile = File(currentPhotoPath)

            myFile.let { file ->
                getFile = file
                binding.ivAddStory.setImageBitmap(BitmapFactory.decodeFile(file.path))
            }
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){result ->
        if (result.resultCode == RESULT_OK){
            val selectedImage = result.data?.data as Uri

            selectedImage.let { uri ->
                val myFile = uriToFile(uri, this@UploadStoryActivity)
                getFile = myFile
                binding.ivAddStory.setImageURI(uri)
            }
        }
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }


}