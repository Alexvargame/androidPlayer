package com.example.musikplayer.ui.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavType
import androidx.navigation.navArgument

import com.example.musikplayer.ui.screen.PlayerScreen

import com.example.musikplayer.viewModel.PlayerViewModel

@Composable
fun appNavGraph(navController: NavHostController, playerViewModel: PlayerViewModel,
                ) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {

            PlayerScreen (navController = navController, playerViewModel=playerViewModel, playlistId = null)
        }
        composable(
            "home?playlistId={playlistId}",
            arguments = listOf(navArgument("playlistId") { type = NavType.LongType })
        ) { backStackEntry ->
            val playlistId = backStackEntry.arguments?.getLong("playlistId")
            PlayerScreen(navController = navController, playerViewModel = playerViewModel, playlistId = playlistId)
        }
        playListNavGraph(
            navController = navController,
        )
        createPlayListNavGraph(
            navController = navController,
        )
    }
}

