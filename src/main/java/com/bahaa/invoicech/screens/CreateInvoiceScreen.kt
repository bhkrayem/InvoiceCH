package com.bahaa.invoicech.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bahaa.invoicech.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun CreateInvoiceScreen(navController: NavController) {
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)

    var discountText by remember { mutableStateOf("0.0") }
    val discount = discountText.toDoubleOrNull() ?: 0.0


    val customerDao = db.customerDao()
    val itemDao = db.itemDao()
    val invoiceRepository = InvoiceRepository(db.invoiceDao(), db.invoiceItemDao())

    var customerQuery by remember { mutableStateOf("") }
    var selectedCustomer by remember { mutableStateOf<CustomerEntity?>(null) }

    var itemQuery by remember { mutableStateOf("") }
    var selectedItem by remember { mutableStateOf<ItemEntity?>(null) }

    var quantityText by remember { mutableStateOf("1.0") }
    val quantity = quantityText.toDoubleOrNull() ?: 1.0

    val unitOptions = listOf("box", "piece")
    var unitType by remember { mutableStateOf(unitOptions[0]) }
    var unitDropdownExpanded by remember { mutableStateOf(false) }

    val cartItems = remember { mutableStateListOf<InvoiceItemEntity>() }

    val customerFlow = if (customerQuery.isNotBlank()) {
        customerDao.searchCustomers("%${customerQuery}%")
    } else {
        emptyFlow()
    }.collectAsStateWithLifecycle(initialValue = emptyList())

    val itemFlow = itemDao.getAllItems().collectAsStateWithLifecycle(initialValue = emptyList())
    val filteredItems = itemFlow.value.filter { it.name.contains(itemQuery, ignoreCase = true) }

    val unitPrice = selectedItem?.let {
        if (unitType == "box") it.boxPrice else it.boxPrice / it.piecesPerBox
    } ?: 0.0

    val totalPrice = quantity * unitPrice
    val subtotal = cartItems.sumOf { it.totalPrice }
    val invoiceTotal = subtotal - discount

    Text("Subtotal: $subtotal")
    Text("Discount: $discount")
    Text("Total: $invoiceTotal")


    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Create Invoice", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = customerQuery,
            onValueChange = { customerQuery = it },
            label = { Text("Search Customer") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = discountText,
            onValueChange = { discountText = it },
            label = { Text("Discount") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        customerFlow.value.take(5).forEach {
            Text(it.name, Modifier
                .fillMaxWidth()
                .clickable {
                    selectedCustomer = it
                    customerQuery = it.name
                }
                .padding(4.dp))
        }

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = itemQuery,
            onValueChange = { itemQuery = it },
            label = { Text("Search Item") },
            modifier = Modifier.fillMaxWidth()
        )
        filteredItems.take(5).forEach {
            Text(it.name, Modifier
                .fillMaxWidth()
                .clickable {
                    selectedItem = it
                    itemQuery = it.name
                }
                .padding(4.dp))
        }

        Spacer(Modifier.height(8.dp))

        if (selectedItem != null) {
            Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = quantityText,
                    onValueChange = { quantityText = it },
                    label = { Text("Quantity") },
                    modifier = Modifier.weight(1f)
                )
                Box(Modifier.weight(1f)) {
                    OutlinedTextField(
                        value = unitType,
                        onValueChange = {},
                        label = { Text("Unit") },
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { unitDropdownExpanded = true }
                    )
                    DropdownMenu(
                        expanded = unitDropdownExpanded,
                        onDismissRequest = { unitDropdownExpanded = false }
                    ) {
                        unitOptions.forEach {
                            DropdownMenuItem(
                                text = { Text(it) },
                                onClick = {
                                    unitType = it
                                    unitDropdownExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(4.dp))
            Text("Unit Price: $unitPrice")
            Text("Total Price: $totalPrice")

            Spacer(Modifier.height(8.dp))
            Button(onClick = {
                val item = selectedItem!!
                cartItems.add(
                    InvoiceItemEntity(
                        invoiceId = 0,
                        itemId = item.id,
                        quantity = quantity,
                        unitType = unitType,
                        unitPrice = unitPrice,
                        totalPrice = totalPrice
                    )
                )
                itemQuery = ""
                quantityText = "1.0"
                unitType = unitOptions[0]
                selectedItem = null
            }, enabled = selectedItem != null, modifier = Modifier.fillMaxWidth()) {
                Text("Add to Invoice")
            }
        }

        Spacer(Modifier.height(16.dp))

        if (cartItems.isNotEmpty()) {
            Text("Items:")
            cartItems.forEach { item ->
                val name = itemFlow.value.find { it.id == item.itemId }?.name ?: "Item"
                Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                    Text("$name: ${item.quantity} ${item.unitType} = ${item.totalPrice}")
                    Text("Delete", color = MaterialTheme.colorScheme.error, modifier = Modifier.clickable {
                        cartItems.remove(item)
                    })
                }
            }
            Spacer(Modifier.height(8.dp))
            Text("Total: $invoiceTotal")
        }

        Spacer(Modifier.height(16.dp))
        Button(onClick = {
            if (selectedCustomer != null && cartItems.isNotEmpty()) {
                CoroutineScope(Dispatchers.IO).launch {
                    invoiceRepository.createInvoiceWithItems(
                        selectedCustomer!!.id,
                        cartItems.toList(),
                        discount = discount
                    )

                    CoroutineScope(Dispatchers.Main).launch {
                        Toast.makeText(context, "Invoice Saved!", Toast.LENGTH_SHORT).show()
                        cartItems.clear()
                        navController.navigate("invoiceList")
                    }
                }
            }
        }, enabled = selectedCustomer != null && cartItems.isNotEmpty(), modifier = Modifier.fillMaxWidth()) {
            Text("Save Invoice")
        }
    }
}
