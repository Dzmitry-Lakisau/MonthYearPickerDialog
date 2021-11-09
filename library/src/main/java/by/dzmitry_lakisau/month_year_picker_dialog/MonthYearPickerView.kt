package by.dzmitry_lakisau.month_year_picker_dialog

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.withStyledAttributes
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.dzmitry_lakisau.month_year_picker_dialog.MonthYearPickerDialog.OnMonthChangedListener
import by.dzmitry_lakisau.month_year_picker_dialog.MonthYearPickerDialog.OnYearChangedListener
import java.util.*
import kotlin.properties.Delegates

internal class MonthYearPickerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    private var rvMonths: RecyclerView
    private var rvYears: RecyclerView

    private var monthsAdapter: MonthsAdapter
    private var yearsAdapter: YearsAdapter

    private var tvSelectedMonth: TextView
    private var tvSelectedYear: TextView
    private var tvTitle: TextView

    private var headerBackgroundColor: Int = 0
    private var headerFontColorNormal: Int = 0
    private var headerFontColorSelected: Int = 0

    var selectedMonth = 0
        private set
    var selectedYear = 0
        private set

    private var showMonthOnly = false

    private lateinit var monthFormat: String

    private var onMonthChangedListener: OnMonthChangedListener? = null
    private var onYearChangedListener: OnYearChangedListener? = null

    fun init(year: Int, month: Int) {
        selectedYear = year
        selectedMonth = month
        monthsAdapter.setSelectedYear(year)
    }

    fun setAnnualMode(enableAnnualMode: Boolean) {
        monthsAdapter.isAnnualMode = enableAnnualMode
    }

    fun setMaxMonth(maxMonth: Int) {
        monthsAdapter.setMaxMonth(maxMonth)
    }

    fun setMaxYear(maxYear: Int) {
        monthsAdapter.maxYear = maxYear
        yearsAdapter.maxYear = maxYear
    }

    fun setMinMonth(minMonth: Int) {
        monthsAdapter.setMinMonth(minMonth)
    }

    fun setMinYear(minYear: Int) {
        monthsAdapter.minYear = minYear
        yearsAdapter.minYear = minYear
    }

    fun setMonthFormat(format: String) {
        monthFormat = format
        monthsAdapter.monthFormat = format
    }

    fun setSelectedMonth(selectedMonth: Int) {
        monthsAdapter.setSelectedMonth(selectedMonth)
        tvSelectedMonth.text = getMonthName(selectedMonth, monthFormat)
    }

    fun setSelectedYear(year: Int) {
        yearsAdapter.setSelectedYear(year)
        rvYears.post {
            rvYears.scrollToPosition(yearsAdapter.getPositionForYear(year))
        }
        tvSelectedYear.text = year.toString()
    }

    fun setTitle(dialogTitle: String?) {
        if (dialogTitle != null && dialogTitle.trim { it <= ' ' }.isNotEmpty()) {
            tvTitle.text = dialogTitle
            tvTitle.visibility = VISIBLE
        } else {
            tvTitle.visibility = GONE
        }
    }

    fun setOnMonthChangedListener(onMonthChangedListener: OnMonthChangedListener?) {
        this.onMonthChangedListener = onMonthChangedListener
    }

    fun setOnYearChangedListener(onYearChangedListener: OnYearChangedListener?) {
        this.onYearChangedListener = onYearChangedListener
    }

    fun showMonthOnly() {
        showMonthOnly = true
        tvSelectedYear.visibility = GONE
    }

    fun showYearOnly() {
        rvMonths.visibility = INVISIBLE
        rvYears.visibility = VISIBLE
        tvSelectedMonth.visibility = GONE
        tvSelectedYear.setTextColor(headerFontColorSelected)
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.view_month_year_picker, this)

        lateinit var monthTextColorStateList: ColorStateList
        var selectedMonthBackgroundColor by Delegates.notNull<Int>()
        lateinit var yearTextColorStateList: ColorStateList
        context.withStyledAttributes(attrs, R.styleable.MonthPickerView, defStyleAttr, defStyleRes) {
            headerBackgroundColor = context.getColorFromAttribute(this, R.styleable.MonthPickerView_headerBgColor, android.R.attr.colorAccent)
            headerFontColorNormal = context.getColorFromAttribute(this, R.styleable.MonthPickerView_headerFontColorNormal, android.R.attr.textColorSecondary)
            headerFontColorSelected = context.getColorFromAttribute(this, R.styleable.MonthPickerView_headerFontColorSelected, android.R.attr.textColorPrimary)

            val monthFontColorDisabled = context.getColorFromAttribute(this, R.styleable.MonthPickerView_monthFontColorDisabled, android.R.attr.textColorPrimary)
            val monthFontColor = context.getColorFromAttribute(this, R.styleable.MonthPickerView_monthFontColorNormal, android.R.attr.textColorPrimary)
            val monthFontColorSelected = context.getColorFromAttribute(this, R.styleable.MonthPickerView_monthFontColorSelected, android.R.attr.colorAccent)
            val states = arrayOf(
                intArrayOf(-android.R.attr.state_enabled),
                intArrayOf(android.R.attr.state_enabled, -android.R.attr.state_selected),
                intArrayOf(android.R.attr.state_enabled, android.R.attr.state_selected),
            )
            val colors = intArrayOf(monthFontColorDisabled, monthFontColor, monthFontColorSelected)
            monthTextColorStateList = ColorStateList(states, colors)

            selectedMonthBackgroundColor = context.getColorFromAttribute(this, R.styleable.MonthPickerView_selectedMonthBackgroundColor, android.R.attr.colorAccent)

            val yearColor = context.getColorFromAttribute(this, R.styleable.MonthPickerView_yearColor, android.R.attr.textColorPrimary)
            val yearSelectedColor = context.getColorFromAttribute(this, R.styleable.MonthPickerView_yearSelectedColor, android.R.attr.colorAccent)
            yearTextColorStateList = ColorStateList(arrayOf(intArrayOf(-android.R.attr.state_selected), intArrayOf(android.R.attr.state_selected)), intArrayOf(yearColor, yearSelectedColor))
        }

        rvMonths = findViewById(R.id.rv_months)
        rvYears = findViewById(R.id.rv_years)
        tvSelectedMonth = findViewById(R.id.tv_selected_month)
        tvSelectedYear = findViewById(R.id.tv_selected_year)
        tvTitle = findViewById(R.id.title)
        val header = findViewById<LinearLayout>(R.id.header)

        tvSelectedMonth.setTextColor(headerFontColorSelected)
        tvSelectedYear.setTextColor(headerFontColorNormal)
        header.setBackgroundColor(headerBackgroundColor)

        monthsAdapter = MonthsAdapter(monthTextColorStateList) {
            selectedMonth = it
            tvSelectedMonth.text = getMonthName(it, monthFormat)
            if (!showMonthOnly) {
                rvMonths.visibility = INVISIBLE
                rvYears.visibility = VISIBLE
                tvSelectedMonth.setTextColor(headerFontColorNormal)
                tvSelectedYear.setTextColor(headerFontColorSelected)
            }
            onMonthChangedListener?.onMonthChanged(it)
        }
        rvMonths.layoutManager = GridLayoutManager(context, 4)
        rvMonths.addItemDecoration(MonthsAdapter.SelectedItemDecoration(selectedMonthBackgroundColor))
        rvMonths.adapter = monthsAdapter

        yearsAdapter = YearsAdapter(yearTextColorStateList) {
            selectedYear = it
            monthsAdapter.setSelectedYear(it)
            tvSelectedYear.text = selectedYear.toString()
            tvSelectedYear.setTextColor(headerFontColorSelected)
            tvSelectedMonth.setTextColor(headerFontColorNormal)
            onYearChangedListener?.onYearChanged(it)
        }
        rvYears.adapter = yearsAdapter

        tvSelectedMonth.setOnClickListener {
            if (!rvMonths.isVisible) {
                rvYears.visibility = INVISIBLE
                rvMonths.visibility = VISIBLE
                tvSelectedYear.setTextColor(headerFontColorNormal)
                tvSelectedMonth.setTextColor(headerFontColorSelected)
            }
        }
        tvSelectedYear.setOnClickListener {
            if (!rvYears.isVisible) {
                rvMonths.visibility = INVISIBLE
                rvYears.visibility = VISIBLE
                tvSelectedYear.setTextColor(headerFontColorSelected)
                tvSelectedMonth.setTextColor(headerFontColorNormal)
            }
        }
    }
}