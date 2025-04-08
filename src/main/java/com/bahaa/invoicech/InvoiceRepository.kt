package com.bahaa.invoicech

class InvoiceRepository(
    private val invoiceDao: InvoiceDao,
    private val invoiceItemDao: InvoiceItemDao
) {
    suspend fun createInvoiceWithItems(customerId: Int, items: List<InvoiceItemEntity>, discount: Double = 0.0) {
        val lastNumber = invoiceDao.getLastInvoiceNumber() ?: 0
        val newInvoiceNumber = lastNumber + 1

        val totalAmountBeforeDiscount = items.sumOf { it.totalPrice }
        val totalAmount = totalAmountBeforeDiscount - discount

        val invoiceId = invoiceDao.insertInvoice(
            InvoiceEntity(
                customerId = customerId,
                totalAmount = totalAmount,
                invoiceNumber = newInvoiceNumber,
                discount = discount
            )
        ).toInt()

        val updatedItems = items.map { it.copy(invoiceId = invoiceId) }
        invoiceItemDao.insertInvoiceItems(updatedItems)
    }
}
