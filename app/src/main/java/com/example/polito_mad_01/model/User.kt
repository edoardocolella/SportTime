package com.example.polito_mad_01.model

import androidx.room.*


@Entity(tableName = "user")
class User(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_id") @JvmField val userId: Int,
    @ColumnInfo(name = "name") @JvmField var name: String,
    @ColumnInfo(name = "surname") @JvmField var surname: String,
    @ColumnInfo(name = "nickname") @JvmField var nickname: String,
    @ColumnInfo(name = "description") @JvmField var description: String,
    @ColumnInfo(name = "gender") @JvmField var gender: String,
    @ColumnInfo(name = "birthdate") @JvmField var birthdate: String,
    @ColumnInfo(name = "location") @JvmField var location: String,
    @ColumnInfo(name = "email") @JvmField var email: String,
    @ColumnInfo(name = "phone_number") @JvmField var phoneNumber: String,
    @ColumnInfo(name = "image_uri") @JvmField var image_uri: String?,
    @ColumnInfo(name = "monday_availability") @JvmField var mondayAvailability: String?,
    @ColumnInfo(name = "tuesday_availability") @JvmField var tuesdayAvailability: String?,
    @ColumnInfo(name = "wednesday_availability") @JvmField var wednesdayAvailability: String?,
    @ColumnInfo(name = "thursday_availability") @JvmField var thursdayAvailability: String?,
    @ColumnInfo(name = "friday_availability") @JvmField var fridayAvailability: String?,
    @ColumnInfo(name = "saturday_availability") @JvmField var saturdayAvailability: String?,
    @ColumnInfo(name = "sunday_availability") @JvmField var sundayAvailability: String?,
) {

    fun setName(name: String) {
        this.name = name
    }

    fun setSurname(surname: String) {
        this.surname = surname
    }

    fun setNickname(nickname: String) {
        this.nickname = nickname
    }

    fun setDescription(description: String) {
        this.description = description
    }

    fun setGender(gender: String) {
        this.gender = gender
    }

    fun setBirthdate(birthdate: String) {
        this.birthdate = birthdate
    }

    fun setLocation(location: String) {
        this.location = location
    }

    fun setEmail(email: String) {
        this.email = email
    }

    fun setPhoneNumber(phoneNumber: String) {
        this.phoneNumber = phoneNumber
    }

    fun setImageUri(image_uri: String?) {
        this.image_uri = image_uri
    }

    fun setMondayAvailability(mondayAvailability: String?) {
        this.mondayAvailability = mondayAvailability
    }

    fun setTuesdayAvailability(tuesdayAvailability: String?) {
        this.tuesdayAvailability = tuesdayAvailability
    }

    fun setWednesdayAvailability(wednesdayAvailability: String?) {
        this.wednesdayAvailability = wednesdayAvailability
    }

    fun setThursdayAvailability(thursdayAvailability: String?) {
        this.thursdayAvailability = thursdayAvailability
    }

    fun setFridayAvailability(fridayAvailability: String?) {
        this.fridayAvailability = fridayAvailability
    }

    fun setSaturdayAvailability(saturdayAvailability: String?) {
        this.saturdayAvailability = saturdayAvailability
    }

    fun setSundayAvailability(sundayAvailability: String?) {
        this.sundayAvailability = sundayAvailability
    }

}