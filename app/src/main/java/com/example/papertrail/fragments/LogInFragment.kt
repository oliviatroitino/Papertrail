package com.example.papertrail.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.papertrail.R
import com.example.papertrail.databinding.FragmentLogInBinding
import com.example.papertrail.databinding.FragmentSignUpBinding
import com.example.papertrail.databinding.FragmentWelcomeBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LogInFragment : Fragment() {

    private lateinit var binding: FragmentLogInBinding
    private lateinit var auth: FirebaseAuth

    override fun onAttach(context: Context) {
        super.onAttach(context)
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLogInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginButton.setOnClickListener {
            val email = binding.emailInput.text.toString().trim()
            val password = binding.passwordInput.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Snackbar.make(binding.root, "Please fill in all fields", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    Snackbar.make(binding.root, "Login successful", Snackbar.LENGTH_SHORT).show()
                    val user = auth.currentUser
                    Log.v("User Logged In:", user?.uid ?: "null")

                    Navigation.findNavController(view)
                        .navigate(R.id.action_logInFragment_to_homeFragment)

                } else {
                    Snackbar.make(binding.root, it.exception?.localizedMessage ?: "Login failed", Snackbar.LENGTH_SHORT).show()
                    Log.e("LoginError", "Login failed", it.exception)
                }
            }
        }

        binding.returnButton.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_logInFragment_to_welcomeFragment)
        }
    }

}