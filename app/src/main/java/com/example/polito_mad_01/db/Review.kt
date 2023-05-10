package com.example.polito_mad_01.db

import androidx.room.Entity
import androidx.room.ForeignKey
import org.jetbrains.annotations.Nullable

@Entity(tableName = "review",
    primaryKeys = ["playground_id","user_id"],
    foreignKeys = [
        ForeignKey(
            entity = Playground::class,
            parentColumns = arrayOf("playground_id"),
            childColumns = arrayOf("playground_id"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = User::class,
            parentColumns = arrayOf("user_id"),
            childColumns = arrayOf("user_id"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Review (
    val playground_id: Int,
    val user_id: Int,
    val rating: Int,
    val review_text: String?
)