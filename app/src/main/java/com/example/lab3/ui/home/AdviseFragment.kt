package com.example.lab3.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.lab3.MainActivity
import com.example.lab3.R
import com.example.lab3.data.Advise
import com.example.lab3.databinding.FragmentAdviseBinding

class AdviseFragment: Fragment() {

    private val binding: FragmentAdviseBinding by lazy { FragmentAdviseBinding.inflate(layoutInflater) }
    private var advise: Advise? = null

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
            advise = arguments?.getSerializable("advise") as Advise
            advise?.let { adviseImage.setImageResource(it.image) }
            adviseTitle.text = advise?.title ?: "No information"
            adviseDescription.text = advise?.description ?: "No information"
            backButton.setOnClickListener {
                Navigation
                    .findNavController(binding.root)
                    .popBackStack(R.id.navigation_home, false)
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as MainActivity).binding.navView.visibility = View.VISIBLE
    }
}