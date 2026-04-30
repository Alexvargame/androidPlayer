package com.example.musikplayer.repository

import com.example.musikplayer.data.dao.PlaylistDao
import com.example.musikplayer.data.dao.PlaylistTrackDao
import com.example.musikplayer.data.dao.TrackDao
import com.example.musikplayer.data.entityes.TrackEntity


class MusicRepository(
    private val playlistDao: PlaylistDao,
    private val trackDao: TrackDao,
    private val playlistTrackDao: PlaylistTrackDao
)  {

    suspend fun getPlaylistTracksOrdered(playlistId: Long): List<TrackEntity> {
        return playlistTrackDao.getByPlaylist(playlistId)
    }

}


