package by.dzmitry_lakisau.month_year_picker_dialog

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.withStyledAttributes
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import by.dzmitry_lakisau.month_year_picker_dialog.MonthYearPickerDialog.OnMonthChangedListener
import by.dzmitry_lakisau.month_year_picker_dialog.MonthYearPickerDialog.OnYearChangedListener
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

    private var headerBackgroundColor: Int = 0
    private var headerTextColor: Int = 0
    private var headerTextColorSelected: Int = 0

    private var mode = MonthYearPickerDialog.Mode.MONTH_AND_YEAR

    private lateinit var monthFormat: SimpleDateFormat

    private var onMonthChangedListener: OnMonthChangedListener? = null
    private var onYearChangedListener: OnYearChangedListener? = null

    init {
        inflate(context, R.layout.view_month_year_picker, this)

        lateinit var monthTextColorStateList: ColorStateList
        var monthBackgroundColorSelected by Delegates.notNull<Int>()
        lateinit var yearTextColorStateList: ColorStateList
        context.withStyledAttributes(attrs, R.styleable.MonthYearPickerDialog, defStyleAttr, defStyleRes) {
            headerBackgroundColor = context.getColorFromAttribute(this, R.styleable.MonthYearPickerDialog_headerBackgroundColor, android.R.attr.colorAccent)
            headerTextColor = context.getColorFromAttribute(this, R.styleable.MonthYearPickerDialog_headerTextColor, android.R.attr.textColorSecondary)
            headerTextColorSelected = context.getColorFromAttribute(this, R.styleable.MonthYearPickerDialog_headerTextColorSelected, android.R.attr.textColorPrimary)

            val monthTextColorDisabled = context.getColorFromAttribute(this, R.styleable.MonthYearPickerDialog_monthTextColorDisabled, android.R.attr.textColorPrimary)
            val monthTextColor = context.getColorFromAttribute(this, R.styleable.MonthYearPickerDialog_monthTextColor, android.R.attr.textColorPrimary)
            val monthTextColorSelected = context.getColorFromAttribute(this, R.styleable.MonthYearPickerDialog_monthTextColorSelected, android.R.attr.colorAccent)
            val states = arrayOf(
                intArrayOf(-android.R.attr.state_enabled),
                intArrayOf(android.R.attr.state_enabled, -android.R.attr.state_selected),
                intArrayOf(android.R.attr.state_enabled, android.R.attr.state_selected),
            )
            val colors = intArrayOf(monthTextColorDisabled, monthTextColor, monthTextColorSelected)
            monthTextColorStateList = ColorStateList(states, colors)

            monthBackgroundColorSelected = context.getColorFromAttribute(this, R.styleable.MonthYearPickerDialog_monthBackgroundColorSelected, android.R.attr.colorAccent)

            val yearColor = context.getColorFromAttribute(this, R.styleable.MonthYearPickerDialog_yearColor, android.R.attr.textColorPrimary)
            val yearColorSelected = context.getColorFromAttribute(this, R.styleable.MonthYearPickerDialog_yearColorSelected, android.R.attr.colorAccent)
            yearTextColorStateList = ColorStateList(arrayOf(intArrayOf(-android.R.attr.state_selected), intArrayOf(android.R.attr.state_selected)), intArrayOf(yearColor, yearColorSelected))
        }

        rvMonths = findViewById(R.id.rv_months)
        rvYears = findViewById(R.id.rv_years)
        tvSelectedMonth = findViewById(R.id.tv_selected_month)
        tvSelectedYear = findViewById(R.id.tv_selected_year)

        tvSelectedMonth.setTextColor(headerTextColorSelected)
        tvSelectedYear.setTextColor(headerTextColor)
        findViewById<LinearLayout>(R.id.vg_header).setBackgroundColor(headerBackgroundColor)

        monthsAdapter = MonthsAdapter(monthTextColorStateList) {
            tvSelectedMonth.text = getMonthName(it, monthFormat)
            if (mode != MonthYearPickerDialog.Mode.MONTH_ONLY) {
                rvMonths.visibility = INVISIBLE
                rvYears.visibility = VISIBLE
                tvSelectedMonth.setTextColor(headerTextColor)
                tvSelectedYear.setTextColor(headerTextColorSelected)
            }
            onMonthChangedListener?.onMonthChanged(it)
        }
        rvMonths.addItemDecoration(MonthsAdapter.SelectedItemDecoration(monthBackgroundColorSelected))
        rvMonths.adapter = monthsAdapter

        yearsAdapter = YearsAdapter(yearTextColorStateList) {
            monthsAdapter.setSelectedYear(it)
            tvSelectedYear.text = it.toString()
            tvSelectedYear.setTextColor(headerTextColorSelected)
            tvSelectedMonth.setTextColor(headerTextColor)
            onYearChangedListener?.onYearChanged(it)
        }
        rvYears.adapter = yearsAdapter

        tvSelectedMonth.setOnClickListener {
            if (!rvMonths.isVisible) {
                rvYears.visibility = INVISIBLE
                rvMonths.visibility = VISIBLE
                tvSelectedYear.setTextColor(headerTextColor)
                tvSelectedMonth.setTextColor(headerTextColorSelected)
            }
        }
        tvSelectedYear.setOnClickListener {
            if (!rvYears.isVisible) {
                rvMonths.visibility = INVISIBLE
                rvYears.visibility = VISIBLE
                tvSelectedYear.setTextColor(headerTextColorSelected)
                tvSelectedMonth.setTextColor(headerTextColor)
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

    fun setOnMonthChangedListener(onMonthChangedListener: OnMonthChangedListener?) {
        this.onMonthChangedListener = onMonthChangedListener
    }

    fun setOnYearChangedListener(onYearChangedListener: OnYearChangedListener?) {
        this.onYearChangedListener = onYearChangedListener
    }
}