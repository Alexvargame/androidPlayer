package com.example.musikplayer.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.material3.AlertDialogDefaults.containerColor
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Text



import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role.Companion.Checkbox
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material3.Checkbox

import com.example.musikplayer.viewModel.PlaylistViewModel

@Composable
fun PlaylistScreen(
    navController: NavController,
    playlistViewModel: PlaylistViewModel
) {

    val bg = Color(0xFF0F0F12)
    val card = Color(0xFF1C1C22)
    val accent = Color(0xFF1DB954)


    val playlists = playlistViewModel.playlists
    LaunchedEffect(Unit) {
        playlistViewModel.loadPlaylists()
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
            .padding(16.dp)
    ) {

        // 🔙 BACK
        Button(
            onClick = { navController.navigate("home") },
            colors = ButtonDefaults.buttonColors(containerColor = card)
        ) {
            Text("← Назад", color = Color.White)
        }

        Spacer(Modifier.height(12.dp))

        LazyColumn {
            items(playlists) { playlist ->

                val selected = playlistViewModel.selectedPlaylistId == playlist.id

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            playlistViewModel.selectPlaylist(playlist.id)
                        }
                        .padding(8.dp)
                ) {
                    Text(
                        text = playlist.name,
                        color = if (selected) Color.Green else Color.White
                    )
                }
            }
        }

        // 🔻 BOTTOM BAR
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            Button(
                onClick = {
                    val id = playlistViewModel.selectedPlaylistId
                    if (id != null) {
                        navController.navigate("home?playlistId=$id")
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = card)
            ) {
                Text("Выбрать", color = Color.White)
            }

            Button(
                onClick = {navController.navigate("create_playlist_screen") },
                colors = ButtonDefaults.buttonColors(containerColor = accent)
            ) {
                Text("Создать", color = Color.White)
            }
        }
    }
}