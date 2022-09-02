package com.example.lab3.widget.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.media.MediaBrowserServiceCompat
import com.example.lab3.R
import com.example.lab3.widget.AudioWidget
import kotlinx.coroutines.*
import java.io.IOException
import java.lang.Runnable

class MusicService : MediaBrowserServiceCompat() {

    val map =
        mapOf("Calm" to 0, "Relaxed" to 1, "Focused" to 2, "Excited" to 3, "No information" to 4)
    val musicName = listOf<String>(
        "Calm music",
        "Relaxed music",
        "Focused music",
        "Excited music",
        "Standart music"
    )
    var current_song: Int = 0
    lateinit var runnable: Runnable
    private lateinit var remoteViews: RemoteViews
    var handler = Handler(Looper.getMainLooper())
    private val notificationManager: NotificationManager by lazy { getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }
    private lateinit var mediaPlayer: MediaPlayer

    //    private val notificationBuilder: MusicNotificationBuilder by lazy {
//        MusicNotificationBuilder(
//            this,
//            channelId
//        )
//    }
    private var currFlipperState = ViewFlipperState.STOPPED

    internal enum class ViewFlipperState {
        STOPPED, PLAYING
    }

    override fun onCreate() {
        super.onCreate()
        /* let's wait until the debugger attaches */
        //android.os.Debug.waitForDebugger();
        Log.d(TAG, "onCreate()")
        startForeground(
            ONGOING_NOTIFICATION_ID, NotificationCompat.Builder(this, channelId)
                .setContentTitle("")
                .setContentText("").build()
        )
        remoteViews = AudioWidget.getRemoteViews(this@MusicService)
        setMediaPlayer()
        mediaPlayer.setOnCompletionListener {
            remoteViews.setImageViewResource(
                R.id.button_play_pause,
                R.drawable.ic_baseline_pause_24
            )
        }
    }

