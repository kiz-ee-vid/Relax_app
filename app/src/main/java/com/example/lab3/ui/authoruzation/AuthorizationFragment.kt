package com.example.lab3.ui.authoruzation

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.lab3.MainActivity
import com.example.lab3.R
import com.example.lab3.data.DB
import com.example.lab3.databinding.FragmentAuthorizationBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AuthorizationFragment: Fragment() {

    private val binding: FragmentAuthorizationBinding by lazy { FragmentAuthorizationBinding.inflate(layoutInflater) }
    private val reference by lazy { DB.getReference() }
    private val auth by lazy { Firebase.auth }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity).binding.navView.visibility = View.GONE
        setListeners()
        return binding.root
    }

    private fun setListeners(){
        binding.authorizationButton.setOnClickListener {
            val login = binding.email.text.toString()
            val password = binding.password.text.toString()
            if (checkData(login, password)) {
                auth.signInWithEmailAndPassword(login, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val bundle = Bundle()
                        bundle.putString("user", login.hashCode().toString())
                        Navigation
                            .findNavController(binding.root)
                            .navigate(
                                R.id.action_authorization_to_navigation_home,
                                bundle,
                                null
                            )
                    } else
                        Toast.makeText(context, "Account does not exist", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.registrationButton.setOnClickListener{
            Navigation
                .findNavController(binding.root)
                .navigate(
                    R.id.action_authorization_to_registration,
                    null
                )
        }
    }

    private fun checkData(login: String, password: String): Boolean {
        if (login.isNotEmpty() && password.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(login)
                .matches()
        )
            return true
        else
            Toast.makeText(context, "Incorrect login/password", Toast.LENGTH_SHORT).show()
        return false
    }

}