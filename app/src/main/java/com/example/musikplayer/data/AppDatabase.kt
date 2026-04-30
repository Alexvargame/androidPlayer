package com.example.musikplayer.data


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

import com.example.musikplayer.data.entityes.TrackEntity
import com.example.musikplayer.data.entityes.PlaylistEntity
import com.example.musikplayer.data.entityes.PlayListTrackEntity

import com.example.musikplayer.data.dao.TrackDao
import com.example.musikplayer.data.dao.PlaylistDao
import com.example.musikplayer.data.dao.PlaylistTrackDao
@Database(
    entities = [
        TrackEntity::class,
        PlaylistEntity::class,
        PlayListTrackEntity::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun trackDao(): TrackDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun playlistTrackDao(): PlaylistTrackDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app.db"
                ).build()

                INSTANCE = instance
                instance
            }
        }
    }
}