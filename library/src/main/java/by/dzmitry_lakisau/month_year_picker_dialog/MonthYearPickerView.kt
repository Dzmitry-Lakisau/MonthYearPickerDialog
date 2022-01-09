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
import android.util.AttributeSet
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.withStyledAttributes
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import kotlin.properties.Delegates

internal class MonthYearPickerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    private var rvMonths: RecyclerView
    private var rvYears: RecyclerView

    var monthsAdapter: MonthsAdapter
        private set
    var yearsAdapter: YearsAdapter
        private set

    private var tvSelectedMonth: TextView
    private var tvSelectedYear: TextView

    private var mode = MonthYearPickerDialog.Mode.MONTH_AND_YEAR

    private lateinit var monthFormat: SimpleDateFormat

    var onMonthSelected: ((Int) -> Unit)? = null
    var onYearSelected: ((Int) -> Unit)? = null

    init {
        inflate(context, R.layout.view_month_year_picker, this)

        var monthBackgroundColorSelected by Delegates.notNull<Int>()
        context.withStyledAttributes(attrs, R.styleable.MonthYearPickerDialog, defStyleAttr, defStyleRes) {
            monthBackgroundColorSelected = getColor(R.styleable.MonthYearPickerDialog_monthBackgroundColorSelected, android.R.attr.colorAccent)
        }

        rvMonths = findViewById(R.id.rv_months)
        rvYears = findViewById(R.id.rv_years)
        tvSelectedMonth = findViewById(R.id.tv_selected_month)
        tvSelectedYear = findViewById(R.id.tv_selected_year)

        tvSelectedMonth.isSelected = true
        tvSelectedYear.isSelected = false

        monthsAdapter = MonthsAdapter {
            tvSelectedMonth.text = getMonthName(it, monthFormat)
            if (mode != MonthYearPickerDialog.Mode.MONTH_ONLY) {
                rvMonths.visibility = INVISIBLE
                rvYears.visibility = VISIBLE
                tvSelectedMonth.isSelected = false
                tvSelectedYear.isSelected = true
            }
            onMonthSelected?.invoke(it)
        }
        rvMonths.addItemDecoration(MonthsAdapter.SelectedItemDecoration(monthBackgroundColorSelected))
        rvMonths.adapter = monthsAdapter

        yearsAdapter = YearsAdapter {
            monthsAdapter.setSelectedYear(it)
            tvSelectedYear.text = it.toString()
            tvSelectedMonth.isSelected = false
            tvSelectedYear.isSelected = true
            onYearSelected?.invoke(it)
        }
        rvYears.adapter = yearsAdapter

        tvSelectedMonth.setOnClickListener {
            if (!rvMonths.isVisible) {
                rvYears.visibility = INVISIBLE
                rvMonths.visibility = VISIBLE
                tvSelectedMonth.isSelected = true
                tvSelectedYear.isSelected = false
            }
        }
        tvSelectedYear.setOnClickListener {
            if (!rvYears.isVisible) {
                rvMonths.visibility = INVISIBLE
                rvYears.visibility = VISIBLE
                tvSelectedMonth.isSelected = false
                tvSelectedYear.isSelected = true
            }
        }
    }

    fun setAnnualMode(enableAnnualMode: Boolean) {
        monthsAdapter.isAnnualMode = enableAnnualMode
    }

    fun setMaxMonth(maxMonth: Int) {
        monthsAdapter.maxMonth = maxMonth
    }

    fun setMaxYear(maxYear: Int) {
        monthsAdapter.maxYear = maxYear
        yearsAdapter.maxYear = maxYear
    }

    fun setMinMonth(minMonth: Int) {
        monthsAdapter.minMonth = minMonth
    }

    fun setMinYear(minYear: Int) {
        monthsAdapter.minYear = minYear
        yearsAdapter.minYear = minYear
    }

    fun setMode(mode: MonthYearPickerDialog.Mode) {
        this.mode = mode
        when (mode) {
            MonthYearPickerDialog.Mode.MONTH_ONLY -> {
                tvSelectedMonth.gravity = Gravity.CENTER
                tvSelectedYear.visibility = GONE
            }
            MonthYearPickerDialog.Mode.YEAR_ONLY -> {
                tvSelectedMonth.visibility = GONE
                tvSelectedYear.gravity = Gravity.CENTER
                rvMonths.visibility = INVISIBLE
                rvYears.visibility = VISIBLE
            }
            MonthYearPickerDialog.Mode.MONTH_AND_YEAR -> {
            }
        }
    }

    fun setMonthFormat(format: SimpleDateFormat) {
        monthFormat = format
        monthsAdapter.monthFormat = format
    }

    fun setSelectedMonth(selectedMonth: Int) {
        monthsAdapter.selectedMonth = selectedMonth
        tvSelectedMonth.text = getMonthName(selectedMonth, monthFormat)
    }

    fun setSelectedYear(year: Int) {
        monthsAdapter.setSelectedYear(year)
        yearsAdapter.setSelectedYear(year)
        rvYears.post {
            rvYears.scrollToPosition(yearsAdapter.getPositionForYear(year))
        }
        tvSelectedYear.text = year.toString()
    }
}