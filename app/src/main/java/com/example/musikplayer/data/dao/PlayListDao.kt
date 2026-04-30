package com.example.musikplayer.data.dao


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.musikplayer.data.entityes.PlayListTrackEntity

import com.example.musikplayer.data.entityes.PlaylistEntity
@Dao
interface PlaylistDao {

    @Insert
    suspend fun insertPlaylist(playlist: PlaylistEntity): Long

    @Query("SELECT * FROM playlists")
    suspend fun getAllPlaylists(): List<PlaylistEntity>

    @Query("DELETE FROM playlists")
    suspend fun clearPlaylists()


}