package com.example.musikplayer.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musikplayer.cases.CreatePlaylistUseCase
import com.example.musikplayer.cases.GetPlaylistWithTracksUseCase
import com.example.musikplayer.cases.GetPlaylistsUseCase
import com.example.musikplayer.cases.GetTracksUseCase
import com.example.musikplayer.data.entityes.TrackEntity
import com.example.musikplayer.data.entityes.PlaylistEntity
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val getTracksUseCase: GetTracksUseCase,
    private val createPlaylistUseCase: CreatePlaylistUseCase,
    private val getPlaylistsUseCase: GetPlaylistsUseCase,

) : ViewModel() {


    var tracks by mutableStateOf<List<TrackEntity>>(emptyList())
        private set

    var selectedTracks by mutableStateOf<Set<Long>>(emptySet())
        private set

    var playlists by mutableStateOf<List<PlaylistEntity>>(emptyList())
        private set
    var selectedPlaylistId by mutableStateOf<Long?>(null)
        private set
    fun loadTracks() {
        Log.d("TRACE_CALL", "LOAD TRACKS -> " + Throwable().stackTraceToString())
        viewModelScope.launch {
            tracks = getTracksUseCase()
        }
    }

    fun loadPlaylists() {
        viewModelScope.launch {
            playlists = getPlaylistsUseCase()
        }
    }

    fun selectPlaylist(id: Long) {
        selectedPlaylistId = id
    }

    fun toggleTracks(id: Long) {
        Log.d("SELECT_DEBUG", "TOGGLE START id=$id")
        selectedTracks = if (selectedTracks.contains(id)) {
            selectedTracks - id          // удаляем
        } else {
            selectedTracks + id          // добавляем
        }
        Log.d("SELECT_DEBUG", "STATE NOW=$selectedTracks")
    }

    fun createPlaylist() {
        viewModelScope.launch {
            createPlaylistUseCase("My Playlist", selectedTracks.toList())
            selectedTracks = emptySet()
        }
    }
}