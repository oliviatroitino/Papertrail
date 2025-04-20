package com.example.papertrail

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("v1/quotes/")
    fun getQuoteById(@Path("id") postId: Int): Call<Quote>
}