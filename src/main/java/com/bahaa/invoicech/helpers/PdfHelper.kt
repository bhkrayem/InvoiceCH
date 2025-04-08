package com.bahaa.invoicech.helpers

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Environment
import com.bahaa.invoicech.InvoiceEntity
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

object PdfHelper {
    fun createInvoicePdf(context: Context, invoices: List<InvoiceEntity>, onResult: (File?) -> Unit) {
        try {
            val pdf = PdfDocument()
            val paint = Paint()

            val pageInfo = PdfDocument.PageInfo.Builder(300, 600 + invoices.size * 40, 1).create()
            val page = pdf.startPage(pageInfo)
            val canvas: Canvas = page.canvas

            var y = 20
            paint.textSize = 14f
            paint.isFakeBoldText = true
            canvas.drawText("Invoice Report", 80f, y.toFloat(), paint)

            y += 20
            paint.textSize = 10f
            paint.isFakeBoldText = false
            canvas.drawText("Date: ${SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())}", 20f, y.toFloat(), paint)

            invoices.forEach { invoice ->
                y += 30

                val total = invoice.totalAmount
                val discount = invoice.discount
                val subtotal = total + discount

                canvas.drawText("Invoice #${invoice.invoiceNumber}", 20f, y.toFloat(), paint)

                y += 16
                canvas.drawText("  Subtotal: %.2f".format(subtotal), 20f, y.toFloat(), paint)

                y += 16
                canvas.drawText("  Discount: %.2f".format(discount), 20f, y.toFloat(), paint)

                y += 16
                canvas.drawText("  Total: %.2f".format(total), 20f, y.toFloat(), paint)
            }

            pdf.finishPage(page)

            val dir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
            val file = File(dir, "Invoices_${System.currentTimeMillis()}.pdf")
            pdf.writeTo(FileOutputStream(file))
            pdf.close()
            onResult(file)
        } catch (e: Exception) {
            e.printStackTrace()
            onResult(null)
        }
    }
}
