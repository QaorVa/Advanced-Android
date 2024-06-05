package com.example.storiesw.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.storiesw.data.model.Story
import com.example.storiesw.databinding.ItemStoryBinding
import com.example.storiesw.utils.getTimeAgo
import com.example.storiesw.utils.setImageUrl

class HomeAdapter(private val onItemClickCallback: OnItemClickCallback) : PagingDataAdapter<Story, HomeAdapter.StoryViewHolder>(
    DIFF_CALLBACK
){
    interface OnItemClickCallback {
        fun onItemClicked(data: Story)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
        holder.itemView.setOnClickListener { getItem(holder.adapterPosition)?.let { it1 ->
            onItemClickCallback.onItemClicked(
                it1
            )
        } }
    }

    inner class StoryViewHolder(private val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(story: Story) {
            binding.apply {
                imgStory.setImageUrl(story.photoUrl)
                tvUserName.text = story.name
                tvUploadAt.text = story.createdAt.getTimeAgo(root.context)
                tvDescription.text = story.description
            }
        }

    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }


}