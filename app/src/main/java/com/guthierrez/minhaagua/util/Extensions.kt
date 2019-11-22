package com.guthierrez.minhaagua.util

import java.text.SimpleDateFormat
import java.util.*

private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
private val referenceFormat = SimpleDateFormat("MM/yyyy", Locale.US)

fun String.date(): Date? {
   return dateFormat.parse(this)
}

fun Date.timestamp(): String {
    return dateFormat.format(this)
}

fun Date.reference(): String {
    return referenceFormat.format(this)
}

fun String.reference(): Date {
    return referenceFormat.parse(this)!!
}