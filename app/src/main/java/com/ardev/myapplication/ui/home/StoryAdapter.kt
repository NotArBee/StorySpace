package com.ardev.myapplication.ui.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ardev.myapplication.data.database.StoryEntity
import com.ardev.myapplication.databinding.ItemStoryBinding
import com.ardev.myapplication.ui.detail.DetailStoryActivity
import com.bumptech.glide.Glide

class StoryAdapter : PagingDataAdapter<StoryEntity, StoryAdapter.ViewHolder>(DiffCallback){
    class ViewHolder(private val binding : ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : StoryEntity?){
            with(binding){
                Glide.with(itemView)
                    .load(item?.photoUrl)
                    .into(ivStories)
                tvItemName.text = item?.name
                tvItemDescription.text = item?.description
                itemStory.setOnClickListener {
                    val intent = Intent(itemView.context, DetailStoryActivity::class.java)
                    intent.putExtra(DetailStoryActivity.STORIES_EXTRA, item)

                    itemView.context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val DiffCallback = object : DiffUtil.ItemCallback<StoryEntity>() {
            override fun areItemsTheSame(
                oldItem: StoryEntity,
                newItem: StoryEntity
            ): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: StoryEntity,
                newItem: StoryEntity
            ): Boolean = oldItem == newItem

        }
    }
}