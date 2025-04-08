package com.bahaa.invoicech

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface InvoiceDao {

    @Insert
    suspend fun insertInvoice(invoice: InvoiceEntity): Long

    @Query("SELECT * FROM invoices ORDER BY date DESC")
    fun getAllInvoices(): Flow<List<InvoiceEntity>>

    @Query("SELECT * FROM invoices WHERE id = :invoiceId")
    suspend fun getInvoiceByIdSuspend(invoiceId: Int): InvoiceEntity?

    @Query("SELECT * FROM invoice_items WHERE invoiceId = :invoiceId")
    suspend fun getItemsForInvoiceSuspend(invoiceId: Int): List<InvoiceItemEntity>

    @Query("SELECT * FROM invoices WHERE date BETWEEN :start AND :end ORDER BY date DESC")
    suspend fun getInvoicesBetweenDatesSuspend(start: Long, end: Long): List<InvoiceEntity>

    @Query("SELECT MAX(invoiceNumber) FROM invoices")
    suspend fun getLastInvoiceNumber(): Int?

}
