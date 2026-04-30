package com.example.musikplayer.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


import com.example.musikplayer.data.entityes.PlayListTrackEntity
import com.example.musikplayer.data.entityes.TrackEntity

@Dao
interface PlaylistTrackDao {

    @Insert
    suspend fun insert(playlistTrack: PlayListTrackEntity)

//    @Query("""
//        SELECT * FROM playlist_tracks
//        WHERE playlistId = :playlistId
//        ORDER BY position ASC
//    """)
//    suspend fun getByPlaylist(playlistId: Long): List<PlayListTrackEntity>
    @Query("""
        SELECT t.* FROM tracks t
        INNER JOIN playlist_tracks pt ON t.id = pt.trackId
        WHERE pt.playlistId = :playlistId
        ORDER BY pt.position ASC
    """)
    suspend fun getByPlaylist(playlistId: Long): List<TrackEntity>

    @Query("DELETE FROM playlist_tracks")
    suspend fun clearRelations()

    @Query("SELECT * FROM playlist_tracks WHERE playlistId = :playlistId ORDER BY position")
    suspend fun getPlaylistTrackRefs(playlistId: Long): List<PlayListTrackEntity>
}