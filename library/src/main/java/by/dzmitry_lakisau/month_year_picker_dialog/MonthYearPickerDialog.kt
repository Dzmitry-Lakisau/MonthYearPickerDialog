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
    private val onDateSetListener: OnDateSetListener,
    year: Int,
    monthOfYear: Int
) : AlertDialog(context, theme), DialogInterface.OnClickListener {

    private val monthYearPickerView: MonthYearPickerView

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_month_year_picker, null)
        setView(view)

        setButton(BUTTON_POSITIVE, context.getString(android.R.string.ok), this)
        setButton(BUTTON_NEGATIVE, context.getString(android.R.string.cancel), this)

        monthYearPickerView = view.findViewById<View>(R.id.monthPicker) as MonthYearPickerView
        monthYearPickerView.init(year, monthOfYear)
    }

    override fun onClick(dialog: DialogInterface, which: Int) {
        if (which == BUTTON_POSITIVE) {
            monthYearPickerView.clearFocus()
            onDateSetListener.onDateSet(monthYearPickerView.selectedMonth, monthYearPickerView.selectedYear)
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
        private val onDateSetListener: OnDateSetListener,
        year: Int,
        @IntRange(from = Calendar.JANUARY.toLong(), to = Calendar.DECEMBER.toLong()) month: Int
    ) {

        private var isAnnualMode = false
        private var selectedMonth = 0
        private var selectedYear = 0
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

        init {
            if (month >= Calendar.JANUARY && month <= Calendar.DECEMBER) {
                selectedMonth = month
            } else {
                throw IllegalArgumentException("Month should be between 0 (Calender.JANUARY) and 11 (Calendar.DECEMBER)")
            }
            if (year >= minYear) {
                selectedYear = year
            } else {
                throw IllegalArgumentException("Selected year should be greater than $minYear")
            }
        }

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
            return if (minMonth >= Calendar.JANUARY && minMonth <= Calendar.DECEMBER) {
                this.minMonth = minMonth
                this
            } else {
                throw IllegalArgumentException("Month should be between 0 (Calender.JANUARY) and 11 (Calendar.DECEMBER)")
            }
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
         * Set the Minimum, maximum enabled months and starting , ending years.
         *
         * @param minMonth minimum enabled month in picker
         * @param maxMonth maximum enabled month in picker
         * @param minYear  starting year
         * @param maxYear  ending year
         * @return
         */
        fun setMonthAndYearRange(
            @IntRange(from = Calendar.JANUARY.toLong(), to = Calendar.DECEMBER.toLong()) minMonth: Int,
            @IntRange(from = Calendar.JANUARY.toLong(), to = Calendar.DECEMBER.toLong()) maxMonth: Int,
            minYear: Int, maxYear: Int
        ): Builder {
            if (minMonth >= Calendar.JANUARY && minMonth <= Calendar.DECEMBER && maxMonth >= Calendar.JANUARY && maxMonth <= Calendar.DECEMBER) {
                this.minMonth = minMonth
                this.maxMonth = maxMonth
            } else {
                throw IllegalArgumentException("Month range should be between 0 (Calender.JANUARY) to 11 (Calendar.DECEMBER)")
            }
            if (minYear <= maxYear) {
                this.minYear = minYear
                this.maxYear = maxYear
            } else {
                throw IllegalArgumentException("Minimum year should be less then Maximum year")
            }
            return this
        }

        /**
         * Minimum and Maximum enable month in picker (0-11 for compatibility with Calender.MONTH or
         * Calendar.JANUARY, Calendar.FEBRUARY etc).
         *
         * @param minMonth minimum enabled month.
         * @param maxMonth maximum enabled month.
         * @return Builder
         */
        fun setMonthRange(
            @IntRange(from = Calendar.JANUARY.toLong(), to = Calendar.DECEMBER.toLong()) minMonth: Int,
            @IntRange(from = Calendar.JANUARY.toLong(), to = Calendar.DECEMBER.toLong()) maxMonth: Int
        ): Builder {
            return if (minMonth >= Calendar.JANUARY && minMonth <= Calendar.DECEMBER && maxMonth >= Calendar.JANUARY && maxMonth <= Calendar.DECEMBER) {
                this.minMonth = minMonth
                this.maxMonth = maxMonth
                this
            } else {
                throw IllegalArgumentException("Month range should be between 0 (Calender.JANUARY) to 11 (Calendar.DECEMBER)")
            }
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
         * Starting and ending year show in picker
         *
         * @param minYear starting year
         * @param maxYear ending year
         * @return
         */
        fun setYearRange(minYear: Int, maxYear: Int): Builder {
            return if (minYear <= maxYear) {
                this.minYear = minYear
                this.maxYear = maxYear
                this
            } else {
                throw IllegalArgumentException("Minimum year should be less then Maximum year")
            }
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
            /** TODO fix and add checks
            require(minMonth <= maxMonth) { "Minimum month should always smaller then maximum month." }
            require(minYear <= maxYear) { "Minimum year should always smaller then maximum year." }
            require(!(selectedMonth < minMonth || selectedMonth > maxMonth)) { "Selected month should always in between minimum and maximum month." }
            require(!(selectedYear < minYear || selectedYear > maxYear)) { "Selected year should always in between minimum year and maximum year." }
             */

            val monthYearPickerDialog = MonthYearPickerDialog(context, themeResId, onDateSetListener, selectedYear, selectedMonth)
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
         * @param selectedMonth The month that was set (0-11) for compatibility with [Calendar].
         * @param selectedYear  The year that was set.
         */
        fun onDateSet(selectedMonth: Int, selectedYear: Int)
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