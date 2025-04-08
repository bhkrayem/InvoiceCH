package com.bahaa.invoicech

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bahaa.invoicech.ui.theme.InvoiceCHTheme

@Composable
fun ItemForm(
    modifier: Modifier = Modifier,
    onSaveItem: (ItemEntity) -> Unit
) {
    var itemName by remember { mutableStateOf("") }
    var boxPrice by remember { mutableStateOf("") }
    var piecesPerBox by remember { mutableStateOf("") }

    Column(modifier = modifier.padding(16.dp)) {
        Text(
            text = "Add New Item",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        TextField(
            value = itemName,
            onValueChange = { itemName = it },
            label = { Text("Item Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = boxPrice,
            onValueChange = { boxPrice = it },
            label = { Text("Box Price") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = piecesPerBox,
            onValueChange = { piecesPerBox = it },
            label = { Text("Pieces per Box") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                onSaveItem(
                    ItemEntity(
                        name = itemName,
                        boxPrice = boxPrice.toDoubleOrNull() ?: 0.0,
                        piecesPerBox = piecesPerBox.toIntOrNull() ?: 0
                    )
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Item")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewItemForm() {
    InvoiceCHTheme {
        ItemForm(onSaveItem = {})
    }
}