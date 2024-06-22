package com.ardev.myapplication.ui

import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.ardev.myapplication.databinding.ActivityDetailStoryBinding
import com.bumptech.glide.Glide
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoryBinding
    private lateinit var viewModel: DetailStoryViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = DetailStoryViewModelFactory(application)
        viewModel = ViewModelProvider(this, factory).get(DetailStoryViewModel::class.java)

        val idStory = intent.getStringExtra(EXTRA_ID)

        viewModel.userData.observe(this) { detailStory ->
            detailStory?.let {
                val token = detailStory.token
                if (idStory != null) {
                    viewModel.getDetailStories("Bearer $token", idStory)
                }
            }
        }

        viewModel.detailStory.observe(this) { detailStory ->
            binding.apply {
                materialToolbar.title = detailStory.name
                Glide.with(this@DetailStoryActivity)
                    .load(detailStory.photoUrl)
                    .into(ivPhoto)
                tvDescription.text = detailStory.description

                val dateString = detailStory.createdAt
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                val dateTime = LocalDateTime.parse(dateString, formatter)
                val date = dateTime.toLocalDate()

                tvCreatedAt.text = date.toString()
            }
        }
    }

    companion object {
        const val EXTRA_ID = "extra_id"
    }
}
