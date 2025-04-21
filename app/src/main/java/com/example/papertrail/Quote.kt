package com.example.papertrail

data class Quote(
    var id: String,
    var content: String,
    var author: String,
    var authorSlug: String,
    var tags: Array<String>,
    var length: Number,
)

// Quotable de lukePeavey en GitHub

//Response
//
//// An array containing one or more Quotes
//Array<{
//    _id: string
//    // The quotation text
//    content: string
//    // The full name of the author
//    author: string
//    // The `slug` of the quote author
//    authorSlug: string
//    // The length of quote (number of characters)
//    length: number
//    // An array of tag names for this quote
//    tags: string[]
//}>
