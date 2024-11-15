package com.andyslab.geobud.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "landmark",
    indices = [Index(value = ["id"], name = "id", unique = true, orders = [Index.Order.ASC])]
)
data class Landmark(
    @PrimaryKey
    val id: Int,
    val name: String,
    val country: String,
    val continent: String,
    val city: String,
    val funFact: String,
    val photoUrl: String?,
    val photographer: String?,
    val photographerUrl: String?,
    )
