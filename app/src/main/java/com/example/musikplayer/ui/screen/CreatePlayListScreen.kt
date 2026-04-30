package com.example.musikplayer.ui.screen


import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.musikplayer.data.entityes.TrackEntity
import com.example.musikplayer.viewModel.PlaylistViewModel

@Composable
fun CreatePlaylistScreen(
    navController: NavController,
    playListViewModel: PlaylistViewModel
) {

    LaunchedEffect(Unit) {
        Log.d("TRACE_CALL", "LOAD TRACKS -> " + Throwable().stackTraceToString())
       playListViewModel.loadTracks()
    }

    val tracks = playListViewModel.tracks
    val selected = playListViewModel.selectedTracks

    Log.d("SELECT_DEBUG", "UI selected=$selected")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // 🔙 BACK
        Button(onClick = { navController.popBackStack() }) {
            Text("Назад")
        }

        Spacer(Modifier.height(12.dp))

        // 🎵 TRACK LIST
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(tracks) { track ->

//                val isSelected = selected[track.id] == true
                val isSelected = selected.contains(track.id)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            playListViewModel.toggleTracks(track.id)
                        }
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Checkbox(
                        checked = isSelected,
                        onCheckedChange = {
                            playListViewModel.toggleTracks(track.id)
                        }
                    )

                    Spacer(Modifier.width(8.dp))

                    Text(text = track.title)
                }
            }
        }

        // 🔻 ACTION BUTTONS
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            Button(onClick = {
                navController.popBackStack()
            }) {
                Text("Отмена")
            }

            Button(onClick = {
                playListViewModel.createPlaylist()
                navController.popBackStack()
            }) {
                Text("Создать")
            }
        }
    }
}