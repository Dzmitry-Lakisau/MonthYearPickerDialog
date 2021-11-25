package by.dzmitry_lakisau.month_year_picker_dialog

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import androidx.annotation.IntRange
import androidx.annotation.StyleRes
import java.text.SimpleDateFormat
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

        monthYearPickerView = view.findViewById(R.id.vg_monthYearPickerView)
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
        private val onDateSetListener: OnDateSetListener? = null,
        private var selectedYear: Int = Calendar.getInstance()[Calendar.YEAR],
        @IntRange(from = Calendar.JANUARY.toLong(), to = Calendar.DECEMBER.toLong())
        private var selectedMonth: Int = Calendar.getInstance()[Calendar.MONTH]
    ) {

        private var isAnnualMode = false
        private var minMonth = Calendar.JANUARY
        private var maxMonth = Calendar.DECEMBER
        private var minYear = 1970
        private var maxYear = Calendar.getInstance()[Calendar.YEAR]
        private var mode = Mode.MONTH_AND_YEAR
        private var onMonthSelectedListener: OnMonthSelectedListener? = null
        private var onYearSelectedListener: OnYearSelectedListener? = null
        private var monthFormat = SimpleDateFormat("LLLL", Locale.getDefault())

        constructor(
            context: Context,
            onDateSetListener: OnDateSetListener? = null,
            selectedYear: Int = Calendar.getInstance()[Calendar.YEAR],
            @IntRange(from = Calendar.JANUARY.toLong(), to = Calendar.DECEMBER.toLong())
            selectedMonth: Int = Calendar.getInstance()[Calendar.MONTH]
        ) : this(
            context,
            R.style.MonthYearPickerDialogStyle_Default,
            onDateSetListener,
            selectedYear,
            selectedMonth
        )

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
        fun setMonthFormat(format: String, locale: Locale = Locale.getDefault()): Builder {
            monthFormat = SimpleDateFormat(format, locale)
            return this
        }

        /**
         * Date format for month name.
         *
         * @param format
         * @return Builder
         */
        fun setMonthFormat(format: SimpleDateFormat): Builder {
            monthFormat = format
            return this
        }

        /**
         * Sets callback that will be invoked when user has selected month.
         *
         * @param onMonthSelectedListener the [callback][MonthYearPickerDialog.OnMonthSelectedListener] that will run.
         *
         * @return This [Builder][Builder] object to allow chaining of setter methods.
         */
        fun setOnMonthSelectedListener(onMonthSelectedListener: OnMonthSelectedListener): Builder {
            this.onMonthSelectedListener = onMonthSelectedListener
            return this
        }

        /**
         * Sets callback that will be invoked when user has selected year.
         *
         * @param onYearSelectedListener the [callback][MonthYearPickerDialog.OnYearSelectedListener] that will run.
         *
         * @return This [Builder][Builder] object to allow chaining of setter methods.
         */
        fun setOnYearSelectedListener(onYearSelectedListener: OnYearSelectedListener): Builder {
            this.onYearSelectedListener = onYearSelectedListener
            return this
        }

        fun setMode(mode: Mode): Builder {
            this.mode = mode
            return this
        }

        fun build(): MonthYearPickerDialog {
            require(minMonth >= Calendar.JANUARY && minMonth <= Calendar.DECEMBER) {
                "Minimum month ($minMonth) is not in range from 0 (Calendar.JANUARY) to 11 (Calendar.DECEMBER)"
            }
            require(maxMonth >= Calendar.JANUARY && maxMonth <= Calendar.DECEMBER) {
                "Maximum month ($maxMonth) is not in range from 0 (Calendar.JANUARY) to 11 (Calendar.DECEMBER)"
            }
            require(selectedMonth >= Calendar.JANUARY && selectedMonth <= Calendar.DECEMBER) {
                "Selected month ($selectedMonth) is not in range from 0 (Calendar.JANUARY) to 11 (Calendar.DECEMBER)"
            }

            require(minYear <= maxYear) {
                "Minimum year ($minYear) is larger than maximum year ($maxYear)"
            }
            require(selectedYear in minYear..maxYear) {
                "Selected year ($selectedYear) is not in range from minimum ($minYear) to maximum ($maxYear) year"
            }
            if (isAnnualMode || minYear == maxYear) {
                require(minMonth <= maxMonth) {
                    "Minimum month ($minMonth) is larger than maximum month ($maxMonth)"
                }
                require(selectedMonth in minMonth..maxMonth) {
                    "Selected month ($selectedMonth) is not in range from minimum ($minMonth) to maximum ($maxMonth) month"
                }
            } else {
                require(selectedYear * 12 + selectedMonth in minYear * 12 + minMonth..maxYear * 12 + maxMonth) {
                    "Selected month and year ($selectedMonth.$selectedYear) is not in range from ($minMonth.$minYear) to ($maxMonth.$maxYear)"
                }
            }

            val monthYearPickerDialog = MonthYearPickerDialog(context, themeResId, onDateSetListener)
            monthYearPickerDialog.monthYearPickerView.apply {
                onMonthSelected = { onMonthSelectedListener?.onMonthSelected(it) }
                onYearSelected = { onYearSelectedListener?.onYearSelected(it) }
                setAnnualMode(isAnnualMode)
                setMinMonth(minMonth)
                setMinYear(minYear)
                setMaxMonth(maxMonth)
                setMaxYear(maxYear)
                setMode(mode)
                setMonthFormat(monthFormat)
                setSelectedMonth(selectedMonth)
                setSelectedYear(selectedYear)
            }
            return monthYearPickerDialog
        }
    }

    /**
     * Interface definition for a callback to be invoked when user has selected month and year.
     */
    fun interface OnDateSetListener {
        /**
         * Called when user has selected month and year.
         *
         * @param year selected year.
         * @param month selected month in range from [Calendar.JANUARY] to [Calendar.DECEMBER].
         */
        fun onDateSet(year: Int, month: Int)
    }

    /**
     * Interface definition for a callback to be invoked when user has selected month.
     */
    fun interface OnMonthSelectedListener {
        /**
         * Called when user has selected month.
         *
         * @param month selected month in range from [Calendar.JANUARY] to [Calendar.DECEMBER].
         */
        fun onMonthSelected(month: Int)
    }

    /**
     * Interface definition for a callback to be invoked when user has selected year.
     */
    fun interface OnYearSelectedListener {
        /**
         * Called when user has selected year.
         *
         * @param year selected year.
         */
        fun onYearSelected(year: Int)
    }

    enum class Mode {
        MONTH_ONLY,
        YEAR_ONLY,
        MONTH_AND_YEAR
    }
}