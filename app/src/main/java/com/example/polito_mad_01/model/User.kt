package com.example.polito_mad_01.model

data class User(
    var id: String,
    var name: String,
    var surname: String,
    var nickname: String,
    var birthdate: String,
    var gender: String,
    var location: String,
    var achievements: String,
    var skills: MutableMap<String, String>,
    var image_uri : String?,
    var email : String,
    var phoneNumber : String,
    val availability : MutableMap<String,Boolean>,
    val friends : List<String>
) {

    constructor() : this(
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        mutableMapOf(),
        null,
        "",
        "",
        mutableMapOf(),
        listOf()
    )
    override fun toString(): String =
        "$name $surname $nickname $birthdate $gender $location $achievements $skills"
}


