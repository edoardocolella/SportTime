package com.example.polito_mad_01.model

data class Review (
    val playground_id: Int,
    val user_id: String,
    val rating: Int,
    val review_text: String?
){
    constructor() : this(0, "0", 0, "")
}