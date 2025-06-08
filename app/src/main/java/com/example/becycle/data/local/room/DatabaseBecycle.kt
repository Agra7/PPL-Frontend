package com.example.becycle.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.becycle.data.local.entity.ArticleEntity
import com.example.becycle.data.local.entity.HistoryEntity
import com.example.becycle.data.local.entity.RecycleEntity
import com.example.becycle.data.local.entity.UserEntity

@Database(entities = [ArticleEntity::class, HistoryEntity::class, RecycleEntity::class, UserEntity::class], version = 1, exportSchema = false)
abstract class DatabaseBecycle : RoomDatabase() {

    abstract fun articleDao(): ArticleDao
    abstract fun historyDao(): HistoryDao
    abstract fun recycleDao(): RecycleDao
    abstract fun userDao(): UserDao


    companion object {
        @Volatile
        private var instance: com.example.becycle.data.local.room.DatabaseBecycle? = null
        fun getInstance(context: Context): com.example.becycle.data.local.room.DatabaseBecycle =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseBecycle::class.java, "Becycle.db"
                )
                    .fallbackToDestructiveMigration()
                    .build().also { instance = it }
            }
    }
}