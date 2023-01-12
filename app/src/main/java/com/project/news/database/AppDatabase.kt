package com.project.news.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.project.news.data.Bookmark
import com.project.news.viewModel.dao.BookmarksDao

@Database(entities = [Bookmark :: class], version = 1)
abstract class AppDatabase : RoomDatabase(){

    abstract fun bookmarksDao() : BookmarksDao

    companion object{

        @Volatile
        private var INSTANCE : AppDatabase? = null

        fun getDatabaseInstance(context :Context) : AppDatabase {
            synchronized(this) {

                val tempInstance = INSTANCE
                if (tempInstance != null) {
                    return tempInstance
                }

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                return instance

            }
        }
    }
}