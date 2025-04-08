package com.bahaa.invoicech.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.bahaa.invoicech.CustomerDao
import com.bahaa.invoicech.CustomerEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun AddCustomerScreen(modifier: Modifier = Modifier, customerDao: CustomerDao) {
    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }

    // ✅ Using mutableStateOf for compatibility
    var refreshTrigger by remember { mutableStateOf(0) }

    val customers = customerDao.getAllCustomers()
        .collectAsStateWithLifecycle(refreshTrigger, initialValue = emptyList())

    Column(modifier = modifier.padding(16.dp)) {
        Text("Add Customer", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Customer Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = address,
            onValueChange = { address = it },
            label = { Text("Address") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (name.isNotBlank() && phone.isNotBlank() && address.isNotBlank()) {
                    CoroutineScope(Dispatchers.IO).launch {
                        customerDao.insertCustomer(
                            CustomerEntity(
                                name = name.trim(),
                                phone = phone.trim(),
                                address = address.trim()
                            )
                        )
                        CoroutineScope(Dispatchers.Main).launch {
                            refreshTrigger++
                            name = ""
                            phone = ""
                            address = ""
                            Toast.makeText(context, "Customer Added", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Customer")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Customers:")

        // ✅ Move this inside the Column scope
        customers.value.forEach { customer ->
            Text(
                "${customer.name.ifBlank { "No Name" }} - ${customer.phone.ifBlank { "No Phone" }} - ${customer.address.ifBlank { "No Address" }}",
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}
