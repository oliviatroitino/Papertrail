package com.example.papertrail.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.Navigation
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.papertrail.R
import com.example.papertrail.databinding.FragmentDailyBinding
import com.example.papertrail.databinding.FragmentWelcomeBinding

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
        val textView = view.findViewById<TextView>(R.id.text_quote)

        val queue = Volley.newRequestQueue(requireContext())
        val url = "https://www.google.com"

        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                textView.text = "Response is: ${response.substring(0, 500)}"
            },
            {
                textView.text = "That didn't work!"
            })

        queue.add(stringRequest)
//        binding.loginButton.setOnClickListener {
//            Navigation.findNavController(view).navigate(R.id.action_welcomeFragment_to_logInFragment)
//        }
    }
}