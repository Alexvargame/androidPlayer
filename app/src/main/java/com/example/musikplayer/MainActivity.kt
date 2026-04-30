package com.example.musikplayer

import android.os.Bundle
import androidx.navigation.compose.rememberNavController

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import android.content.Intent
import android.net.Uri
import android.util.Log

import com.example.musikplayer.cases.PlayTrackUseCase
import com.example.musikplayer.data.AppDatabase
import com.example.musikplayer.player_manager.PlayerManager
import com.example.musikplayer.repository.PlayerRepository
import com.example.musikplayer.ui.theme.MusicPlayerTheme
import com.example.musikplayer.repository.MusicRepository

import com.example.musikplayer.add_data.*
import com.example.musikplayer.cases.GetPlaylistWithTracksUseCase
import com.example.musikplayer.repository.PlaylistRepository
import com.example.musikplayer.ui.navigation.appNavGraph


import com.example.musikplayer.viewModel.PlayerViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("TEST", "APP STARTED")
        Log.d("TEST_DB_PERMISSION_DEBUG", "onCreate START")

//        val granted = checkPermission()
//        Log.d("TEST_DB_PERMISSION_DEBUG", "GRANTED = $granted")
//
//        if (!granted) {
//            Log.d("TEST_DB_PERMISSION_DEBUG", "REQUESTING PERMISSION")
//            requestPermission()
//        }
//        if (Build.VERSION.SDK_INT >= 33) {
//            requestPermissions(arrayOf(Manifest.permission.READ_MEDIA_VIDEO), 100)
//        }
        Read_tracks(this).readTracks()
        Read_playlistts(this).readplaylists()

        enableEdgeToEdge()
//        if (!checkPermission()) {
//            requestPermission() // CHANGED: добавлен runtime permission check
//        }
        val uri = when (intent?.action) {
            Intent.ACTION_VIEW -> intent.data
            Intent.ACTION_SEND -> intent.getParcelableExtra<Uri>(Intent.EXTRA_STREAM)
            else -> null
        }
        Log.d("TEST_DB_INTENT_DEBUG", "URI = $uri")
        val db = AppDatabase.getInstance(this)
        val musicRepository = MusicRepository(
            db.playlistDao(),
            db.trackDao(),
            db.playlistTrackDao()
        )
        val playlistRepository = PlaylistRepository(
            db.trackDao(),
            db.playlistDao(),
            db.playlistTrackDao()
        )
        setContent {
            MusicPlayerTheme {

                val getPlaylistTracksUseCase = GetPlaylistWithTracksUseCase(playlistRepository)
                val playerViewModel = PlayerViewModel(
                    useCase = PlayTrackUseCase(
                        repository = PlayerRepository(
                            playerManager = PlayerManager(this)
                        )
                    ),
                    musicRepository = musicRepository,
                    getPlaylistTracksUseCase = getPlaylistTracksUseCase
                )
                // 👉 ЕСЛИ ПРИШЁЛ ФАЙЛ — СРАЗУ ИГРАЕМ
                if (uri != null) {
                    playerViewModel.play(uri.toString())
                }
                val navController = rememberNavController()
                appNavGraph(navController, playerViewModel)
            }
        }
    }
}

@PreviewScreenSizes
@Composable
fun MusikPlayerApp() {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach {
                item(
                    icon = {
                        Icon(
                            it.icon,
                            contentDescription = it.label
                        )
                    },
                    label = { Text(it.label) },
                    selected = it == currentDestination,
                    onClick = { currentDestination = it }
                )
            }
        }
    ) {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Greeting(
                name = "Android",
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

enum class AppDestinations(
    val label: String,
    val icon: ImageVector,
) {
    HOME("Home", Icons.Default.Home),
    FAVORITES("Favorites", Icons.Default.Favorite),
    PROFILE("Profile", Icons.Default.AccountBox),
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MusicPlayerTheme {
        Greeting("Android")
    }
}
