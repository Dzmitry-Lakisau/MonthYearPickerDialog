package by.dzmitry_lakisau.month_year_picker_dialog

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.IntRange
import androidx.annotation.StyleRes
import java.util.*

class MonthYearPickerDialog private constructor(
    context: Context,
    @StyleRes
    theme: Int,
    private val onDateSetListener: OnDateSetListener?
) : AlertDialog(context, theme), DialogInterface.OnClickListener {

    private val monthYearPickerView: MonthYearPickerView

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_month_year_picker, null)
        setView(view)

        setButton(BUTTON_POSITIVE, context.getString(android.R.string.ok), this)
        setButton(BUTTON_NEGATIVE, context.getString(android.R.string.cancel), this)

        monthYearPickerView = view.findViewById<View>(R.id.monthPicker) as MonthYearPickerView
    }

    override fun onClick(dialog: DialogInterface, which: Int) {
        if (which == BUTTON_POSITIVE) {
            monthYearPickerView.clearFocus()
            onDateSetListener?.onDateSet(monthYearPickerView.yearsAdapter.selectedYear, monthYearPickerView.monthsAdapter.selectedMonth)
        }
    }

    /**
     * Build a Dialog with month and year with given context.
     *
     * @param context  Context: the parent context
     * @param onDateSetListener MonthPickerDialog.OnDateSetListener: the listener to call
     * when the user sets the date
     * @param year     the initially selected year
     * @param month    the initially selected month (0-11 for compatibility with
     * [Calendar]Calender.MONTH or Calendar.JANUARY, Calendar.FEBRUARY etc)
     */
    class Builder(
        private val context: Context,
        @StyleRes
        private val themeResId: Int,
        private val onDateSetListener: OnDateSetListener?,
        private var selectedYear: Int,
        @IntRange(from = Calendar.JANUARY.toLong(), to = Calendar.DECEMBER.toLong())
        private var selectedMonth: Int
    ) {

        private var isAnnualMode = false
        private var minMonth = Calendar.JANUARY
        private var maxMonth = Calendar.DECEMBER
        private var minYear = 1970
        private var maxYear = Calendar.getInstance()[Calendar.YEAR]
        private var monthOnly = false
        private var yearOnly = false
        private var title: String? = null
        private var onYearChangedListener: OnYearChangedListener? = null
        private var onMonthChangedListener: OnMonthChangedListener? = null
        private var monthFormat: String = "LLLL"

        fun setAnnualMode(enableAnnualMode: Boolean): Builder {
            isAnnualMode = enableAnnualMode
            return this
        }

        /**
         * Maximum enabled month in picker (0-11 for compatibility with Calender.MONTH or
         * Calendar.JANUARY, Calendar.FEBRUARY etc).
         *
         * @param maxMonth
         * @return
         */
        fun setMaxMonth(@IntRange(from = Calendar.JANUARY.toLong(), to = Calendar.DECEMBER.toLong()) maxMonth: Int): Builder {
            this.maxMonth = maxMonth
            return this
        }

        /**
         * Ending year in the picker.
         *
         * @param maxYear
         * @return Builder
         */
        fun setMaxYear(maxYear: Int): Builder {
            this.maxYear = maxYear
            return this
        }

        /**
         * Minimum enable month in picker (0-11 for compatibility with Calender.MONTH or
         * Calendar.JANUARY, Calendar.FEBRUARY etc).
         *
         * @param minMonth
         * @return Builder
         */
        fun setMinMonth(@IntRange(from = Calendar.JANUARY.toLong(), to = Calendar.DECEMBER.toLong()) minMonth: Int): Builder {
            this.minMonth = minMonth
            return this
        }

        /**
         * Starting year in the picker.
         *
         * @param minYear
         * @return Builder
         */
        fun setMinYear(minYear: Int): Builder {
            this.minYear = minYear
            return this
        }

        /**
         * Date format for month name.
         *
         * @param format
         * @return Builder
         */
        fun setMonthFormat(format: String): Builder {
            monthFormat = format
            return this
        }

        /**
         * Sets the callback that will be called when user click on any month.
         *
         * @param onMonthChangedListener
         * @return Builder
         */
        fun setOnMonthChangedListener(onMonthChangedListener: OnMonthChangedListener): Builder {
            this.onMonthChangedListener = onMonthChangedListener
            return this
        }

        /**
         * Sets the callback that will be called when the user select any year.
         *
         * @param onYearChangedListener
         * @return Builder
         */
        fun setOnYearChangedListener(onYearChangedListener: OnYearChangedListener): Builder {
            this.onYearChangedListener = onYearChangedListener
            return this
        }

        /**
         * Initially selected month (0-11 for compatibility with Calender.MONTH or
         * Calendar.JANUARY, Calendar.FEBRUARY etc).
         *
         * @param selectedMonth
         * @return Builder
         */
        fun setSelectedMonth(@IntRange(from = Calendar.JANUARY.toLong(), to = Calendar.DECEMBER.toLong()) selectedMonth: Int): Builder {
            require(selectedMonth >= Calendar.JANUARY && selectedMonth <= Calendar.DECEMBER) { "Month should be between 0 (Calender.JANUARY) and 11 (Calendar.DECEMBER)" }
            this.selectedMonth = selectedMonth
            return this
        }

        /**
         * Initially selected year (0-11 for compatibility with Calender.MONTH or
         * Calendar.JANUARY, Calendar.FEBRUARY etc).
         *
         * @param selectedYear
         * @return Builder
         */
        fun setSelectedYear(selectedYear: Int): Builder {
            this.selectedYear = selectedYear
            return this
        }

        /**
         * Set the title to the picker.
         *
         * @param title
         * @return Builder
         */
        fun setTitle(title: String?): Builder {
            this.title = title
            return this
        }

        /**
         * User can select month only. Year won't be shown to user once user select the month.
         *
         * @return Builder
         */
        fun showMonthOnly(): Builder {
            yearOnly = false
            monthOnly = true
            return this
        }

        /**
         * User can select year only. Month won't be shown to user once user select the month.
         *
         * @return Builder
         */
        fun showYearOnly(): Builder {
            monthOnly = false
            yearOnly = true
            return this
        }

        fun build(): MonthYearPickerDialog {
            require(minMonth >= Calendar.JANUARY && minMonth <= Calendar.DECEMBER) {
                "Minimum month ($minMonth) should be between 0 (Calendar.JANUARY) and 11 (Calendar.DECEMBER)"
            }
            require(maxMonth >= Calendar.JANUARY && maxMonth <= Calendar.DECEMBER) {
                "Maximum month ($maxMonth) should be between 0 (Calendar.JANUARY) and 11 (Calendar.DECEMBER)"
            }
            require(selectedMonth >= Calendar.JANUARY && selectedMonth <= Calendar.DECEMBER) {
                "Selected month ($selectedMonth) should be between 0 (Calendar.JANUARY) and 11 (Calendar.DECEMBER)"
            }

            require(minYear <= maxYear) {
                "Minimum year ($minYear) should always smaller than maximum year ($maxYear)"
            }
            require(selectedYear in minYear..maxYear) {
                "Selected year ($selectedYear) should always in between minimum ($minYear) and maximum ($maxYear) year"
            }
            if (isAnnualMode || minYear == maxYear) {
                require(minMonth <= maxMonth) {
                    "Minimum month ($minMonth) should always be smaller than maximum month ($maxMonth)"
                }
                require(selectedMonth in minMonth..maxMonth) {
                    "Selected month ($selectedMonth) should always in between minimum ($minMonth) and maximum ($maxMonth) month"
                }
            } else {
                require(selectedYear * 12 + selectedMonth in minYear * 12 + minMonth..maxYear * 12 + maxMonth) {
                    "Selected month and year ($selectedMonth.$selectedYear) is not in range from ($minMonth.$minYear) to ($maxMonth.$maxYear)"
                }
            }

            val monthYearPickerDialog = MonthYearPickerDialog(context, themeResId, onDateSetListener)
            val monthYearPickerView = monthYearPickerDialog.monthYearPickerView

            if (monthOnly) {
                monthYearPickerView.showMonthOnly()
                minYear = 0
                maxYear = 0
                selectedYear = 0
            } else if (yearOnly) {
                monthYearPickerView.showYearOnly()
                minMonth = 0
                maxMonth = 0
                selectedMonth = 0
            }
            monthYearPickerView.setAnnualMode(isAnnualMode)
            monthYearPickerView.setMonthFormat(monthFormat)
            monthYearPickerView.setMinMonth(minMonth)
            monthYearPickerView.setMaxMonth(maxMonth)
            monthYearPickerView.setMinYear(minYear)
            monthYearPickerView.setMaxYear(maxYear)
            monthYearPickerView.setSelectedMonth(selectedMonth)
            monthYearPickerView.setSelectedYear(selectedYear)
            monthYearPickerView.setOnMonthChangedListener(onMonthChangedListener)
            monthYearPickerView.setOnYearChangedListener(onYearChangedListener)
            monthYearPickerView.setTitle(title?.trim { it <= ' ' })
            return monthYearPickerDialog
        }
    }

    /**
     * The callback used to indicate the user is done selecting month.
     */
    interface OnDateSetListener {
        /**
         * @param year  The year that was set.
         * @param month The month that was set (0-11) for compatibility with [Calendar].
         */
        fun onDateSet(year: Int, month: Int)
    }

    /**
     * The callback used to indicate the user click on month
     */
    interface OnMonthChangedListener {
        /**
         * @param selectedMonth The month that was set (0-11) for compatibility
         * with [Calendar].
         */
        fun onMonthChanged(selectedMonth: Int)
    }

    /**
     * The callback used to indicate the user click on year.
     */
    interface OnYearChangedListener {
        /**
         * Called upon a year change.
         *
         * @param selectedYear The year that was set.
         */
        fun onYearChanged(selectedYear: Int)
    }
}