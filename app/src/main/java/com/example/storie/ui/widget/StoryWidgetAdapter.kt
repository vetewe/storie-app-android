package com.example.storie.ui.widget

import com.example.storie.data.Result
import android.content.Context
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.bumptech.glide.Glide
import com.example.storie.R
import com.example.storie.data.repositories.WidgetStoryRepository
import com.example.storie.data.response.ListStoryItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class StoryWidgetAdapter(
    private val context: Context,
    private val repository: WidgetStoryRepository
) : RemoteViewsService.RemoteViewsFactory {
    private var story: List<ListStoryItem> = listOf()

    override fun onCreate() {

    }

    override fun onDataSetChanged() {
        val result = runBlocking { repository.getStory() }
        story = if (result is Result.Success) {
            result.data.filterNotNull()
        } else {
            listOf()
        }
    }

    override fun onDestroy() {

    }

    override fun getCount(): Int {
        return story.size
    }

    override fun getViewAt(position: Int): RemoteViews {
        val story = story[position]
        val rv = RemoteViews(context.packageName, R.layout.item_widget_story)


        rv.setTextViewText(R.id.tv_item_description_widget, story.description)
        val bitmap = runBlocking {
            withContext(Dispatchers.IO) {
                Glide.with(context)
                    .asBitmap()
                    .load(story.photoUrl)
                    .submit()
                    .get()
            }
        }

        rv.setImageViewBitmap(R.id.iv_item_photo_widget, bitmap)
        return rv
    }

    override fun getLoadingView(): RemoteViews? {
        return null
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }
}