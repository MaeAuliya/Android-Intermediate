package com.example.storyapp.utils

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapp.data.model.StoryModel
import com.example.storyapp.databinding.ItemStoryBinding
import com.example.storyapp.ui.view.DetailActivity


class StoryAdapter:
    PagingDataAdapter<StoryModel, StoryAdapter.StoryViewHolder>(DIFF_CALLBACK){


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StoryViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null){
            holder.bind(data)
        }

    }

    class StoryViewHolder(var binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(list: StoryModel){
            binding.apply {
                tvTitle.text = list.name
                Glide.with(ivStory.context)
                    .load(list.photoUrl)
                    .into(ivStory)
            }

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_DATA, list.id)
                itemView.context.startActivity(intent)
            }
        }
    }


    companion object {
        val DIFF_CALLBACK = object :DiffUtil.ItemCallback<StoryModel>(){
            override fun areItemsTheSame(oldItem: StoryModel, newItem: StoryModel): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: StoryModel, newItem: StoryModel): Boolean {
                return oldItem.name == newItem.name && oldItem.photoUrl == newItem.photoUrl
            }

        }
    }

}