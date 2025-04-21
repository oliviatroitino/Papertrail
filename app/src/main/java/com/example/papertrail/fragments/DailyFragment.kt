package com.example.papertrail.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.papertrail.R
import com.example.papertrail.databinding.FragmentDailyBinding
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest


class DailyFragment : Fragment() {

    private lateinit var binding: FragmentDailyBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDailyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
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