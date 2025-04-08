package com.bahaa.invoicech

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Environment
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

            invoices.forEach {
                y += 30
                canvas.drawText("Invoice #${it.id} - Total: ${it.totalAmount}", 20f, y.toFloat(), paint)
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
