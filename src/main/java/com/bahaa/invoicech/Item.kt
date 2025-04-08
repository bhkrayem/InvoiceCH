package com.bahaa.invoicech

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class ItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // Auto-incremented ID
    val name: String,
    val boxPrice: Double,
    val piecesPerBox: Int
)
