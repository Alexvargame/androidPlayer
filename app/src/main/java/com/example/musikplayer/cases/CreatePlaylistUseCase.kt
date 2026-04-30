package com.example.musikplayer.cases

import com.example.musikplayer.repository.PlaylistRepository

class CreatePlaylistUseCase(
    private val repository: PlaylistRepository
) {

    suspend operator fun invoke(
        name: String,
        trackIds: List<Long>
    ) {
        val playlistId = repository.createPlaylist(name)

        trackIds.forEachIndexed { index, trackId ->
            repository.addTrackToPlaylist(
                playlistId,
                trackId,
                index
            )
        }
    }
}