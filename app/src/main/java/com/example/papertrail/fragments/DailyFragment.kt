package com.example.papertrail.fragments

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.papertrail.JournalEntry
import com.example.papertrail.R
import com.example.papertrail.databinding.FragmentDailyBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.Date
import java.util.Locale
import java.util.UUID

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

        val uid = FirebaseAuth.getInstance().currentUser?.uid
        var name = "User"

        database.getReference("users")
            .child(uid!!)
            .child("name")
            .get().addOnSuccessListener { snapshot ->
                name = snapshot.getValue(String::class.java).toString()
                binding.helloUser.text = "Hello, ${name}."
                Log.d("userName", "Nombre: $name")
            }.addOnFailureListener{
                Log.e("userName", "Error al obtener el nombre")
                binding.helloUser.text = "Hello."
            }

        val imageView = view.findViewById<ImageView>(R.id.motivationalImage)
        val imageUrl = "https://plus.unsplash.com/premium_photo-1687067885966-d755107af021?q=80&w=2664&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"

        Glide.with(requireContext())
            .load(imageUrl)
            .into(imageView)

        binding.saveButton.setOnClickListener{
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val now = sdf.format(Date())
            val uid = auth.currentUser?.uid

            if(uid==null){
                Snackbar.make(binding.root, "User not authenticated", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val title = binding.journalEntryTitle.text.toString().trim()
            val content = binding.journalEntryText.text.toString().trim()

            if(title.isEmpty()){
                Snackbar.make(binding.root, "Please add a title for your journal entry", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(content.isEmpty()){
                Snackbar.make(binding.root, "Please write something in your journal entry", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val entries = database.getReference("entries").child(uid!!)
            val newRef = entries.push()
            val id = newRef.key ?: UUID.randomUUID().toString()

            val entry = JournalEntry(
                id = id,
                title = binding.journalEntryTitle.text.toString().trim(),
                content = binding.journalEntryText.text.toString().trim(),
                dateTime = now
            )

            if (uid != null) {
                newRef.setValue(entry)
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

        val url = "https://quotes-inspirational-quotes-motivational-quotes.p.rapidapi.com/quote?token=ipworld.info"

        val request = object : JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                val text = response.getString("text")
                val author = response.getString("author")

                binding.quoteText.text = "“$text”"
                binding.authorText.text = "$author"
            },
            { error ->
                Log.e("DailyFragment", "Error fetching quote", error)
            }
        ) {
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["x-rapidapi-key"] = "bc86099804mshcb76e20e47978d7p11dfb4jsn0aa39bc45337"
                headers["x-rapidapi-host"] = "quotes-inspirational-quotes-motivational-quotes.p.rapidapi.com"
                return headers
            }
        }

        Volley.newRequestQueue(requireContext()).add(request)

    }
}