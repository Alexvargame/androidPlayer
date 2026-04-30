package com.example.musikplayer.add_data

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import android.util.Log
import com.example.musikplayer.data.AppDatabase
import com.example.musikplayer.data.entityes.TrackEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch



class Read_playlistts(private val context: Context) {

    fun readplaylists() {

        Log.d("TEST_DB_playlists", "Context $context")

        val db = AppDatabase.getInstance(context)

        Log.d(
            "TEST_DB_playlists",
            "DB path: ${context.getDatabasePath("app.db")}"
        )

        val playlistDao = db.playlistDao()

        CoroutineScope(Dispatchers.IO).launch {


            val playlists = playlistDao.getAllPlaylists()



            Log.d("TEST_DB_playlists", "ALL: $playlists")

            playlists.forEach {
                Log.d(
                    "TEST_DB_playlists",
                    "playlist: ${it.id} / ${it.name}"
                )
            }
        }
    }
}