package com.example.storie.ui.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.example.storie.R
import com.example.storie.utils.AppExecutors

@Suppress("DEPRECATION")
class StoryWidget : AppWidgetProvider() {
    private val appExecutors = AppExecutors()

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            appExecutors.diskIO.execute {
                updateAppWidget(context, appWidgetManager, appWidgetId)
            }
        }
    }

    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val rv = RemoteViews(context.packageName, R.layout.story_widget)
        val intent = Intent(context, StoryWidgetService::class.java).apply {
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        }

        rv.setRemoteAdapter(R.id.widget_list_view, intent)
        rv.setEmptyView(R.id.widget_list_view, R.id.empty_view)

        appWidgetManager.updateAppWidget(appWidgetId, rv)
    }

}