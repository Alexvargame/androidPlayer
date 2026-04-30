package com.example.musikplayer.repository.factoryes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.musikplayer.cases.CreatePlaylistUseCase
import com.example.musikplayer.cases.GetPlaylistWithTracksUseCase
import com.example.musikplayer.cases.GetTracksUseCase
import com.example.musikplayer.cases.GetPlaylistsUseCase
import com.example.musikplayer.viewModel.PlaylistViewModel

class PlaylistViewModelFactory(
    private val getTracksUseCase: GetTracksUseCase,
    private val createPlaylistUseCase: CreatePlaylistUseCase,
    private val getPlaylistsUseCase: GetPlaylistsUseCase,

) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PlaylistViewModel(
            getTracksUseCase,
            createPlaylistUseCase,
            getPlaylistsUseCase,
        ) as T
    }
}
