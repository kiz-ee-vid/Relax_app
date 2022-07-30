package com.example.lab3.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lab3.databinding.ItemMoodBinding

class MoodAdapter(var context: Context, val itemClick: (String) -> Unit) :
    RecyclerView.Adapter<MoodAdapter.MoodHolder>() {

    private var moodsList = arrayOf("Calm", "Relaxed", "Focused", "Excited")

    inner class MoodHolder(val binding: ItemMoodBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoodHolder {
        val binding = ItemMoodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MoodHolder(binding)
    }

    override fun onBindViewHolder(holder: MoodHolder, position: Int) {
        with(holder){
            val imgId = context.resources.getIdentifier("ic_mood_$position", "drawable", context.packageName)
            binding.moodIcon.setImageResource(imgId)
            binding.moodTitle.text = moodsList[position]
            itemView.setOnClickListener { itemClick(moodsList[position]) }
        }
    }

    override fun getItemCount(): Int {
        return moodsList.size
    }
}