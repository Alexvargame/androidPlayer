package com.example.musikplayer.cases

import com.example.musikplayer.data.entityes.TrackEntity
import com.example.musikplayer.repository.PlaylistRepository


class GetPlaylistWithTracksUseCase(
    private val repository: PlaylistRepository
) {
    suspend operator fun invoke(playlistId: Long): List<TrackEntity> =
        repository.getPlaylistTracks(playlistId)
}