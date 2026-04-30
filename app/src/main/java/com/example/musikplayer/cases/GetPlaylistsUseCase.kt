package com.example.musikplayer.cases

import com.example.musikplayer.data.entityes.PlaylistEntity
import com.example.musikplayer.repository.PlaylistRepository

class GetPlaylistsUseCase(
    private val repository: PlaylistRepository
) {
    suspend operator fun invoke(): List<PlaylistEntity> {
        return repository.getPlaylists()
    }
}