    private fun getPlaybackStateBuilder() = PlaybackStateCompat.Builder()
        .setActions(
            PlaybackStateCompat.ACTION_PLAY
                    or PlaybackStateCompat.ACTION_STOP
                    or PlaybackStateCompat.ACTION_PAUSE
                    or PlaybackStateCompat.ACTION_PLAY_PAUSE
                    or PlaybackStateCompat.ACTION_SKIP_TO_NEXT
                    or PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
                    or PlaybackStateCompat.ACTION_SET_SHUFFLE_MODE
        )


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        isRunning = true
        if (intent == null || intent.action == null) {
            return Service.START_STICKY
        } else {
            processStartCommand(intent)
            //    MediaButtonReceiver.handleIntent(mediaSession, intent)
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun processStartCommand(intent: Intent) {
        try {
            when (intent.action) {
                AudioWidget.ACTION_PLAY_PAUSE ->
                    if (mediaPlayer!!.isPlaying) pauseMusic()
                    else playMusic()
                AudioWidget.ACTION_STOP -> stopMusic()
                AudioWidget.ACTION_NEXT -> nextSong()
                AudioWidget.ACTION_PREVIOUS -> previousSong()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            stopMusic()
        }
    }

    override fun onDestroy() {
        Log.d(TAG, "DESTROY SERVICE")
        isRunning = false
        if (mediaPlayer!!.isPlaying) stopMusic()
        super.onDestroy()
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot? =
        BrowserRoot(javaClass.name, null)

    override fun onLoadChildren(
        parentId: String,
        result: Result<List<MediaBrowserCompat.MediaItem>>
    ) =
        result.sendResult(listOf())

    private fun updateUI(song: Int?, isPlaying: Boolean) {
        if (mediaPlayer.isPlaying) {
            remoteViews.setImageViewResource(
                R.id.button_play_pause,
                R.drawable.ic_baseline_pause_24
            )
        } else {
            remoteViews.setImageViewResource(
                R.id.button_play_pause,
                R.drawable.ic_baseline_play_arrow_24
            )
        }
        CoroutineScope(Dispatchers.Default).launch {
            // We need to use the RemoteViews generated by the MusicWidget, class to make sure we preserve the pending intents for the buttons.
            // Otherwise the widget's buttons can stop responding to touch events.
            //val remoteViews = MusicWidget.getRemoteViews(this@MusicService)
            currFlipperState = if (song != null) {
                remoteViews.setTextViewText(R.id.textViewTitle, musicName[song])
                remoteViews.setImageViewResource(R.id.ivAlbumArt, R.drawable.ic_horoscope)

                if (currFlipperState == ViewFlipperState.STOPPED) {
                    remoteViews.setDisplayedChild(
                        R.id.viewFlipper,
                        ViewFlipperState.PLAYING.ordinal
                    )
                }
                ViewFlipperState.PLAYING
            } else {
                if (currFlipperState == ViewFlipperState.PLAYING) {
                    remoteViews.setDisplayedChild(
                        R.id.viewFlipper,
                        ViewFlipperState.STOPPED.ordinal
                    )
                }
                ViewFlipperState.STOPPED
            }

            val thisWidget = ComponentName(this@MusicService, AudioWidget::class.java)
            val manager = AppWidgetManager.getInstance(this@MusicService)
            manager.updateAppWidget(thisWidget, remoteViews)

            // Create/Update a notification, to run the service in foreground
//            if (song != null) {
//                val notification = notificationBuilder
//                    .build(song, isPlaying)
//                notificationManager.notify(ONGOING_NOTIFICATION_ID, notification)
//            }
        }
    }

    // If earlier version channel ID is not used
    // https://developer.android.com/reference/android/support/v4/app/NotificationCompat.Builder.html#NotificationCompat.Builder(android.content.Context)
    private val channelId: String by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(
                "music_widget_service",
                "Music playback controls"
            )
        } else {
            // If earlier version channel ID is not used
            // https://developer.android.com/reference/android/support/v4/app/NotificationCompat.Builder.html#NotificationCompat.Builder(android.content.Context)
            "Default Channel"
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String): String {
        val channel =
            NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW)
        channel.lightColor = Color.BLUE
        channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        notificationManager.createNotificationChannel(channel)
        return channelId
    }

    @Throws(IOException::class)
    private fun playMusic() {
        Log.d(TAG, "PLAY")
        mediaPlayer.start()
        updateUI(current_song, true)
    }

    private fun pauseMusic() {
        Log.d(TAG, "PAUSE")
        if (mediaPlayer.isPlaying) mediaPlayer.pause()
        updateUI(current_song, false)
    }

    private fun stopMusic() {
        Log.d(TAG, "STOP MUSIC")
        isRunning = false
        mediaPlayer.stop()
        updateUI(null, false)
        stopForeground(true)
        stopSelf()
    }

    @Throws(IOException::class)
    private fun nextSong() {
        Log.d(TAG, "NEXT SONG")
        current_song = current_song.inc()
        if (current_song == 5) current_song = 0
        mediaPlayer.reset()
        setMediaPlayer()
        updateUI(current_song, false)
    }

    @Throws(IOException::class)
    private fun previousSong() {
        Log.d(TAG, "PREVIOUS SONG")
        current_song = current_song.dec()
        if (current_song == -1) current_song = 4
        mediaPlayer.reset()
        setMediaPlayer()
        updateUI(current_song, false)
    }

    private fun setMediaPlayer() {
        when (current_song) {
            0 -> {
                mediaPlayer = MediaPlayer.create(this, R.raw.calm)
                remoteViews.setTextViewText(R.id.textViewTitle, musicName[current_song])
            }
            1 -> {
                mediaPlayer = MediaPlayer.create(this, R.raw.relax)
                remoteViews.setTextViewText(R.id.textViewTitle, musicName[current_song])
            }
            2 -> {
                mediaPlayer = MediaPlayer.create(this, R.raw.excited)
                remoteViews.setTextViewText(R.id.textViewTitle, musicName[current_song])
            }
            3 -> {
                mediaPlayer = MediaPlayer.create(this, R.raw.focused)
                remoteViews.setTextViewText(R.id.textViewTitle, musicName[current_song])
            }
            4 -> {
                mediaPlayer = MediaPlayer.create(this, R.raw.usual)
                remoteViews.setTextViewText(R.id.textViewTitle, musicName[current_song])
            }
        }
    }

    companion object {
        @JvmField
        var isRunning = false
        private const val TAG = "Music Service"
        private const val ONGOING_NOTIFICATION_ID = 1
        private const val PLAYBACK_SPEED = 1.0f
    }
}