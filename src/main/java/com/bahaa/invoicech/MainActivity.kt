package com.bahaa.invoicech

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bahaa.invoicech.screens.*
import com.bahaa.invoicech.ui.theme.InvoiceCHTheme
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Modifier
import com.bahaa.invoicech.screens.AddCustomerScreen
import com.bahaa.invoicech.screens.AddItemScreen
import com.bahaa.invoicech.screens.InvoiceListScreen
import com.bahaa.invoicech.screens.ExportInvoicesScreen
import com.bahaa.invoicech.screens.InvoicePreviewScreen
import com.bahaa.invoicech.screens.CreateInvoiceScreen





class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InvoiceCHTheme {
                val navController = rememberNavController()

                NavHost(navController, startDestination = "main") {
                    composable("main") { MainScreen(navController) }
                    composable("addItem") { AddItemScreen(navController) }
                    composable("addCustomer") {
                        val context = LocalContext.current
                        AddCustomerScreen(
                            modifier = Modifier,
                            customerDao = AppDatabase.getDatabase(context).customerDao()
                        )
                    }


                    composable("createInvoice") { CreateInvoiceScreen(navController) }
                    composable("invoiceList") { InvoiceListScreen(navController) }
                    composable("exportInvoices") { ExportInvoicesScreen(navController) }
                    composable(
                        "invoicePreview/{invoiceId}",
                        arguments = listOf(navArgument("invoiceId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val invoiceId = backStackEntry.arguments?.getInt("invoiceId") ?: 0
                        InvoicePreviewScreen(navController, invoiceId)
                    }
                }
            }
        }
    }
}
