package com.example.musikplayer.ui.screen
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.ui.PlayerView



import com.example.musikplayer.ui.viewModel.PlayerViewModel




@Composable
fun PlayerScreen(
    playerViewModel: PlayerViewModel
) {
    val bg = Color(0xFF0F0F12)
    val card = Color(0xFF1C1C22)
    val accent = Color(0xFF1DB954)
    val context = LocalContext.current

    var currentUri by remember { mutableStateOf("") }
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

        Spacer(Modifier.weight(1f))

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

//            Button(
//                onClick = { playerViewModel.toggle("file:///storage/emulated/0/DCIM/V.mp4")},
//                colors = ButtonDefaults.buttonColors(containerColor = accent),
//                shape = RoundedCornerShape(50)
//            ) {
//                Text(
//                    if (playerViewModel.isPlaying.value) "⏸" else "▶"
//                )
//            }

            // ▶/⏸ Play/Pause (с иконкой, которая меняется)
            Button(
                onClick = { playerViewModel.toggle(currentUri) },
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



