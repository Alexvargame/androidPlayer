package com.example.musikplayer.player_manager

import android.content.Context
import android.net.Uri

import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer



class PlayerManager(context: Context) {

    private val player = ExoPlayer.Builder(context).build()

    private var queue: List<Uri> = emptyList()
    private var currentIndex: Int = -1
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

    fun next() {
        val nextIndex = currentIndex + 1
        if (nextIndex < queue.size) {
            playAt(nextIndex)
        }
    }

    fun prev() {
        val prevIndex = currentIndex - 1
        if (prevIndex >= 0) {
            playAt(prevIndex)
        }
    }
    fun playAt(index: Int) {
        if (index !in queue.indices) return

        currentIndex = index

        val mediaItem = MediaItem.fromUri(queue[index])
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
    }


    fun setQueue(list: List<Uri>, startIndex: Int = 0) {
        queue = list
        currentIndex = startIndex.coerceIn(list.indices)

        if (queue.isNotEmpty()) {
            playAt(currentIndex)
        }
    }

}