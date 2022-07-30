package com.example.lab3.ui.horoscope

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lab3.databinding.ItemMoodBinding

class HoroscopeAdapter(var context: Context, val itemClick: (String) -> Unit) :
    RecyclerView.Adapter<HoroscopeAdapter.HoroscopeHolder>() {

    private var horoscopeList = arrayOf(
        "Aries",
        "Taurus",
        "Gemini",
        "Cancer",
        "Leo",
        "Virgo",
        "Libra",
        "Scorpio",
        "Sagittarius",
        "Capricorn",
        "Aquarius",
        "Pisces"
    )

    inner class HoroscopeHolder(val binding: ItemMoodBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HoroscopeHolder {
        val binding = ItemMoodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HoroscopeHolder(binding)
    }

    override fun onBindViewHolder(holder: HoroscopeHolder, position: Int) {
        with(holder) {
            val imgId = context.resources.getIdentifier(
                "ic_sign_$position",
                "drawable",
                context.packageName
            )
            binding.moodIcon.setImageResource(imgId)
            binding.moodTitle.text = horoscopeList[position]
        }
        holder.itemView.setOnClickListener {
            itemClick(horoscopeList[position])
        }
    }

    override fun getItemCount(): Int {
        return horoscopeList.size
    }
}