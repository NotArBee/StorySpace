package com.ardev.myapplication.ui.fragment.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadStateAdapter
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ardev.myapplication.data.response.ListStoryItem
import com.ardev.myapplication.databinding.StoryCardBinding
import com.bumptech.glide.Glide

class StoryPagingAdapter : PagingDataAdapter<ListStoryItem, StoryPagingAdapter.ListViewHolder>(DIFF_CALLBACK) {

    inner class ListViewHolder(val binding: StoryCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = StoryCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val story = getItem(position)
        story?.let {
            holder.binding.apply {
                tvName.text = story.name
                tvDescription.text = story.description
                tvCreatedAt.text = story.createdAt
                Glide.with(holder.itemView.context)
                    .load(story.photoUrl)
                    .into(ivPhoto)

                // Set click listener using itemView
                holder.itemView.setOnClickListener {
                    // Handle item click
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
