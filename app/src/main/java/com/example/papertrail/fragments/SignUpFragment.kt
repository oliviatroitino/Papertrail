package com.example.papertrail.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.papertrail.R
import com.example.papertrail.databinding.FragmentSignUpBinding
import com.example.papertrail.databinding.FragmentWelcomeBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase

class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onAttach(context: Context) {
        super.onAttach(context)
        auth = FirebaseAuth.getInstance()
        database =
            FirebaseDatabase.getInstance("https://papertrail-75267-default-rtdb.europe-west1.firebasedatabase.app/")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.signupButton.setOnClickListener {

            val name = binding.nameInput.text.toString().trim()
            val email = binding.emailInput.text.toString().trim()
            val password = binding.passwordInput.text.toString().trim()

            if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
                Snackbar.make(binding.root, "Please fill in all fields", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(
                email, password
            ).addOnCompleteListener {
                if (it.isSuccessful) {
                    Snackbar.make(binding.root, "User created successfully", Snackbar.LENGTH_SHORT)
                        .show()

                    val user: FirebaseUser = auth.currentUser!!
                    val uid = user.uid
                    val email = user.email!!

                    val userRef = database.getReference("users").child(uid)
                    val userData = mapOf(
                        "name" to name,
                        "email" to email,
                        "createdAt" to System.currentTimeMillis()
                    )
                    userRef.setValue(userData).addOnSuccessListener {
                        Log.v("Database", "User saved successfully")
                    }.addOnFailureListener {
                        Log.e("Database", "Failed to save user", it)
                    }

                    Log.v("User: ", uid)
                    auth.signOut()

                    Navigation.findNavController(view).navigate(R.id.action_signUpFragment_to_logInFragment)
                } else {
                    Snackbar.make(binding.root, "Sign up failed", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
        binding.returnButton.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_signUpFragment_to_welcomeFragment)
        }
    }
}