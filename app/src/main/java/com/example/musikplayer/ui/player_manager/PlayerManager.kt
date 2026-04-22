package com.example.musikplayer.ui.player_manager

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer

private val media3: Any? = null

class PlayerManager(context: Context) {

    private val player = ExoPlayer.Builder(context).build()
    fun getPlayer(): ExoPlayer = player

    fun setMedia(uri: String) {
        val mediaItem = MediaItem.fromUri(uri)
        player.setMediaItem(mediaItem)
        player.prepare()
    }

    fun play() {
        player.play()
    }

    fun pause() {
        player.pause()
    }

    fun release() {
        player.release()
    }


}