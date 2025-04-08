package com.bahaa.invoicech.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bahaa.invoicech.AppDatabase
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun InvoiceListScreen(navController: NavController) {
    val context = LocalContext.current
    val dao = AppDatabase.getDatabase(context).invoiceDao()
    val invoices = dao.getAllInvoices().collectAsStateWithLifecycle(initialValue = emptyList())

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Invoice List", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))
        invoices.value.forEach {
            Text(
                text = "Invoice #${it.invoiceNumber} - ${it.totalAmount}",
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.navigate("invoicePreview/${it.id}") }
                    .padding(4.dp)
            )
        }
    }
}
