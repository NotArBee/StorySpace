package com.ardev.myapplication.ui.upload_stories

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.ardev.myapplication.databinding.ActivityUploadStoryBinding
import com.ardev.myapplication.utils.ViewModelFactory
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import com.ardev.myapplication.data.response.Result
import com.ardev.myapplication.ui.home.HomeActivity
import com.ardev.myapplication.utils.createTempFile
import com.ardev.myapplication.utils.gone
import com.ardev.myapplication.utils.reduceFileImage
import com.ardev.myapplication.utils.show
import com.ardev.myapplication.utils.uriToFile
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class UploadStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadStoryBinding

    private val viewModel by viewModels<UploadViewModel> {
        ViewModelFactory.getInstance(this)
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
    private lateinit var currentPhotoPath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cameraButton.setOnClickListener {
            startCamera()
        }
        binding.galleryButton.setOnClickListener { startGallery() }
        binding.uploadButton.setOnClickListener { uploadImage() }

        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.title = "Upload Stories"
        }

        // Request permissions
        requestCameraPermission()
    }

    private fun requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }
    }

    private fun uploadStory(imageMultipart: MultipartBody.Part, toRequestBody: RequestBody) {
        viewModel.uploadStories(imageMultipart, toRequestBody).observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    showLoading(true)
                }
                is Result.Success -> {
                    showLoading(false)
                    result.data.let {
                        if (!it.error) {
                            Intent(this@UploadStoryActivity, HomeActivity::class.java).also {
                                startActivity(it)
                                finishAffinity()
                            }
                        }
                    }
                }
                is Result.Error -> {
                    showLoading(false)
                    showMessage(result.error)
                }
            }
        }
    }

    private fun uploadImage() {
        val description = binding.etUploadDesc.text.toString()

        if (getFile != null) {
            val file = reduceFileImage(getFile as File)
            val requestImageFile = file.asRequestBody("image/jpg".toMediaType())
            val imageMultipart: MultipartBody.Part =
                MultipartBody.Part.createFormData("photo", file.name, requestImageFile)
            uploadStory(
                imageMultipart,
                description.toRequestBody("text/plain".toMediaType())
            )
        } else {
            binding.uploadButton.isEnabled = true
            showMessage("Image is Required")
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun startCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(packageManager) != null) {
            createTempFile(application).also {
                val photoURI: Uri = FileProvider.getUriForFile(
                    this@UploadStoryActivity,
                    "com.ardev.myapplication.fileprovider",
                    it
                )
                currentPhotoPath = it.absolutePath
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                launcherIntentCamera.launch(intent)
            }
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private var getFile: File? = null
    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            getFile = myFile
            val result = BitmapFactory.decodeFile(myFile.path)
            binding.ivUpload.setImageBitmap(result)
        } else {
            Toast.makeText(this, "Failed to take picture", Toast.LENGTH_SHORT).show()
        }
    }

    private val launcherIntentGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val selectedImg: Uri = result.data?.data as Uri
                val myFile = uriToFile(selectedImg, this@UploadStoryActivity)
                getFile = myFile
                binding.ivUpload.setImageURI(selectedImg)
            }
        }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) binding.progressbar.show() else binding.progressbar.gone()
    }
}
