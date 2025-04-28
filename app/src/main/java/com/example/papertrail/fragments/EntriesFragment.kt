package com.example.papertrail.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.papertrail.EntryAdapter
import com.example.papertrail.JournalEntry
import com.example.papertrail.R
import com.example.papertrail.databinding.FragmentDailyBinding
import com.example.papertrail.databinding.FragmentEntriesBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class EntriesFragment : Fragment() {

    private lateinit var binding: FragmentEntriesBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var entriesRef: DatabaseReference
    private lateinit var entryAdapter: EntryAdapter
    private lateinit var entryList: ArrayList<JournalEntry>
    private val entriesList = mutableListOf<JournalEntry>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance("https://papertrail-75267-default-rtdb.europe-west1.firebasedatabase.app/")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEntriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        binding.entriesRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val uid = auth.currentUser?.uid

        if (uid != null) {
            entriesRef = database.getReference("entries").child(uid)

            entriesRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    entriesList.clear()
                    for (entrySnapshot in snapshot.children) {
                        val entry = entrySnapshot.getValue(JournalEntry::class.java)
                        entry?.let { entriesList.add(it) }
                    }
                    entriesList.reverse()
                    binding.entriesRecyclerView.adapter = EntryAdapter(entriesList, requireContext()) { entry ->

                        val bundle = Bundle().apply {
                            putString("title", entry.title)
                            putString("content", entry.content)
                            putString("dateTime", entry.dateTime)
                        }
                        findNavController().navigate(R.id.action_homeFragment_to_entryFragment, bundle)

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("EntriesFragment", "Database error: ${error.message}")
                }
            })
        }
    }
}