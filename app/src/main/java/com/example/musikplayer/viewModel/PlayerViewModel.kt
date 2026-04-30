package com.example.musikplayer.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.exoplayer.ExoPlayer
import com.example.musikplayer.cases.GetPlaylistWithTracksUseCase


import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

import com.example.musikplayer.cases.PlayTrackUseCase
import com.example.musikplayer.data.entityes.TrackEntity
import com.example.musikplayer.repository.MusicRepository


class PlayerViewModel(
    private val useCase: PlayTrackUseCase,
    private val musicRepository: MusicRepository,
    private val getPlaylistTracksUseCase: GetPlaylistWithTracksUseCase

) : ViewModel() {

    sealed class PlayMode {
        data class Single(val uri: String) : PlayMode()
        data class Playlist(val tracks: List<TrackEntity>, val startIndex: Int = 0) : PlayMode()
    }

    var mode by mutableStateOf<PlayMode?>(null)
        private set

    private var currentIndex = 0

    val player: ExoPlayer = useCase.getPlayer()
    var isPlaying = mutableStateOf(false)
        private set

    private val _tracks = MutableStateFlow<List<TrackEntity>>(emptyList())
    val tracks: StateFlow<List<TrackEntity>> = _tracks


    fun playSingle(uri: String) {
        mode = PlayMode.Single(uri)
        play(uri)
    }

    fun playPlaylist(playlistId: Long) {
        viewModelScope.launch {
            val tracks = getPlaylistTracksUseCase(playlistId)   // возвращает List<TrackEntity>
            tracks.firstOrNull()?.uri?.let { play(it) }        // uri есть у TrackEntity
        }
    }
    fun play(uri: String) {
        useCase.play(uri)
        isPlaying.value = true
    }

    fun pause() {
        useCase.pause()
        isPlaying.value = false
    }

    fun toggle(uri: String) {
        Log.e("URI", "$uri")
        Log.e("isPlay", "$isPlaying")
        if (isPlaying.value) {
            pause()
        } else {
            play(uri)
        }
    }

    fun loadPlaylist(playlistId: Long) {
        viewModelScope.launch {
            _tracks.value = musicRepository.getPlaylistTracksOrdered(playlistId)
        }
    }
}