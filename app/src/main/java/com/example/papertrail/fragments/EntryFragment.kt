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
import com.example.papertrail.databinding.FragmentEntryBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class EntryFragment : Fragment() {

    private lateinit var binding: FragmentEntryBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onAttach(context: Context) {
        super.onAttach(context)
        auth = FirebaseAuth.getInstance()
        database =
            FirebaseDatabase.getInstance("https://papertrail-75267-default-rtdb.europe-west1.firebasedatabase.app/")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentEntryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val title = arguments?.getString("title")
        val content = arguments?.getString("content")
        val dateTime = arguments?.getString("dateTime")
        val id = arguments?.getString("entryId")

        val uid = auth.currentUser?.uid

        binding.titleText.text = title
        binding.contentText.text = content
        binding.dateText.text = dateTime

        binding.returnButton.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_entryFragment_to_homeFragment)
        }

        binding.deleteButton.setOnClickListener {
            Log.d("ID", "ID: ${id}")

            if(uid!=null){
                val entry = database.getReference("entries").child(uid).child(id!!)

                Log.d("DELETE ENTRY", "Entry to delete: ${title}")

                entry.removeValue()
                    .addOnSuccessListener {
                        Log.d("Firebase", "Entry deleted successfully")
                    }
                    .addOnFailureListener { error ->
                        Log.e("Firebase", "Failed to delete entry", error)
                    }
            }

            Navigation.findNavController(view)
                .navigate(R.id.action_entryFragment_to_homeFragment)
        }
    }
}
