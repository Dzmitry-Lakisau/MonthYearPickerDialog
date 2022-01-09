/**
 * Copyright © 2021 Dzmitry Lakisau
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package by.dzmitry_lakisau.month_year_picker_dialog

import android.content.Context
import android.content.DialogInterface
import android.graphics.drawable.ColorDrawable
import androidx.annotation.IntRange
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.content.withStyledAttributes
import java.text.SimpleDateFormat
import java.util.*

class MonthYearPickerDialog private constructor(
    context: Context,
    @StyleRes
    themeResId: Int,
    private val onDateSetListener: OnDateSetListener?,
    positiveButtonText: CharSequence,
    negativeButtonText: CharSequence
) : AlertDialog(context, themeResId), DialogInterface.OnClickListener {

    private val monthYearPickerView: MonthYearPickerView = MonthYearPickerView(ContextThemeWrapper(context, themeResId))

    init {
        monthYearPickerView.context.withStyledAttributes(themeResId, R.styleable.MonthYearPickerDialog) {
            val backgroundColor = getColor(R.styleable.MonthYearPickerDialog_monthBackgroundColorSelected, android.R.attr.colorBackgroundFloating)
            window!!.setBackgroundDrawable(ColorDrawable(backgroundColor))
        }
        setView(monthYearPickerView)

        setButton(BUTTON_POSITIVE, positiveButtonText, this)
        setButton(BUTTON_NEGATIVE, negativeButtonText, this)
    }

    override fun onClick(dialog: DialogInterface, which: Int) {
        if (which == BUTTON_POSITIVE) {
            monthYearPickerView.clearFocus()
            onDateSetListener?.onDateSet(monthYearPickerView.yearsAdapter.selectedYear, monthYearPickerView.monthsAdapter.selectedMonth)
        }
    }

    /**
     * Builder for MonthYearPickerDialog.
     *
     * @param context parent context.
     * @param themeResId resource ID of the theme against which to inflate this dialog.
     * @param onDateSetListener the [listener][MonthYearPickerDialog.OnDateSetListener] to be invoked when user sets the date. Default value is null.
     * @param selectedYear initially selected year. Default value is current year.
     * @param selectedMonth initially selected month in range from [Calendar.JANUARY] to [Calendar.DECEMBER]. Default value is current month.
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

        private var positiveButtonText: CharSequence = context.getText(android.R.string.ok)
        private var negativeButtonText: CharSequence = context.getText(android.R.string.cancel)
        private var isAnnualMode = false
        private var minMonth = Calendar.JANUARY
        private var maxMonth = Calendar.DECEMBER
        private var minYear = 1970
        private var maxYear = Calendar.getInstance()[Calendar.YEAR]
        private var mode = Mode.MONTH_AND_YEAR
        private var onMonthSelectedListener: OnMonthSelectedListener? = null
        private var onYearSelectedListener: OnYearSelectedListener? = null
        private var monthFormat = SimpleDateFormat("LLLL", Locale.getDefault())

        /**
         * Enables or disables annual mode.
         *
         * @param enableAnnualMode If true, every year will have months from [setMinMonth] to [setMaxMonth] enabled;
         * if false, which is default, minimum year will have months from [setMinMonth] enabled, maximum year will have
         * months to [setMaxMonth] enabled and every year in between will have all months enabled.
         *
         * @return This [Builder][Builder] object to allow chaining of setter methods.
         */
        fun setAnnualMode(enableAnnualMode: Boolean): Builder {
            isAnnualMode = enableAnnualMode
            return this
        }

        /**
         * Sets maximum enabled month.
         *
         * @param month month in range from [Calendar.JANUARY] to [Calendar.DECEMBER].
         *
         * @return This [Builder][Builder] object to allow chaining of setter methods.
         */
        fun setMaxMonth(@IntRange(from = Calendar.JANUARY.toLong(), to = Calendar.DECEMBER.toLong()) month: Int): Builder {
            maxMonth = month
            return this
        }

        /**
         * Sets maximum available year.
         *
         * @return This [Builder][Builder] object to allow chaining of setter methods.
         */
        fun setMaxYear(year: Int): Builder {
            maxYear = year
            return this
        }

        /**
         * Sets minimum enabled month.
         *
         * @param month month in range from [Calendar.JANUARY] to [Calendar.DECEMBER].
         *
         * @return This [Builder][Builder] object to allow chaining of setter methods.
         */
        fun setMinMonth(@IntRange(from = Calendar.JANUARY.toLong(), to = Calendar.DECEMBER.toLong()) month: Int): Builder {
            minMonth = month
            return this
        }

        /**
         * Sets minimum available year.
         *
         * @return This [Builder][Builder] object to allow chaining of setter methods.
         */
        fun setMinYear(year: Int): Builder {
            minYear = year
            return this
        }

        /**
         * Sets UI mode of dialog.
         *
         * @param mode [Mode] that defines whether or not month and year pickers will be shown. Default value is [Mode.MONTH_AND_YEAR].
         *
         * @return This [Builder][Builder] object to allow chaining of setter methods.
         */
        fun setMode(mode: Mode): Builder {
            this.mode = mode
            return this
        }

        /**
         * Constructs a [SimpleDateFormat] from given pattern and
         * default date format symbols for given locale and sets it for usage when picking months.
         *
         * @param pattern pattern describing date and time format.
         * @param locale locale whose date format symbols should be used.
         *
         * @return This [Builder][Builder] object to allow chaining of setter methods.
         */
        fun setMonthFormat(pattern: String, locale: Locale = Locale.getDefault()): Builder {
            monthFormat = SimpleDateFormat(pattern, locale)
            return this
        }

        /**
         * Sets format for usage when picking months.
         *
         * @return This [Builder][Builder] object to allow chaining of setter methods.
         */
        fun setMonthFormat(format: SimpleDateFormat): Builder {
            monthFormat = format
            return this
        }

        /**
         * Sets text to display in the negative button.
         *
         * @return This [Builder][Builder] object to allow chaining of setter methods.
         */
        fun setNegativeButton(@StringRes textId: Int): Builder {
            negativeButtonText = context.getText(textId)
            return this
        }

        /**
         * Sets text to display in the negative button.
         *
         * @return This [Builder][Builder] object to allow chaining of setter methods.
         */
        fun setNegativeButton(text: CharSequence): Builder {
            negativeButtonText = text
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

        /**
         * Sets text to display in the positive button.
         *
         * @return This [Builder][Builder] object to allow chaining of setter methods.
         */
        fun setPositiveButton(@StringRes textId: Int): Builder {
            positiveButtonText = context.getText(textId)
            return this
        }

        /**
         * Sets text to display in the positive button.
         *
         * @return This [Builder][Builder] object to allow chaining of setter methods.
         */
        fun setPositiveButton(text: CharSequence): Builder {
            positiveButtonText = text
            return this
        }

        /**
         * Creates [MonthYearPickerDialog] after checking the entered parameters.
         *
         * @return [MonthYearPickerDialog].
         */
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

            val monthYearPickerDialog = MonthYearPickerDialog(context, themeResId, onDateSetListener, positiveButtonText, negativeButtonText)
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

    /**
     * Enum that defines UI mode of dialog.
     */
    enum class Mode {
        /** User can select month and year. */
        MONTH_AND_YEAR,

        /** User can select only month. Year picker will not be shown. */
        MONTH_ONLY,

        /** User can select only year. Month picker will not be shown. */
        YEAR_ONLY
    }
}