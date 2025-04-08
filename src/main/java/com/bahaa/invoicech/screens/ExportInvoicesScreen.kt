package com.bahaa.invoicech.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bahaa.invoicech.*
import com.bahaa.invoicech.helpers.PdfHelper
import com.bahaa.invoicech.helpers.PrintFileDocumentAdapter
import com.bahaa.invoicech.utils.formatDate
import com.bahaa.invoicech.utils.showDatePicker

@Composable
fun ExportInvoicesScreen(navController: NavController) {
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val invoiceDao = db.invoiceDao()

    val today = System.currentTimeMillis()
    var startDate by remember { mutableLongStateOf(today) }
    var endDate by remember { mutableLongStateOf(today) }

    val invoices by produceState(initialValue = emptyList<InvoiceEntity>(), startDate, endDate) {
        value = invoiceDao.getInvoicesBetweenDatesSuspend(startDate, endDate)
    }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Export Invoices", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(16.dp))

        Text("Start: ${formatDate(startDate)}")
        Button(onClick = {
            showDatePicker(context) { date -> if (date != null) startDate = date }
        }) { Text("Pick Start Date") }

        Spacer(Modifier.height(8.dp))

        Text("End: ${formatDate(endDate)}")
        Button(onClick = {
            showDatePicker(context) { date -> if (date != null) endDate = date }
        }) { Text("Pick End Date") }

        Spacer(Modifier.height(16.dp))
        Button(onClick = {
            PdfHelper.createInvoicePdf(context, invoices) { file ->
                if (file != null) {
                    Toast.makeText(context, "Saved: ${file.absolutePath}", Toast.LENGTH_LONG).show()
                }
            }
        }) { Text("Export to PDF") }

        Button(onClick = {
            PdfHelper.createInvoicePdf(context, invoices) { file ->
                if (file != null) {
                    val uri = androidx.core.content.FileProvider.getUriForFile(
                        context, "${context.packageName}.provider", file
                    )
                    val intent = android.content.Intent().apply {
                        action = android.content.Intent.ACTION_SEND
                        type = "application/pdf"
                        putExtra(android.content.Intent.EXTRA_STREAM, uri)
                        addFlags(android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
                    context.startActivity(android.content.Intent.createChooser(intent, "Share PDF"))
                }
            }
        }) { Text("Share PDF") }

        Button(onClick = {
            PdfHelper.createInvoicePdf(context, invoices) { file ->
                if (file != null) {
                    val printManager = context.getSystemService(android.content.Context.PRINT_SERVICE) as android.print.PrintManager
                    val adapter = PrintFileDocumentAdapter(context, file)
                    printManager.print("Invoice Report", adapter, null)
                }
            }
        }) { Text("Print PDF") }
    }
}
