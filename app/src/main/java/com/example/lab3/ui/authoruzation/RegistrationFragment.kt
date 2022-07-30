package com.example.lab3.ui.authoruzation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.lab3.R
import com.example.lab3.data.DB
import com.example.lab3.data.User
import com.example.lab3.databinding.FragmentRegistrationBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegistrationFragment : Fragment() {

    private val binding: FragmentRegistrationBinding by lazy {
        FragmentRegistrationBinding.inflate(
            layoutInflater
        )
    }
    private val reference by lazy { DB.getReference() }
    private val auth by lazy { Firebase.auth }
    private var image: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setListener()
        return binding.root
    }

    private fun setListener() {

        binding.registrationImage.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1)
        }

        binding.registrationButton.setOnClickListener {
            val login = binding.email.text.toString()
            val password = binding.password.text.toString()
            val name = binding.name.text.toString()
            //        println(image.toString())
            if (checkData(login, password, name)) {
                auth.createUserWithEmailAndPassword(login, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        reference.child("users").child(login.hashCode().toString())
                            .setValue(
                                User(
                                    login,
                                    mutableListOf(),
                                    name,
                                    "No information",
                                    image.toString()
                                )
                            )
                        Navigation
                            .findNavController(binding.root)
                            .navigate(
                                R.id.action_registration_to_authorization,
                                null
                            )
                    }
                    else
                        Toast.makeText(context, "Such account already exist", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun checkData(login: String, password: String, name: String): Boolean {
        if (name.isNotEmpty() && login.isNotEmpty() && password.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(login)
                .matches()
        )
            return true
        else
            Toast.makeText(context, "Incorrect data", Toast.LENGTH_SHORT).show()
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data != null) {
            binding.registrationImage.setImageURI(data.data)
            image = data.data
        }
    }

}