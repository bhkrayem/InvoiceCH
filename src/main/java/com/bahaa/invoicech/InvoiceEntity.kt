package com.bahaa.invoicech

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "invoices")
data class InvoiceEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val customerId: Int,

    val date: Long = System.currentTimeMillis(),
    val totalAmount: Double,
    val invoiceNumber: Int,
    val discount: Double = 0.0// ✅ New field

)
