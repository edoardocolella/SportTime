package com.example.polito_mad_01.model


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "playground_extra",
    foreignKeys = [
        ForeignKey(
            entity = Playground::class,
            parentColumns = arrayOf("playground_id"),
            childColumns = arrayOf("playground_id"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PlaygroundExtra(
    @PrimaryKey(autoGenerate = true)
    val extra_id: Int = 0,
    val playground_id: Int,
    val name: String,
    val description: String,
    val price: Double,
    )