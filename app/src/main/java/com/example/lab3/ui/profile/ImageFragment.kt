package com.example.lab3.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.lab3.MainActivity
import com.example.lab3.R
import com.example.lab3.databinding.FragmentImageBinding
import com.example.lab3.databinding.FragmentProfileBinding
import com.example.lab3.ui.home.HomeViewModel

class ImageFragment: Fragment() {

    private val binding: FragmentImageBinding by lazy {
        FragmentImageBinding.inflate(
            layoutInflater
        )
    }
    private val homeViewModel: HomeViewModel by activityViewModels()
    var index: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as MainActivity).binding.navView.visibility = View.GONE
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding.apply {
            val path = arguments?.getString("path")
            index = arguments?.getInt("index")
            Glide.with(imageDetail)
                .load(path)
                .into(imageDetail)
        }

        binding.buttonClose.setOnClickListener {
            Navigation
                .findNavController(binding.root)
                .popBackStack(R.id.navigation_profile, false)
        }

        binding.buttonDelete.setOnClickListener {
            index?.let { it1 -> homeViewModel.user.value?.list?.removeAt(it1) }
            homeViewModel.saveList()
            Navigation
                .findNavController(binding.root)
                .popBackStack(R.id.navigation_profile, false)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as MainActivity).binding.navView.visibility = View.VISIBLE
    }
}