package com.ardev.myapplication.ui.detail

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ardev.myapplication.R
import com.ardev.myapplication.data.database.StoryEntity
import com.ardev.myapplication.databinding.ActivityDetailStoryBinding
import com.bumptech.glide.Glide

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setView()
    }

    @Suppress("DEPRECATION")
    private fun setView() = with(binding) {
        val story = intent.getParcelableExtra<StoryEntity>(STORIES_EXTRA) as StoryEntity
        Glide.with(this@DetailStoryActivity)
            .load(story.photoUrl)
            .into(ivStories)
        tvItemDescription.text = story.description
        supportActionBar?.title = story.name
    }

    companion object{
        const val STORIES_EXTRA = "stories_extra"
    }
}