package com.example.lab3.ui.music

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MusicViewModel : ViewModel() {

    var mediaPlayer: MediaPlayer? = null
    var current_song: Int = -1
    lateinit var runnable: Runnable
    var handler = Handler(Looper.getMainLooper())
}