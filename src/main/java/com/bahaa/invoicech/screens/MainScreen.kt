package com.bahaa.invoicech.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun MainScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = { navController.navigate("addItem") }, modifier = Modifier.fillMaxWidth()) {
            Text("Manage Items")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate("addCustomer") }, modifier = Modifier.fillMaxWidth()) {
            Text("Manage Customers")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate("createInvoice") }, modifier = Modifier.fillMaxWidth()) {
            Text("Create Invoice")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate("invoiceList") }, modifier = Modifier.fillMaxWidth()) {
            Text("View Invoices")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate("exportInvoices") }, modifier = Modifier.fillMaxWidth()) {
            Text("Export Invoices")
        }
    }
}
