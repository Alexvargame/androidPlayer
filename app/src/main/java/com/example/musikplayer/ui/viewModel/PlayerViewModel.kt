package com.example.musikplayer.ui.viewModel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.media3.exoplayer.ExoPlayer
import com.example.musikplayer.ui.cases.PlayTrackUseCase

class PlayerViewModel(
    private val useCase: PlayTrackUseCase
) : ViewModel() {

    val player: ExoPlayer = useCase.getPlayer()
    var isPlaying = mutableStateOf(false)
        private set

    fun play(uri: String) {
        useCase.play(uri)
        isPlaying.value = true
    }

    fun pause() {
        useCase.pause()
        isPlaying.value = false
    }

    fun toggle(uri: String) {
        Log.e("URI", "$uri")
        Log.e("isPlay", "$isPlaying")
        if (isPlaying.value) {
            pause()
        } else {
            play(uri)
        }
    }
}