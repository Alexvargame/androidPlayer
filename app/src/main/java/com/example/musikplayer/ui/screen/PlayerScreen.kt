package com.example.musikplayer.ui.screen
import android.content.Intent
import android.media.MediaMetadataRetriever
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
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
    val playlistTracks by playerViewModel.playlistTracks.collectAsState()
    val currenttrackUri by playerViewModel.currentTrackUri.collectAsState()

    var singleTrackTitle by remember { mutableStateOf<String?>(null) }
    var singleTrackArtist by remember { mutableStateOf<String?>(null) }
    val pickVideo = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            context.contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            playerViewModel.clearPlaylist()  // добавим этот метод ниже
            val retriever = MediaMetadataRetriever()
            try {
                retriever.setDataSource(context, uri)
                val title = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
                    ?: uri.lastPathSegment?.substringAfterLast('/') ?: "Unknown"
                val artist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST) ?: "Unknown artist"
                singleTrackTitle = title
                singleTrackArtist = artist
            } catch (e: Exception) {
                singleTrackTitle = uri.lastPathSegment?.substringAfterLast('/') ?: "Unknown"
                singleTrackArtist = "Unknown artist"
            } finally {
                retriever.release()
            }
            currentUri = uri.toString()
            playerViewModel.play(uri.toString())
        }
    }
    LaunchedEffect(playlistId) {
        playlistId?.let {
            singleTrackTitle = null
            singleTrackArtist = null
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

        if (playlistTracks.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            ) {
                items(playlistTracks) { track ->
                    val isCurrent = track.uri == currenttrackUri
                    Text(
                        text = track.title,
                        color = if(isCurrent) Color.Green else Color.White,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
        else {
            Text(
                text = singleTrackTitle ?: "Track name",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = singleTrackArtist ?: "Artist name",
                color = Color.Gray,
                fontSize = 14.sp
            )
        }
        Spacer(Modifier.height(16.dp))


        // 🎵 Название трека


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

        // 🎛 Кнопки
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            Button(
                onClick = { playerViewModel.prevTrack()},
                colors = ButtonDefaults.buttonColors(containerColor = card),
                shape = RoundedCornerShape(50)
            ) {
                Text("⏮", color = Color.White)
            }


            Button(

                onClick = {
                    if (playlistTracks.isNotEmpty()) {
                        // Режим плейлиста
                        playerViewModel.togglePlayPause()
                    } else if (currentUri.isNotEmpty()) {
                        // Режим одиночного файла
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
                onClick = { playerViewModel.nextTrack()},
                colors = ButtonDefaults.buttonColors(containerColor = card),
                shape = RoundedCornerShape(50)
            ) {
                Text("⏭", color = Color.White)
            }
        }
    }
}



