package by.dzmitry_lakisau.month_year_picker_dialog

import java.text.SimpleDateFormat
import java.util.*

internal fun getMonthName(month: Int, monthFormat: SimpleDateFormat): String {
    val calendar = Calendar.getInstance()
    calendar[Calendar.DAY_OF_MONTH] = 1
    calendar[Calendar.MONTH] = month
    return monthFormat.format(calendar.time).replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}