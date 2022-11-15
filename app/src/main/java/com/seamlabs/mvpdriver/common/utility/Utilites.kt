package com.seamlabs.mvpdriver.common.utility

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.format.DateUtils
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


fun Activity.callIntent(phoneNumber:String){
    val intent = Intent(
        Intent.ACTION_DIAL,
        Uri.parse("tel:$phoneNumber")
    )
    startActivity(intent)
}

fun Context.getDifferenceBetweenDates(
    customStartDate: String,
    customEndDate: String,
    numOfDay: (String) -> Unit,
) {
    try {

        var format = SimpleDateFormat("dd/MM/yyyy", Locale(UserPreferences.getUserLanguage(this)))

        val start = format.parse(customStartDate)

        val end = format.parse(customEndDate)

        format = SimpleDateFormat("dd-MM-yyyy", Locale(UserPreferences.getUserLanguage(this)))


        val startDate = format.format(start)
        val endDate = format.format(end)


        val diff = format.parse(endDate).time - format.parse(startDate).time

        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24
        numOfDay(days.toString())

    } catch (e: ParseException) {
        println(e.message)
    }
}

fun Context.customFormatDate(
    dateString: String,
    currentFormat: String,
    newFormat: String,
    newDate: (String) -> Unit,
) {
    try {
        var format = SimpleDateFormat(currentFormat, Locale(UserPreferences.getUserLanguage(this)))
        val date = format.parse(dateString)
        format = SimpleDateFormat(newFormat, Locale(UserPreferences.getUserLanguage(this)))
        val tripTime = format.format(date)
        newDate(tripTime)
    } catch (e: ParseException) {
    }
}

fun Context.relativeTime(date:String,relativeTime:(String)->Unit){
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale(UserPreferences.getUserLanguage(this)))
    val date: Date = format.parse(date)
    val niceDateStr: String = DateUtils.getRelativeTimeSpanString(date.time,
        Calendar.getInstance().timeInMillis,
        DateUtils.MINUTE_IN_MILLIS).toString()
    relativeTime(niceDateStr)
}