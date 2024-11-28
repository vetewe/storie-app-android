package com.example.storie.data.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storie.data.response.ListStoryItem
import com.example.storie.databinding.ItemStoryBinding
import com.example.storie.ui.activity.detail_story.DetailStoryActivity
import com.example.storie.utils.DateFormatter

class StoryAdapter :
    PagingDataAdapter<ListStoryItem, StoryAdapter.MyViewHolder>(
        DIFF_CALLBACK
    ) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val binding =
            ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(
            binding
        )
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int
    ) {
        val story = getItem(position)
        if (story != null) {
            holder.bind(story)
        }

        holder.itemView.setOnClickListener {
            val storyId = story?.id
            val intentId = Intent(holder.itemView.context, DetailStoryActivity::class.java).apply {
                putExtra(DetailStoryActivity.EXTRA_ID, storyId)
            }

            val optionCompat: ActivityOptionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    holder.itemView.context as Activity,
                    Pair(holder.binding.ivItemPhoto, "imageStoryDetail"),
                    Pair(holder.binding.tvDetailName, "nameStoryDetail"),
                    Pair(holder.binding.tvItemDescription, "descriptionStoryDetail"),
                    Pair(holder.binding.tvDetailDate, "dateStoryDetail")
                )

            holder.itemView.context.startActivity(intentId, optionCompat.toBundle())
        }
    }

    class MyViewHolder(val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(story: ListStoryItem) {
            binding.tvDetailName.text = story.name
            binding.tvItemDescription.text = story.description
            Glide.with(binding.root)
                .load(story.photoUrl)
                .into(binding.ivItemPhoto)
            val formattedDate = story.createdAt?.let { DateFormatter.formatDate(it) }
            binding.tvDetailDate.text = formattedDate
        }
    }


    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<ListStoryItem> =
            object : DiffUtil.ItemCallback<ListStoryItem>() {
                override fun areItemsTheSame(
                    oldItem: ListStoryItem,
                    newItem: ListStoryItem
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: ListStoryItem,
                    newItem: ListStoryItem
                ): Boolean {
                    return oldItem == newItem
                }

            }
    }
}