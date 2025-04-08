package com.bahaa.invoicech

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "invoice_items")
data class InvoiceItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val invoiceId: Int,
    val itemId: Int,
    val quantity: Double,
    val unitType: String, // "box" or "piece"
    val unitPrice: Double,
    val totalPrice: Double
)
