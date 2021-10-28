package by.dzmitry_lakisau.month_year_picker_dialog

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.withStyledAttributes
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import java.util.*

internal class MonthsAdapter(private val onMonthSelectedListener: ((Int) -> Unit)? = null) : RecyclerView.Adapter<MonthsAdapter.ViewHolder>() {

    private var maxMonth = Calendar.DECEMBER
    private var minMonth = Calendar.JANUARY

    var maxMonthAndYear: Date? = null
    var minMonthAndYear: Date? = null

    lateinit var monthFormat: String

    private val months = listOf(
        Calendar.JANUARY,
        Calendar.FEBRUARY,
        Calendar.MARCH,
        Calendar.APRIL,
        Calendar.MAY,
        Calendar.JUNE,
        Calendar.JULY,
        Calendar.AUGUST,
        Calendar.SEPTEMBER,
        Calendar.OCTOBER,
        Calendar.NOVEMBER,
        Calendar.DECEMBER
    )

    lateinit var colorStateList: ColorStateList

    constructor(context: Context, attributeSet: AttributeSet, onMonthSelectedListener: ((Int) -> Unit)?) : this(onMonthSelectedListener) {
        context.withStyledAttributes(attributeSet, R.styleable.MonthPickerView) {
            val monthFontColorDisabled = context.getColorFromAttribute(this, R.styleable.MonthPickerView_monthFontColorDisabled, android.R.attr.textColorPrimary)
            val monthFontColor = context.getColorFromAttribute(this, R.styleable.MonthPickerView_monthFontColorNormal, android.R.attr.textColorPrimary)
            val monthFontColorSelected = context.getColorFromAttribute(this, R.styleable.MonthPickerView_monthFontColorSelected, android.R.attr.colorAccent)
            val states = arrayOf(
                intArrayOf(-android.R.attr.state_enabled),
                intArrayOf(android.R.attr.state_enabled, -android.R.attr.state_selected),
                intArrayOf(android.R.attr.state_enabled, android.R.attr.state_selected),
            )
            val colors = intArrayOf(monthFontColorDisabled, monthFontColor, monthFontColorSelected)
            colorStateList = ColorStateList(states, colors)
        }
    }

    private var selectedMonth = Calendar.getInstance()[Calendar.MONTH]
    private var selectedYear = Calendar.getInstance()[Calendar.YEAR]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_month, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBindViewHolder(months[position])
    }

    override fun getItemCount(): Int {
        return months.size
    }

    fun setMaxMonth(maxMonth: Int) {
        this.maxMonth = if (maxMonth <= Calendar.DECEMBER && maxMonth >= Calendar.JANUARY) {
            maxMonth
        } else {
            throw IllegalArgumentException("Month should be between 0 (Calender.JANUARY) and 11 (Calendar.DECEMBER)")
        }
    }

    fun setMinMonth(minMonth: Int) {
        this.minMonth = if (minMonth >= Calendar.JANUARY && minMonth <= Calendar.DECEMBER) {
            minMonth
        } else {
            throw IllegalArgumentException("Month should be between 0 (Calender.JANUARY) and 11 (Calendar.DECEMBER)")
        }
    }

    fun setSelectedMonth(selectedMonth: Int) {
        if (selectedMonth >= Calendar.JANUARY && selectedMonth <= Calendar.DECEMBER) {
            this.selectedMonth = selectedMonth
        } else {
            throw IllegalArgumentException("Month should be between 0 (Calender.JANUARY) and 11 (Calendar.DECEMBER)")
        }
    }

    fun setSelectedYear(year: Int) {
        selectedYear = year
        if (minMonthAndYear != null) {
            if (!isInRange(selectedMonth)) {
                selectedMonth = minMonthAndYear!!.month
                onMonthSelectedListener?.invoke(selectedMonth)
            }
            notifyDataSetChanged()
        }
        if (maxMonthAndYear != null) {
            if (!isInRange(selectedMonth)) {
                selectedMonth = maxMonthAndYear!!.month
                onMonthSelectedListener?.invoke(selectedMonth)
            }
            notifyDataSetChanged()
        }
    }

    private fun isInRange(month: Int): Boolean {
        return when {
            minMonthAndYear == null && maxMonthAndYear == null -> {
                month in minMonth..maxMonth
            }
            minMonthAndYear != null && maxMonthAndYear == null -> {
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.YEAR, selectedYear)
                calendar.set(Calendar.MONTH, month)
                calendar.time > minMonthAndYear
            }
            minMonthAndYear == null && maxMonthAndYear != null -> {
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.YEAR, selectedYear)
                calendar.set(Calendar.MONTH, month)
                calendar.time < maxMonthAndYear
            }
            else -> {
                val minCalendar = Calendar.getInstance()
                minCalendar.time = minMonthAndYear!!
                val maxCalendar = Calendar.getInstance()
                maxCalendar.time = maxMonthAndYear!!
                when (selectedYear) {
                    minCalendar[Calendar.YEAR] -> {
                        month >= minMonthAndYear!!.month
                    }
                    maxCalendar[Calendar.YEAR] -> {
                        month <= maxMonthAndYear!!.month
                    }
                    else -> {
                        return true
                    }
                }
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvName: MaterialTextView = itemView.findViewById(R.id.tv_name)

        init {
            tvName.setTextColor(colorStateList)
        }

        fun onBindViewHolder(item: Int) {
            tvName.text = getMonthName(item, monthFormat)

            itemView.isEnabled = isInRange(item)
            tvName.isEnabled = itemView.isEnabled

            itemView.isSelected = item == selectedMonth

            itemView.setOnClickListener {
                selectedMonth = item
                notifyDataSetChanged()
                onMonthSelectedListener?.invoke(item)
            }
        }
    }

}