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



class Read_tracks(private val context: Context) {

    fun readTracks() {

        Log.d("TEST_DB_tracks", "Context $context")

        val db = AppDatabase.getInstance(context)

        Log.d(
            "TEST_DB_tracks",
            "DB path: ${context.getDatabasePath("app.db")}"
        )

        val trackDao = db.trackDao()

        CoroutineScope(Dispatchers.IO).launch {


            val tracks = trackDao.getAllTracks()



            Log.d("TEST_DB_TRACKS", "ALL: $tracks")
//            val test = listOf(
//                TrackEntity(0, "uri1", "t1", "a1", 1000),
//                TrackEntity(0, "uri2", "t2", "a2", 2000),
//                TrackEntity(0, "uri3", "t3", "a3", 3000)
//            )
//
////            //  1. ПОЛУЧАЕМ 3 ТРЕКА ИЗ ТЕЛЕФОНА
//            val tracksFromPhone = getFirst3Videos(context)
//
//            // 2. ЗАПИСЫВАЕМ В ROOM
//            trackDao.insertAll(tracksFromPhone)
//////
//            Log.d("TEST_DB_TRACKS", "INSERTED: ${tracksFromPhone.size}")
//            Log.d("TEST_DB_DELETE", "$tracks")
//            db.playlistTrackDao().clearRelations()
//            db.trackDao().clearTracks()
//            db.playlistDao().clearPlaylists()

//            test.forEach { trackDao.insertTrack(it) }

            tracks.forEach {
                Log.d(
                    "TEST_DB_TRACK",
                    "Track: ${it.id} / ${it.uri}"
                )
            }
        }
    }

    private fun getFirst3Videos(context: Context): List<TrackEntity> {

        val result = mutableListOf<TrackEntity>()

        val uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI

        val projection = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.DURATION
        )

        val cursor = context.contentResolver.query(
            uri,
            projection,
            null,
            null,
            null
        )
        Log.d("TEST_DB_VIDEO_DEBUG", "cursor = $cursor")
        Log.d("TEST_DB_VIDEO", "cursor count = ${cursor?.count}")

        cursor?.use {

            val idCol = it.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
            val nameCol = it.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
            val durationCol = it.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)

            var count = 0

            while (it.moveToNext() && count < 3) {

                val id = it.getLong(idCol)

                val contentUri = ContentUris.withAppendedId(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    id
                )
                Log.d("TEST_DB_VIDEO_DEBUG", "ID = $id")
                Log.d("TEST_DB_VIDEO_DEBUG", "URI = $contentUri")
                result.add(
                    TrackEntity(
                        uri = contentUri.toString(),
                        title = it.getString(nameCol) ?: "Unknown",
                        artist = "Video",
                        duration = it.getLong(durationCol)
                    )
                )

                count++
            }
            Log.d("TEST_DB_VIDEO_DEBUG", "RESULT SIZE = ${result.size}")
        }

        return result
    }
}