package com.example.storie.ui.widget

import android.content.Intent
import android.widget.RemoteViewsService
import com.example.storie.di.Injection

class StoryWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        val repository = Injection.widgetStoryRepository(applicationContext)
        return StoryWidgetAdapter(applicationContext, repository)
    }
}