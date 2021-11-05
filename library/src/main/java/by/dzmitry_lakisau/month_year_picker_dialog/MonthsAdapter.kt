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

    var isAnnualMode = false
    private var maxMonth = Calendar.DECEMBER
    private var minMonth = Calendar.JANUARY
    var minYear = 0
    var maxYear = 0

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
        if (!isAnnualMode) {
            if (selectedYear == minYear) {
                if (selectedMonth < minMonth) {
                    selectedMonth = minMonth
                    onMonthSelectedListener?.invoke(selectedMonth)
                }
                notifyItemRangeChanged(Calendar.JANUARY, Calendar.DECEMBER + 1)
                return
            }
            if (selectedYear == maxYear) {
                if (selectedMonth > maxMonth) {
                    selectedMonth = maxMonth
                    onMonthSelectedListener?.invoke(selectedMonth)
                }
                notifyItemRangeChanged(Calendar.JANUARY, Calendar.DECEMBER + 1)
            }
        }
    }

    private fun isInRange(month: Int): Boolean {
        return when {
            isAnnualMode -> {
                month in minMonth..maxMonth
            }
            minYear == maxYear -> {
                month in minMonth..maxMonth
            }
            selectedYear == maxYear -> {
                month <= maxMonth
            }
            selectedYear == minYear -> {
                month >= minMonth
            }
            else -> {
                true
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
                val oldSelectedMonth = selectedMonth
                selectedMonth = item
                notifyItemChanged(oldSelectedMonth)
                notifyItemChanged(selectedMonth)
                onMonthSelectedListener?.invoke(selectedMonth)
            }
        }
    }

}