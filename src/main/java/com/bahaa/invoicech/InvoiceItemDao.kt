package com.bahaa.invoicech

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface InvoiceItemDao {

    @Query("SELECT * FROM invoice_items WHERE invoiceId = :invoiceId")
    suspend fun getItemsForInvoiceSuspend(invoiceId: Int): List<InvoiceItemEntity>

    @Insert
    suspend fun insertInvoiceItems(items: List<InvoiceItemEntity>)
}
