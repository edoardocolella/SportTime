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
    @ColumnInfo(name = "extra_id")
    val extraId: Int = 0,

    @ColumnInfo(name = "playground_id")
    val playgroundId: Int,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "price")
    val price: Double,



    )