package com.kintukka.hanger.ui

import com.kintukka.hanger.data.repo.TempUnit
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale
import kotlin.math.roundToInt

fun formatTemp(celsius: Double, unit: TempUnit): String = when (unit) {
    TempUnit.CELSIUS -> "${celsius.roundToInt()}°"
    TempUnit.FAHRENHEIT -> "${(celsius * 9 / 5 + 32).roundToInt()}°"
}

fun formatMoney(amount: Double, currency: String): String =
    if (amount >= 100 || amount == amount.roundToInt().toDouble()) "$currency${amount.roundToInt()}"
    else "$currency${"%.2f".format(Locale.UK, amount)}"

private val dayMonth = DateTimeFormatter.ofPattern("EEEE d MMMM", Locale.UK)
private val shortDay = DateTimeFormatter.ofPattern("EEE d MMM", Locale.UK)
private val monthYear = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.UK)

fun LocalDate.longLabel(): String = format(dayMonth)
fun LocalDate.shortLabel(): String = format(shortDay)
fun LocalDate.monthLabel(): String = format(monthYear)

fun LocalDate.relativeLabel(today: LocalDate = LocalDate.now()): String {
    val days = ChronoUnit.DAYS.between(this, today)
    return when {
        days == 0L -> "Today"
        days == 1L -> "Yesterday"
        days == -1L -> "Tomorrow"
        days in 2..13 -> "$days days ago"
        days in 14..59 -> "${days / 7} weeks ago"
        days in 60..729 -> "${days / 30} months ago"
        days < -1 -> "In ${-days} days"
        else -> "${days / 365} years ago"
    }
}

fun plural(count: Int, one: String, many: String = one + "s") = "$count ${if (count == 1) one else many}"
