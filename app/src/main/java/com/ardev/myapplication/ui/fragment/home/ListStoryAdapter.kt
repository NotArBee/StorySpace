package com.ardev.myapplication.ui.fragment.home

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.ardev.myapplication.data.response.ListStoryItem
import com.ardev.myapplication.databinding.StoryCardBinding
import com.bumptech.glide.Glide
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ListStoryAdapter(private val listStory: List<ListStoryItem>) :
    RecyclerView.Adapter<ListStoryAdapter.ListViewHolder>() {
    inner class ListViewHolder(val binding: StoryCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = StoryCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int = listStory.size

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val story = listStory[position]

        val dateString = story.createdAt
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val dateTime = LocalDateTime.parse(dateString, formatter)
        val date = dateTime.toLocalDate()

        holder.binding.apply {
            tvName.text = story.name
            tvDescription.text = story.description
            tvCreatedAt.text = date.toString()
            Glide.with(holder.itemView.context)
                .load(story.photoUrl)
                .into(ivPhoto)
        }
    }

}