package com.example.musikplayer.data.dao


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

import com.example.musikplayer.data.entityes.TrackEntity
@Dao
interface TrackDao {

    @Insert
    suspend fun insertTrack(track: TrackEntity): Long

    @Insert
    suspend fun insertAll(tracks: List<TrackEntity>)

    @Query("SELECT * FROM tracks WHERE id IN (:ids)")
    suspend fun getTracksByIds(ids: List<Long>): List<TrackEntity>

    @Query("SELECT * FROM tracks")
    suspend fun getAllTracks(): List<TrackEntity>

    @Query("DELETE FROM tracks")
    suspend fun clearTracks()
}