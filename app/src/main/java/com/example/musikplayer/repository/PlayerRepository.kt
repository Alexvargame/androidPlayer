package com.example.musikplayer.repository

import android.net.Uri
import androidx.media3.exoplayer.ExoPlayer
import com.example.musikplayer.player_manager.PlayerManager


class PlayerRepository(
    private val playerManager: PlayerManager
) {

    fun setQueue(uris: List<Uri>, startIndex: Int) {
        playerManager.setQueue(uris, startIndex)
    }

    fun next() {
        playerManager.next()
    }

    fun prev() {
        playerManager.prev()
    }
    fun play(uri: String) {
        playerManager.setMedia(uri)
        playerManager.play()
    }

    fun playList() {
        playerManager.play()   // продолжает текущий трек
    }

    fun pause() {
        playerManager.pause()
    }

    fun getPlayer(): ExoPlayer = playerManager.getPlayer()
}