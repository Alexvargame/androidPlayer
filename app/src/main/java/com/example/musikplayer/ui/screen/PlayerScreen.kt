package com.example.musikplayer.ui.screen
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.ui.PlayerView

import androidx.navigation.NavController



import com.example.musikplayer.viewModel.PlayerViewModel





@Composable
fun PlayerScreen(
    navController: NavController,
    playerViewModel: PlayerViewModel,
    playlistId: Long? = null
) {
    val bg = Color(0xFF0F0F12)
    val card = Color(0xFF1C1C22)
    val accent = Color(0xFF1DB954)
    val context = LocalContext.current

//    var currentUri by remember { mutableStateOf("") }
    var currentUri by rememberSaveable { mutableStateOf("") }

//    val playlistTracks = playerViewModel.getCurrentPlaylistTracks()
    val pickVideo = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            context.contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            currentUri = uri.toString()
            playerViewModel.play(uri.toString())
        }
    }
    LaunchedEffect(playlistId) {
        playlistId?.let {
            playerViewModel.playPlaylist(it)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
            .padding(16.dp)
    ) {

        // 🎬 Видео / обложка
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    player = playerViewModel.player
                    useController = true
                }
            },
            update = { view ->
                view.player = playerViewModel.player
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .clip(RoundedCornerShape(20.dp))
        )

        Spacer(Modifier.height(16.dp))


        // 🎵 Название трека
        Text(
            text = "Track name",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Artist name",
            color = Color.Gray,
            fontSize = 14.sp
        )

        Button(
            onClick = {
                navController.navigate("playlist_screen")
            },
            colors = ButtonDefaults.buttonColors(containerColor = card),
            shape = RoundedCornerShape(50),
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("📃 Плейлисты", color = Color.White)
        }
        Spacer(Modifier.weight(1f))
//        LazyColumn(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(200.dp)
//        ) {
//            items(playlistTracks) { track ->
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .clickable {
//                            // переключиться на выбранный трек
//                            val newIndex = playlistTracks.indexOf(track)
//                            if (newIndex != -1) {
//                                // нужно добавить в ViewViewModel метод setCurrentIndex
//                                playerViewModel.setCurrentIndex(newIndex)
//                            }
//                        }
//                        .padding(8.dp)
//                ) {
//                    Text(
//                        text = track.title,
//                        color = if (track.uri == currentTrack?.uri) Color.Green else Color.White
//                    )
//                }
//            }
//        }
        // 🎛 Кнопки
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(containerColor = card),
                shape = RoundedCornerShape(50)
            ) {
                Text("⏮", color = Color.White)
            }


            Button(
                onClick = {
                    if (currentUri.isNotEmpty()) {
                        playerViewModel.toggle(currentUri)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = accent),
                shape = RoundedCornerShape(50)
            ) {
                Text(
                    if (playerViewModel.isPlaying.value) "⏸" else "▶",
                    color = Color.White
                )
            }

            // 📁 Выбрать видео
            Button(
                onClick = { pickVideo.launch(arrayOf("video/*")) },
                colors = ButtonDefaults.buttonColors(containerColor = card),
                shape = RoundedCornerShape(50)
            ) {
                Text("📁", color = Color.White)  // или текст "ВЫБРАТЬ"
            }

            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(containerColor = card),
                shape = RoundedCornerShape(50)
            ) {
                Text("⏭", color = Color.White)
            }
        }
    }
}



