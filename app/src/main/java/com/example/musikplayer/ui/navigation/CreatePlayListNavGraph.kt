package com.example.musikplayer.ui.navigation

import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.musikplayer.cases.CreatePlaylistUseCase
import com.example.musikplayer.cases.GetPlaylistsUseCase
import com.example.musikplayer.cases.GetTracksUseCase
import com.example.musikplayer.data.AppDatabase
import com.example.musikplayer.repository.PlaylistRepository
import com.example.musikplayer.repository.factoryes.PlaylistViewModelFactory
import com.example.musikplayer.ui.screen.CreatePlaylistScreen
import com.example.musikplayer.viewModel.PlaylistViewModel


fun NavGraphBuilder.createPlayListNavGraph(
    navController: NavHostController,

) {
    composable("create_playlist_screen") {
        val context = LocalContext.current
        val db = AppDatabase.getInstance(context)

        val repository = PlaylistRepository(
            db.trackDao(),
            db.playlistDao(),
            db.playlistTrackDao()
        )

        val getTracksUseCase = GetTracksUseCase(repository)
        val createPlaylistUseCase = CreatePlaylistUseCase(repository)
        val getPlaylistsUseCase = GetPlaylistsUseCase(repository)

        val factory = PlaylistViewModelFactory(
            getTracksUseCase,
            createPlaylistUseCase,
            getPlaylistsUseCase

        )

        val playListViewModel: PlaylistViewModel = viewModel(factory = factory)

        CreatePlaylistScreen(
            navController = navController,
            playListViewModel = playListViewModel
        )
    }
}