package com.example.musikplayer.ui.repository

import androidx.media3.exoplayer.ExoPlayer
import com.example.musikplayer.ui.player_manager.PlayerManager


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