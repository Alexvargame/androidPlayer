package com.example.musikplayer.repository

import androidx.media3.exoplayer.ExoPlayer
import com.example.musikplayer.player_manager.PlayerManager


class PlayerRepository(
    private val playerManager: PlayerManager
) {

    fun play(uri: String) {
        playerManager.setMedia(uri)
        playerManager.play()
    }

    fun pause() {
        playerManager.pause()
    }

    fun getPlayer(): ExoPlayer = playerManager.getPlayer()
}