package com.bahaa.invoicech.utils

import android.content.Context
import android.widget.Toast
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*

fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

fun showDatePicker(context: Context, onDateSelected: (Long?) -> Unit) {
    val picker = MaterialDatePicker.Builder.datePicker()
        .setTitleText("Select Date")
        .build()

    picker.addOnPositiveButtonClickListener { millis ->
        onDateSelected(millis)
    }

    val activity = context as? androidx.fragment.app.FragmentActivity
    activity?.supportFragmentManager?.let {
        picker.show(it, "DATE_PICKER")
    } ?: run {
        Toast.makeText(context, "Cannot open date picker", Toast.LENGTH_SHORT).show()
    }
}
