package com.example.storiesw.utils

import android.content.Context
import com.example.storiesw.R
import com.example.storiesw.utils.Constants.MULTIPART_FORM_DATA
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun String.isEmailCorrect(): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.getTimeAgo(context: Context): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    val date = dateFormat.parse(this)
    val now = Date()
    val seconds = ((now.time - (date?.time ?: Date().time)) / 1000).toInt()
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24
    return when {
        days > 0 -> context.getString(R.string.label_day, days)
        hours > 0 -> context.getString(R.string.label_hour, hours)
        minutes > 0 -> context.getString(R.string.label_minutes, minutes)
        else -> context.getString(R.string.label_seconds, seconds)
    }
}

fun String.toRequestBody(): RequestBody {
    return this.toRequestBody(MULTIPART_FORM_DATA.toMediaTypeOrNull())
}

fun Float?.toRequestBody(): RequestBody? {
    if (this == null) return null
    return this.toString().toRequestBody(MULTIPART_FORM_DATA.toMediaTypeOrNull())
}