package com.example.lab3.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lab3.R
import com.example.lab3.data.Advise
import com.example.lab3.databinding.ItemAdviseBinding

class AdviseAdapter(val itemClick: (Bundle) -> Unit) :
    RecyclerView.Adapter<AdviseAdapter.AdviseHolder>() {

    private var moodsList = arrayOf("Calm", "Relaxed", "Focused", "Excited")
    private val adviseList = arrayOf(
        Advise("Calm", "Calm Course", "Learn to control your mind, and not lose emotional control.", R.drawable.motivation_0),
        Advise("Relaxed", "Relax Course", "Learn to rest and relax your mind.", R.drawable.motivation_0),
        Advise("Relaxed", "Relax Course", "Releasing your feelings is all part of the therapy.", R.drawable.motivation_1),
        Advise("Focused", "Focus Course", "Learn to focus on your goal.", R.drawable.motivation_0),
        Advise("Excited", "Excitement Course", "Learn to enjoy achievements.", R.drawable.motivation_0),
        Advise("Excited", "Excitement Course", "Learn to deal with excessive excitement.", R.drawable.motivation_1)
    )
    private val chosenAdvise = arrayListOf<Advise>()

    inner class AdviseHolder(val binding: ItemAdviseBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdviseHolder {
        val binding = ItemAdviseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdviseHolder(binding)
    }

    override fun onBindViewHolder(holder: AdviseHolder, position: Int) {
        with(holder) {
            binding.itemTitle.text = chosenAdvise[position].title
            binding.itemDescription.text = chosenAdvise[position].description
            binding.adviseImage.setImageResource(chosenAdvise[position].image)
            binding.button.setOnClickListener {
                val bundle = Bundle()
                bundle.putSerializable("advise", chosenAdvise[position])
                itemClick(bundle)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(mood: String){
        chosenAdvise.clear()
        adviseList.forEach {
            if (it.mood == mood){
                chosenAdvise.add(it)
            }
        }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return chosenAdvise.size
    }
}