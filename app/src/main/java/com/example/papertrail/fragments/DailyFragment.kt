package com.example.papertrail.fragments

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.papertrail.JournalEntry
import com.example.papertrail.databinding.FragmentDailyBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.Date
import java.util.Locale


class DailyFragment : Fragment() {

    private lateinit var binding: FragmentDailyBinding
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
        binding = FragmentDailyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.saveButton.setOnClickListener{
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val now = sdf.format(Date())
            val id = Date().time.toString()

            val entry = JournalEntry(
                id = id,
                title = binding.journalEntryTitle.text.toString().trim(),
                content = binding.journalEntryText.text.toString().trim(),
                dateTime = now
            )

            if(entry.title.isEmpty()){
                Snackbar.make(binding.root, "Please add a title for your journal entry", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(entry.content.isEmpty()){
                Snackbar.make(binding.root, "Please write something in your journal entry", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val uid = auth.currentUser?.uid
            if (uid != null) {
                database.getReference("entries")
                    .child(uid)
                    .child(entry.id)
                    .setValue(entry)
                    .addOnSuccessListener {
                        Snackbar.make(binding.root, "Journal entry saved", Snackbar.LENGTH_SHORT).show()
                        binding.journalEntryTitle.text?.clear()
                        binding.journalEntryText.text?.clear()
                    }
                    .addOnFailureListener {
                        Snackbar.make(binding.root, "Failed to save entry", Snackbar.LENGTH_SHORT).show()
                    }
            } else {
                Snackbar.make(binding.root, "User not authenticated", Snackbar.LENGTH_SHORT).show()
            }
        }

        super.onViewCreated(view, savedInstanceState)

        val url = "https://andruxnet-random-famous-quotes.p.rapidapi.com/?cat=famous&count=1"

        val request = object : JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                val quoteObject = response.getJSONObject(0)
                val quote = quoteObject.getString("quote")
                val author = quoteObject.getString("author")

                binding.quoteText.text = "\"$quote\""
                binding.authorText.text = "- $author"
            },
            { error ->
                Log.e("DailyFragment", "Error fetching quote", error)
            }
        ) {
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["x-rapidapi-key"] = "bc86099804mshcb76e20e47978d7p11dfb4jsn0aa39bc45337"
                headers["x-rapidapi-host"] = "andruxnet-random-famous-quotes.p.rapidapi.com"
                return headers
            }
        }

        Volley.newRequestQueue(requireContext()).add(request)


    }
}