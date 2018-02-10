package com.konradszewczuk.weatherapp.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object StringFormatter {
    private const val DAY_HOUR_MINUTE = "EEEE, HH:mm"
    private const val HOUR_MINUTE = "HH:mm"

    const val unitPercentage = "%"
    const val unitDegrees = "\u00b0"
    const val unitsMetresPerSecond = "m/s"
    const val unitDegreesCelsius = "\u2103"

    fun convertTimestampToDayOfTheWeek(timestamp: Int): String {
        val formatter = SimpleDateFormat("EEEE", Locale.ENGLISH)
        return formatter.format(Date(timestamp.toLong() * 1000))
    }

    fun convertTimestampToDayAndHourFormat(timestamp: Long): String {
        val formatter = SimpleDateFormat(DAY_HOUR_MINUTE, Locale.ENGLISH)
        return formatter.format(Date(timestamp))
    }

    fun convertTimestampToHourFormat(timestamp: Long, timeZone: String?): String {
        val formatter = SimpleDateFormat(HOUR_MINUTE)
        formatter.timeZone = TimeZone.getTimeZone(timeZone)
        return formatter.format(Date(timestamp * 1000))
    }

    fun convertToValueWithUnit(precision: Int, unitSymbol: String, value: Double?): String {
        return getPrecision(precision).format(value) + unitSymbol
    }

    private fun getPrecision(precision: Int): String {
        return "%." + precision + "f"
    }
}

