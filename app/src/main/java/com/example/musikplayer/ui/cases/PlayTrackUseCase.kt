package com.example.musikplayer.ui.cases

import androidx.media3.exoplayer.ExoPlayer
import com.example.musikplayer.ui.repository.PlayerRepository

class PlayTrackUseCase(
    private val repository: PlayerRepository
) {

    fun play(uri: String) {
        repository.play(uri)
    }

    fun pause() {
        repository.pause()
    }

    fun getPlayer(): ExoPlayer = repository.getPlayer()
}