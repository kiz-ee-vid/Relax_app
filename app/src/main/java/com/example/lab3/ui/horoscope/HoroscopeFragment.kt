package com.example.lab3.ui.horoscope

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lab3.databinding.FragmentHoroscopeBinding
import com.example.lab3.ui.home.MoodAdapter
import kotlin.math.pow

class HoroscopeFragment : Fragment() {

    private val horoscopeViewModel: HoroscopeViewModel by activityViewModels()
    private val binding: FragmentHoroscopeBinding by lazy {
        FragmentHoroscopeBinding.inflate(
            layoutInflater
        )
    }
    private val horoscopeRecycler: RecyclerView by lazy { binding.goroscopRecycler }
    lateinit var horoscopeAdapter: HoroscopeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding.horoscopAnswer.visibility = View.GONE
        horoscopeAdapter = HoroscopeAdapter(context!!) {
            horoscopeViewModel.getHoroscope(it)
        }
        horoscopeRecycler.adapter = horoscopeAdapter
        horoscopeRecycler.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

        horoscopeViewModel.horoscope.observe(viewLifecycleOwner) {
            binding.sign.text = it?.current_date
            binding.date.text = it?.date_range
            binding.description.text = it?.description
            if(horoscopeViewModel.horoscope.value != null)
            binding.horoscopAnswer.visibility = View.VISIBLE
            binding.horoscopAnswer.setOnClickListener {
                binding.horoscopAnswer.visibility = View.GONE
            }
        }

        binding.getBmi.setOnClickListener {
            val growth = binding.growth.text.toString()
            val weight = binding.weight.text.toString()
            if (!growth.isNullOrEmpty() && !weight.isNullOrEmpty()){
                val result = weight.toInt() / (growth.toDouble() / 100).pow(2)
                binding.result.text = String.format("%.2f", result)
                when (result){
                    in 0.0..16.0 -> binding.resultDescription.text = "Body weight deficit"
                    in 16.0..18.5 -> binding.resultDescription.text = "Insufficient body weight"
                    in 18.5..25.0 -> binding.resultDescription.text = "Normal body weight"
                    in 25.0..30.0 -> binding.resultDescription.text = "Overweight (pre-obesity)"
                    in 30.0..35.0 -> binding.resultDescription.text = "Obesity of the 1st degree"
                    in 35.0..40.0 -> binding.resultDescription.text = "Obesity of the 2nd degree"
                    in 40.0..60.0 -> binding.resultDescription.text = "Obesity of the 3nd degree"
                    else -> binding.resultDescription.text = "Are you kidding?"
                }
            }
        }

        return binding.root
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        binding.weight.text.clear()
        binding.growth.text.clear()
    }

    override fun onDestroy() {
        horoscopeViewModel.horoscope.value = null
        binding.horoscopAnswer.visibility = View.GONE
        super.onDestroy()
    }

}