package com.example.lab3.ui.music

import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.lab3.R
import com.example.lab3.databinding.FragmentMusicBinding
import com.example.lab3.ui.home.HomeViewModel

class MusicFragment : Fragment() {

    val map = mapOf("Calm" to 0, "Relaxed" to 1, "Focused" to 2, "Excited" to 3, "No information" to 4)
    val musicName = listOf<String>("Calm music", "Relaxed music", "Focused music", "Excited music", "Standart music")
    private val musicViewModel: MusicViewModel by activityViewModels()
    private val homeViewModel: HomeViewModel by activityViewModels()
    private val binding: FragmentMusicBinding by lazy {
        FragmentMusicBinding.inflate(
            layoutInflater
        )
    }
   // lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        homeViewModel.user.observe(viewLifecycleOwner){
            if (musicViewModel.current_song != map[it.mood]){
                musicViewModel.current_song = map[it.mood]!!
                musicViewModel.mediaPlayer?.reset()
                musicViewModel.mediaPlayer = null
            }
            setMediaPlayer()
            binding.seekBar.max = musicViewModel.mediaPlayer!!.duration
            setHandle()
        }

        binding.start.setOnClickListener {
            if (!musicViewModel.mediaPlayer!!.isPlaying){
                musicViewModel.mediaPlayer!!.start()
                binding.start.setImageResource(R.drawable.ic_baseline_pause_24)
            }else{
                musicViewModel.mediaPlayer!!.pause()
                binding.start.setImageResource(R.drawable.ic_baseline_play_arrow_24)
            }
        }

        binding.seekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, pos: Int, changed: Boolean) {
                if(changed){
                    musicViewModel.mediaPlayer!!.seekTo(pos)
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })

        binding.next.setOnClickListener {
            musicViewModel.current_song = musicViewModel.current_song.inc()
            if (musicViewModel.current_song == 5) musicViewModel.current_song = 0
            musicViewModel.mediaPlayer!!.reset()
            musicViewModel.mediaPlayer = null
            binding.start.setImageResource(R.drawable.ic_baseline_play_arrow_24)
            setMediaPlayer()
        }

        binding.previous.setOnClickListener {
            musicViewModel.current_song = musicViewModel.current_song.dec()
            if (musicViewModel.current_song == -1) musicViewModel.current_song = 4
            musicViewModel.mediaPlayer!!.reset()
            musicViewModel.mediaPlayer = null
            binding.start.setImageResource(R.drawable.ic_baseline_play_arrow_24)
            setMediaPlayer()
        }

        return binding.root
    }

    fun setMediaPlayer(){
        if (musicViewModel.mediaPlayer == null) {
            when(musicViewModel.current_song){
                0 -> {
                    musicViewModel.mediaPlayer = MediaPlayer.create(context, R.raw.calm)
                    binding.seekBar.progress = 0
                    binding.musicTitle.text = musicName[musicViewModel.current_song]
                }
                1 -> {
                    musicViewModel.mediaPlayer = MediaPlayer.create(context, R.raw.relax)
                    binding.seekBar.progress = 0
                    binding.musicTitle.text = musicName[musicViewModel.current_song]
                }
                2 -> {
                    musicViewModel.mediaPlayer = MediaPlayer.create(context, R.raw.excited)
                    binding.seekBar.progress = 0
                    binding.musicTitle.text = musicName[musicViewModel.current_song]
                }
                3 -> {
                    musicViewModel.mediaPlayer = MediaPlayer.create(context, R.raw.focused)
                    binding.seekBar.progress = 0
                    binding.musicTitle.text = musicName[musicViewModel.current_song]
                }
                4 -> {
                    musicViewModel.mediaPlayer = MediaPlayer.create(context, R.raw.usual)
                    binding.seekBar.progress = 0
                    musicViewModel.current_song = 4
                    binding.musicTitle.text = musicName[musicViewModel.current_song]
                }
            }
            musicViewModel.mediaPlayer!!.setOnCompletionListener {
                binding.start.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                binding.seekBar.progress = 0
            }
        }
        else{
            binding.seekBar.progress = musicViewModel.mediaPlayer!!.currentPosition
            binding.musicTitle.text = musicName[musicViewModel.current_song]
            if (musicViewModel.mediaPlayer!!.isPlaying) binding.start.setImageResource(R.drawable.ic_baseline_pause_24)
            else binding.start.setImageResource(R.drawable.ic_baseline_play_arrow_24)
        }
    }

    fun setHandle(){
        musicViewModel.runnable = Runnable {
            if (musicViewModel.mediaPlayer != null) {
                binding.seekBar.progress = musicViewModel.mediaPlayer!!.currentPosition
                musicViewModel.handler.postDelayed(musicViewModel.runnable, 500)
            }
        }
        musicViewModel.handler.postDelayed(musicViewModel.runnable, 500)
    }
}