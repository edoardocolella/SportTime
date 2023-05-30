package com.example.polito_mad_01.model

class UserData(
    var name: String,
    var surname: String,
    var nickname: String,
    var birthdate: String,
    var gender: String,
    var location: String,
    var skills: MutableMap<String, String>,
    var image_uri : String?,
    var email : String,
    var phoneNumber : String,
    var availability : MutableMap<String,Boolean>,
    var password : String
    ) {
    constructor() : this(
        name="",
        surname="",
        nickname="",
        birthdate="",
        gender="",
        location="",
        skills=mutableMapOf(),
        image_uri=null,
        email="",
        password="",
        phoneNumber="",
        availability=mutableMapOf(),
    )

    fun toUser() : User {
        return User(
            name,
            surname,
            nickname,
            birthdate,
            gender,
            location,
            listOf(),
            skills,
            image_uri,
            email,
            phoneNumber,
            availability,
            listOf()
        )
    }
}