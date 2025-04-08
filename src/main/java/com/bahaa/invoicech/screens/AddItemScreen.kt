package com.bahaa.invoicech.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bahaa.invoicech.AppDatabase
import com.bahaa.invoicech.ItemEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun AddItemScreen(navController: NavController) {
    val context = LocalContext.current
    val itemDao = AppDatabase.getDatabase(context).itemDao()
    var name by remember { mutableStateOf("") }
    var boxPriceText by remember { mutableStateOf("") }
    var piecesPerBoxText by remember { mutableStateOf("") }

    val items = itemDao.getAllItems().collectAsStateWithLifecycle(initialValue = emptyList())

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Add Item", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Item Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = boxPriceText,
            onValueChange = { boxPriceText = it },
            label = { Text("Box Price") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = piecesPerBoxText,
            onValueChange = { piecesPerBoxText = it },
            label = { Text("Pieces per Box") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            val boxPrice = boxPriceText.toDoubleOrNull()
            val piecesPerBox = piecesPerBoxText.toIntOrNull()
            if (name.isNotBlank() && boxPrice != null && piecesPerBox != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    itemDao.insertItem(ItemEntity(name = name.trim(), boxPrice = boxPrice, piecesPerBox = piecesPerBox))
                }
                name = ""
                boxPriceText = ""
                piecesPerBoxText = ""
            }
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Add Item")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Items:")
        items.value.forEach {
            Text(it.name, modifier = Modifier.padding(4.dp))
        }
    }
}
