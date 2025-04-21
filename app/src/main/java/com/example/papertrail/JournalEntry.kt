package com.example.papertrail

data class JournalEntry(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val dateTime: String = ""   // "DD/MM/YYYY HH:mm"
)
