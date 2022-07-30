package com.example.lab3.ui.authoruzation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lab3.MainActivity
import com.example.lab3.R
import com.example.lab3.databinding.FragmentStartBinding
import com.example.lab3.ui.music.MusicViewModel
import java.util.ArrayList

class StartFragment : Fragment() {

    private val binding: FragmentStartBinding by lazy { FragmentStartBinding.inflate(layoutInflater) }
    private val musicViewModel: MusicViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        musicViewModel.mediaPlayer?.reset()

    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        (activity as MainActivity).binding.navView.visibility = View.GONE
        if (musicViewModel.mediaPlayer != null) {
            musicViewModel.mediaPlayer!!.reset()
            musicViewModel.mediaPlayer = null
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding.authoriztionButton.setOnClickListener {
            Navigation
                .findNavController(binding.root)
                .navigate(
                    R.id.action_start_to_registration,
                    null
                )
        }
        return binding.root
    }

}