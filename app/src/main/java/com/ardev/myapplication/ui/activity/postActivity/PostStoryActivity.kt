package com.ardev.myapplication.ui.activity.postActivity

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.ardev.myapplication.R
import com.ardev.myapplication.data.response.NewStoryResponse
import com.ardev.myapplication.data.retrofit.ApiConfig
import com.ardev.myapplication.databinding.ActivityPostStoryBinding
import com.ardev.myapplication.ui.DetailStoryViewModelFactory
import com.ardev.myapplication.utils.ViewModelFactory
import com.ardev.myapplication.utils.getImageUri
import com.ardev.myapplication.utils.reduceFileImage
import com.ardev.myapplication.utils.uriToFile
import com.bumptech.glide.Glide
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.IOException

class PostStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostStoryBinding
    private var currentImageUri: Uri? = null
    private lateinit var viewModel: PostStoryActivityViewModel
    private var token: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = DetailStoryViewModelFactory(application)
        viewModel = ViewModelProvider(this, factory).get(PostStoryActivityViewModel::class.java)

        viewModel.userData.observe(this) { story ->
            story?.let {
                token = story.token
            }
        }

        binding.btnGallery.setOnClickListener { startGallery() }
        binding.btnCamera.setOnClickListener { startCamera() }
        binding.btnUpload.setOnClickListener { uploadImage() }

        // Request permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 0)
        }
    }

    private fun uploadImage() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            Log.d("Image File", "showImage: ${imageFile.path}")
            val description = binding.etDescription.text.toString()

            if (description.isEmpty()) {
                showToast(getString(R.string.empty_description_warning))
                return
            }

            showLoading(true)

            val requestBody = description.toRequestBody("text/plain".toMediaType())
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "photo",
                imageFile.name,
                requestImageFile
            )

            lifecycleScope.launch {
                try {
                    val apiService = ApiConfig.getApiService()
                    val successResponse = apiService.uploadStories(
                        requestBody,
                        multipartBody,
                        token = "Bearer $token"
                    )
                    showToast(successResponse.message)
                } catch (e: HttpException) {
                    val errorBody = e.response()?.errorBody()?.string()
                    val errorResponse = Gson().fromJson(errorBody, NewStoryResponse::class.java)
                    showToast(errorResponse.message)
                } catch (e: IOException) {
                    showToast(getString(R.string.network_error))
                } catch (e: Exception) {
                    showToast(getString(R.string.unknown_error))
                } finally {
                    showLoading(false)
                }
            }
        } ?: showToast(getString(R.string.empty_image_warning))
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri!!)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery =
        registerForActivityResult(
            ActivityResultContracts.PickVisualMedia()
        ) { uri: Uri? ->
            if (uri != null) {
                currentImageUri = uri
                showImage()
            } else {
                Log.d("Photo Picker", "No media selected")
            }
        }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            Glide.with(this)
                .load(it)
                .into(binding.ivPhoto)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
