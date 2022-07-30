package com.example.lab3.ui.profile

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lab3.databinding.ItemImageBinding
import com.example.lab3.databinding.ItemMoodBinding

class ImageAdapter(val itemClick: (String, Int) -> Unit) :
    RecyclerView.Adapter<ImageAdapter.ImageHolder>() {

    private var imageList = ArrayList<String>()

    inner class ImageHolder(val binding: ItemImageBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val binding = ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        with(holder){
            Glide.with(binding.itemImage)
                .load(imageList[position])
                .into(binding.itemImage)
        }

        holder.itemView.setOnClickListener {
            itemClick(imageList[position], position)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addList(image: ArrayList<String>){
        imageList.clear()
        imageList.addAll(image)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addToList(image: String){
        imageList.add(image)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return imageList.size
    }
}