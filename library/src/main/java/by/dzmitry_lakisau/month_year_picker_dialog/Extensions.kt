package by.dzmitry_lakisau.month_year_picker_dialog

import android.content.Context
import android.content.res.TypedArray
import android.util.TypedValue
import java.text.SimpleDateFormat
import java.util.*

internal fun Context.getColorFromAttribute(typedArray: TypedArray, attribute: Int, fallbackAttribute: Int): Int {
    val color = typedArray.getColor(attribute, 0)
    return if (color != 0) {
        color
    } else {
        val outValue = TypedValue()
        theme.resolveAttribute(fallbackAttribute, outValue, true)
        outValue.data
    }
}

internal fun getMonthName(month: Int, monthFormat: SimpleDateFormat): String {
    val calendar = Calendar.getInstance()
    calendar[Calendar.DAY_OF_MONTH] = 1
    calendar[Calendar.MONTH] = month
    return monthFormat.format(calendar.time).replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}