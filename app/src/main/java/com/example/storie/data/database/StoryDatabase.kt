package com.example.storie.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.storie.data.response.ListStoryItem

@Database(
    entities = [ListStoryItem::class, RemoteKeys::class],
    version = 2,
    exportSchema = false
)
abstract class StoryDatabase : RoomDatabase() {
    abstract fun storyDao(): StoryDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        @Volatile
        private var instance: StoryDatabase? = null

        @JvmStatic
        fun getInstance(context: Context): StoryDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    StoryDatabase::class.java, "story_database"
                )
                    .fallbackToDestructiveMigrationFrom()
                    .build()
                    .also { instance = it }
            }
        }
    }
}