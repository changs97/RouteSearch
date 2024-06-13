package com.changs.routesearch.util

import androidx.compose.runtime.Composable
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun formatMonthDay(timestamp: Long): String {
    val sdf = SimpleDateFormat("MM.dd", Locale.getDefault())
    return sdf.format(Date(timestamp))
}