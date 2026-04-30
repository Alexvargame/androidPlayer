package com.example.musikplayer.repository

import com.example.musikplayer.data.dao.PlaylistDao
import com.example.musikplayer.data.dao.PlaylistTrackDao
import com.example.musikplayer.data.dao.TrackDao
import com.example.musikplayer.data.entityes.PlaylistEntity
import com.example.musikplayer.data.entityes.TrackEntity
import com.example.musikplayer.data.entityes.PlayListTrackEntity

class PlaylistRepository(
    private val trackDao: TrackDao,
    private val playlistDao: PlaylistDao,
    private val playlistTrackDao: PlaylistTrackDao
) {

    suspend fun getTracks(): List<TrackEntity> {
        return trackDao.getAllTracks()
    }

    suspend fun createPlaylist(name: String): Long {
        return playlistDao.insertPlaylist(PlaylistEntity(name = name))
    }

    suspend fun addTrackToPlaylist(
        playlistId: Long,
        trackId: Long,
        position: Int
    ) {
        playlistTrackDao.insert(
            PlayListTrackEntity(
                playlistId = playlistId,
                trackId = trackId,
                position = position
            )
        )
    }
    suspend fun getPlaylists(): List<PlaylistEntity> {
        return playlistDao.getAllPlaylists()
    }

    suspend fun getPlaylistTracks(playlistId: Long): List<TrackEntity> {
        val refs = playlistTrackDao.getPlaylistTrackRefs(playlistId)  // нужен метод в PlaylistTrackDao
        val trackIds = refs.map { it.trackId }
        return trackDao.getTracksByIds(trackIds)
    }
}