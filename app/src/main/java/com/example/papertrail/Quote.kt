package com.example.papertrail

data class Quote(
    var id: String,
    var content: String,
    var author: String,
    var authorSlug: String,
    var authorId: String,
    var tags: Array<String>,
    var length: Number,
    var dateAdded: String,
    var dateModified: String
)

// Quotable de lukePeavey en GitHub

//const QuoteSchema = new Schema({
//    // @internal
//    _id: { type: String, default: shortid.generate },
//    // The quotation text
//    content: { type: String, required: true },
//    // The full display name of the quote's author
//    author: { type: String, required: true },
//    // The author `slug` is a unique ID derived from the author's name.
//    authorSlug: { type: String, required: true },
//    // @deprecated in favor of authorSlug
//    authorId: { type: String, required: true },
//    // An array of tags for this quote
//    tags: { type: [String], required: true },
//    // The length of the quote (total number of characters)
//    length: { type: Number, required: true },
//    // Timestamp when item was added
//    dateAdded: { type: String },
//    // Timestamp when item was last updated
//    dateModified: { type: String },
//})
