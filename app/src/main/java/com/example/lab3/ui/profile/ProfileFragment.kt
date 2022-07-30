package com.example.lab3.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lab3.R
import com.example.lab3.databinding.FragmentProfileBinding
import com.example.lab3.ui.home.HomeViewModel

class ProfileFragment : Fragment() {

    private val homeViewModel: HomeViewModel by activityViewModels()
    private val binding: FragmentProfileBinding by lazy {
        FragmentProfileBinding.inflate(
            layoutInflater
        )
    }
    private val imageRecycler: RecyclerView by lazy { binding.imageRecycler }
    lateinit var imageAdapter: ImageAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (homeViewModel.user.value?.image != "null") {
            Glide.with(binding.profileImage)
                .load(homeViewModel.user.value?.image)
                .into(binding.profileImage)
        }
        binding.profileName.text = homeViewModel.user.value?.name ?: "No information"

        imageAdapter = ImageAdapter() { uri, index ->
            val bundle = Bundle()
            bundle.putString("path", uri)
            bundle.putInt("index", index)
            Navigation
                .findNavController(binding.root)
                .navigate(
                    R.id.action_navigation_profile_to_image,
                    bundle,
                    null
                )
        }
        imageRecycler.adapter = imageAdapter
        imageRecycler.layoutManager = GridLayoutManager(context, 2)

        homeViewModel.user.observe(viewLifecycleOwner) { item ->
            imageAdapter.addList(item.list as ArrayList<String>)
        }

        binding.addImage.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1)
        }

        binding.profileImage.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 2)
        }

        return binding.root
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data != null && requestCode == 1) {
            data.data?.let {
                if (!homeViewModel.user.value!!.list.contains(it.toString())) {
                    homeViewModel.saveData(it.toString())
                    imageAdapter.addToList(it.toString())
                }
            }
        }
        if (data != null && requestCode == 2){
            homeViewModel.user.value?.image = data.dataString
            homeViewModel.saveImage()
            Glide.with(binding.profileImage)
                .load(data.dataString)
                .into(binding.profileImage)
        }
    }
}