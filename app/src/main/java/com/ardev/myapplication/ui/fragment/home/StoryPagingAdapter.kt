package com.ardev.myapplication.ui.fragment.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadStateAdapter
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ardev.myapplication.data.response.ListStoryItem
import com.ardev.myapplication.databinding.StoryCardBinding
import com.ardev.myapplication.ui.activity.detailActivity.DetailStoryActivity
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

                holder.itemView.setOnClickListener {
                    val intent = Intent(holder.itemView.context, DetailStoryActivity::class.java)
                    intent.putExtra(DetailStoryActivity.EXTRA_ID, story.id)
                    holder.itemView.context.startActivity(intent)
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