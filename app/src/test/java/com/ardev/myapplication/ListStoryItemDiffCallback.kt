package com.ardev.myapplication

import androidx.recyclerview.widget.DiffUtil
import com.ardev.myapplication.data.response.ListStoryItem

class ListStoryItemDiffCallback : DiffUtil.ItemCallback<ListStoryItem>() {
    override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
        return oldItem == newItem
    }
}