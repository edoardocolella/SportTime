package com.example.polito_mad_01.model

data class User(
    var name: String,
    var surname: String,
    var nickname: String,
    var birthdate: String,
    val gender: String,
    var location: String,
    var achievements: List<String>,
    var skills: MutableMap<String, String>,
    var image_uri : String?,
    var email : String,
    var phoneNumber : String,
    var monday_availability : Boolean,
    var tuesday_availability : Boolean,
    var wednesday_availability : Boolean,
    var thursday_availability : Boolean,
    var friday_availability : Boolean,
    var saturday_availability : Boolean,
    var sunday_availability : Boolean,
) {

    constructor() :
            this("", "", "", "", "", "",
                listOf<String>(), mutableMapOf<String, String>(), "", "",
                "", false, false,
                false, false, false, false, false)
    override fun toString(): String =
        "$name $surname $nickname $birthdate $gender $location $achievements $skills"
}


