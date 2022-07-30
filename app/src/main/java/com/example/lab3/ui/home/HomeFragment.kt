package com.example.lab3.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
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
import com.example.lab3.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by activityViewModels()
    private val binding: FragmentHomeBinding by lazy { FragmentHomeBinding.inflate(layoutInflater) }
    private val moodRecycler: RecyclerView by lazy { binding.moodRecycler }
    lateinit var moodAdapter: MoodAdapter
    private val adviseRecycler: RecyclerView by lazy { binding.adviseRecycler }
    lateinit var adviseAdapter: AdviseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        binding.apply {
            if (checkInternetConnection()) {
                val hash = arguments?.getString("user")
                if (hash != null) {
                    homeViewModel.getUser(hash)
                }
            } else Toast.makeText(context, "No Internet connection", Toast.LENGTH_SHORT).show()
        }
        (activity as MainActivity).binding.navView.visibility = View.VISIBLE
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        adviseAdapter = AdviseAdapter(){
            Navigation
                .findNavController(binding.root)
                .navigate(
                    R.id.action_home_to_advise,
                    it,
                    null
                )
        }
        adviseRecycler.adapter = adviseAdapter
        adviseRecycler.layoutManager = LinearLayoutManager(context)

        moodAdapter = MoodAdapter(context!!) {
            homeViewModel.user.value!!.mood = it
            adviseAdapter.setList(it)
            homeViewModel.saveMood()
            binding.lastMood.text = "Your last mood was ${homeViewModel.user.value?.mood}"
        }
        moodRecycler.adapter = moodAdapter
        moodRecycler.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

        homeViewModel.user.observe(viewLifecycleOwner) {
            binding.userHello.text = "Welcome back, ${homeViewModel.user.value?.name}"
            binding.lastMood.text = "Your last mood was ${homeViewModel.user.value?.mood}"
            if (it.mood != "No infromation")
                adviseAdapter.setList(it.mood)
        }

        return binding.root
    }

    private fun checkInternetConnection(): Boolean {
        val connection =
            activity?.applicationContext?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connection.activeNetwork != null
    }
}