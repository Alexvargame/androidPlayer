package com.example.musikplayer.viewModel

import android.util.Log
import android.net.Uri
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

    val player: ExoPlayer = useCase.getPlayer()
    var isPlaying = mutableStateOf(false)
        private set

    private val _tracks = MutableStateFlow<List<TrackEntity>>(emptyList())
    val tracks: StateFlow<List<TrackEntity>> = _tracks

    private val _playlistTracks = MutableStateFlow<List<TrackEntity>>(emptyList())
    val playlistTracks: StateFlow<List<TrackEntity>> = _playlistTracks

    private val _currentTrackUri = MutableStateFlow<String?>(null)
    val currentTrackUri: StateFlow<String?> = _currentTrackUri
    private var currentPlaylistTracks: List<TrackEntity> = emptyList()
    private var currentIndex: Int = 0

    fun playSingle(uri: String) {
        mode = PlayMode.Single(uri)
        play(uri)
    }
    fun playPlaylist(playlistId: Long) {
        viewModelScope.launch {
            Log.d("PLAYLIST_DEBUG", "playPlaylist: playlistId=$playlistId, START")
            val tracks = getPlaylistTracksUseCase(playlistId)
            Log.d("PLAYLIST_DEBUG", "playPlaylist: tracks.size=${tracks.size}")
            _playlistTracks.value = tracks
            currentPlaylistTracks = tracks          // добавить
            currentIndex = 0
            val uris = tracks.map { Uri.parse(it.uri) }
//            Log.d("PLAYLIST_DEBUG", "playPlaylist: uris.size=${uris.size}")
            Log.d("PLAYLIST_DEBUG", "currentIndex=${currentIndex}")

            useCase.setQueue(uris, 0)
//            Log.d("PLAYLIST_DEBUG", "playPlaylist: setQueue done, queue size=${uris.size}")
            _currentTrackUri.value = tracks[currentIndex].uri
            useCase.playList()  // или useCase.resume()

            Log.d("PLAYLIST_DEBUG", "playPlaylist: setQueue done, queue size=${uris.size}," +
                    " currentTrackUri=${_currentTrackUri.value}")
        }
    }

//    fun playPlaylist(playlistId: Long) {
//        viewModelScope.launch {
//            val tracks = getPlaylistTracksUseCase(playlistId)
//            _playlistTracks.value = tracks// возвращает List<TrackEntity>
//            currentPlaylistTracks = tracks          // добавить
//            currentIndex = 0
//            tracks.firstOrNull()?.uri?.let { play(it) }        // uri есть у TrackEntity
//        }
//    }
    fun play(uri: String) {
        Log.d("TRACK", "$uri")
        useCase.play(uri)
        isPlaying.value = true
        _currentTrackUri.value = uri
    }

    fun pause() {
        useCase.pause()
        isPlaying.value = false
    }
    fun nextTrack() {
        Log.d("PLAYLIST_DEBUG", "nextTrack: вызывается")
        currentIndex++
        _currentTrackUri.value = currentPlaylistTracks[currentIndex].uri
        useCase.next()
        Log.d("PLAYLIST_DEBUG", "nextTrack: выполнен useCase.next()")
    }

    fun prevTrack() {
        Log.d("PLAYLIST_DEBUG", "prevTrack: вызывается")
        currentIndex--
        _currentTrackUri.value = currentPlaylistTracks[currentIndex].uri
        useCase.prev()
        Log.d("PLAYLIST_DEBUG", "prevTrack: выполнен useCase.prev()")
    }
    fun togglePlayPause() {
        Log.d("PLAYLIST_DEBUG", "togglePlayPause: isPlaying=${isPlaying.value}")
        if (isPlaying.value) {
            useCase.pause()
            isPlaying.value = false
            Log.d("PLAYLIST_DEBUG", "togglePlayPause: pause")
        } else {
            useCase.playList()
            isPlaying.value = true
            Log.d("PLAYLIST_DEBUG", "togglePlayPause: play")
        }
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
    fun clearPlaylist() {
        Log.d("PLAYLIST_DEBUG", "clearPlaylist вызывается, очищаем _playlistTracks")
        _playlistTracks.value = emptyList()
    }
}