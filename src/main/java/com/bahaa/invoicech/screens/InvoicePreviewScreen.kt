package com.bahaa.invoicech.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bahaa.invoicech.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun InvoicePreviewScreen(navController: NavController, invoiceId: Int) {
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val invoice by produceState<InvoiceEntity?>(null, invoiceId) {
        value = db.invoiceDao().getInvoiceByIdSuspend(invoiceId)
    }
    val items by produceState(initialValue = emptyList<InvoiceItemEntity>(), invoiceId) {
        value = db.invoiceItemDao().getItemsForInvoiceSuspend(invoiceId)
    }
    val customers = db.customerDao().getAllCustomers().collectAsStateWithLifecycle(initialValue = emptyList())
    val allItems = db.itemDao().getAllItems().collectAsStateWithLifecycle(initialValue = emptyList())

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Invoice Preview", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))

        val name = customers.value.find { it.id == invoice?.customerId }?.name ?: "Unknown"
        Text("Customer: $name")

        items.forEach { itx ->
            val itemName = allItems.value.find { it.id == itx.itemId }?.name ?: "Item"
            Text("$itemName: ${itx.quantity} ${itx.unitType} x ${itx.unitPrice} = ${itx.totalPrice}")
        }

        Spacer(Modifier.height(8.dp))
        Text("Total: ${invoice?.totalAmount}")

        val discount = invoice?.discount ?: 0.0
        val total = invoice?.totalAmount ?: 0.0
        val subtotal = total + discount

        Text("Subtotal: %.2f".format(subtotal))
        Text(
            text = "Discount: %.2f".format(discount),
            color = if (discount > 0) MaterialTheme.colorScheme.error else LocalContentColor.current
        )
        Text("Total: %.2f".format(total))






    }
}
