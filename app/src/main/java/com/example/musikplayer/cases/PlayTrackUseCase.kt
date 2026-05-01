package com.example.musikplayer.cases

import android.net.Uri
import androidx.media3.exoplayer.ExoPlayer
import com.example.musikplayer.repository.PlayerRepository

class PlayTrackUseCase(
    private val repository: PlayerRepository
) {

    fun play(uri: String) {
        repository.play(uri)
    }

    fun playList() {
        repository.playList()
    }

    fun pause() {
        repository.pause()
    }

    fun setQueue(uris: List<Uri>, startIndex: Int) {
        repository.setQueue(uris, startIndex)
    }

    fun next() {
        repository.next()
    }

    fun prev() {
        repository.prev()
    }
    fun getPlayer(): ExoPlayer = repository.getPlayer()
}