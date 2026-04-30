package com.example.musikplayer.ui.navigation

import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.musikplayer.cases.CreatePlaylistUseCase
import com.example.musikplayer.cases.GetPlaylistWithTracksUseCase
import com.example.musikplayer.cases.GetPlaylistsUseCase
import com.example.musikplayer.cases.GetTracksUseCase
import com.example.musikplayer.data.AppDatabase
import com.example.musikplayer.repository.PlaylistRepository
import com.example.musikplayer.repository.factoryes.PlaylistViewModelFactory
import com.example.musikplayer.ui.screen.PlaylistScreen
import com.example.musikplayer.viewModel.PlaylistViewModel


fun NavGraphBuilder.playListNavGraph(
    navController: NavHostController,
    ) {
    composable("playlist_screen") {
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
            getPlaylistsUseCase,

        )
        val playListViewModel: PlaylistViewModel = viewModel(factory = factory)

        PlaylistScreen(navController,playListViewModel )
    }
}