package com.example.musikplayer.cases

import com.example.musikplayer.data.entityes.TrackEntity
import com.example.musikplayer.repository.PlaylistRepository

class GetTracksUseCase(
    private val repository: PlaylistRepository
) {
    suspend operator fun invoke(): List<TrackEntity> {
        return repository.getTracks()
    }
}