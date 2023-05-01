package com.example.polito_mad_01.db

import androidx.room.*

@Entity(tableName = "slot",
    foreignKeys = [
        ForeignKey(
            entity = Playground::class,
            parentColumns = arrayOf("playground_id"),
            childColumns = arrayOf("playground_id"),
            onDelete = ForeignKey.CASCADE
        )
    ])
data class Slot(
    @PrimaryKey(autoGenerate = true)
    var slot_id: Int = 0,
    val playground_id: Int,
    var user_id: Int?,
    var date: String,
    var start_time: String,
    var end_time: String,
    val total_price: Double,
    var lighting: Boolean,
    var heating: Boolean,
    var equipment: Boolean,
    var locker_room: Boolean
    )

data class SlotWithPlayground(
    @Embedded val slot: Slot,
    @Relation(
        parentColumn = "playground_id",
        entityColumn = "playground_id"
    )
    val playground: Playground)



