package by.dzmitry_lakisau.month_year_picker_dialog

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.withStyledAttributes
import androidx.recyclerview.widget.RecyclerView

internal class YearsAdapter(private val onYearSelectedListener: ((Int) -> Unit)? = null) : RecyclerView.Adapter<YearsAdapter.ViewHolder>() {

    var maxYear = 0
    var minYear = 0
    private var selectedYear = 0

    lateinit var colorStateList: ColorStateList

    constructor(context: Context, attributeSet: AttributeSet, onYearSelectedListener: ((Int) -> Unit)?) : this(onYearSelectedListener) {
        context.withStyledAttributes(attributeSet, R.styleable.MonthPickerView) {
            val yearColor = context.getColorFromAttribute(this, R.styleable.MonthPickerView_yearColor, android.R.attr.textColorPrimary)
            val yearSelectedColor = context.getColorFromAttribute(this, R.styleable.MonthPickerView_yearSelectedColor, android.R.attr.colorAccent)
            val states = arrayOf(intArrayOf(-android.R.attr.state_selected), intArrayOf(android.R.attr.state_selected))
            val colors = intArrayOf(yearColor, yearSelectedColor)
            colorStateList = ColorStateList(states, colors)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_year, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBindViewHolder(getYearForPosition(position))
    }

    override fun getItemCount(): Int {
        return maxYear - minYear + 1
    }

    fun getPositionForYear(year: Int): Int {
        return year - minYear
    }

    fun setSelectedYear(year: Int) {
        if (year in minYear..maxYear) {
            selectedYear = year
            notifyItemChanged(selectedYear)
        } else {
            throw IllegalArgumentException("Selected year is not in range")
        }
    }

    private fun getYearForPosition(position: Int): Int {
        return minYear + position
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            (itemView as TextView).setTextColor(colorStateList)
        }

        fun onBindViewHolder(item: Int) {
            (itemView as TextView).apply {
                isSelected = selectedYear == item
                text = item.toString()
                textSize = if (selectedYear == item) {
                    25f
                } else {
                    20f
                }
            }
            itemView.setOnClickListener {
                if (selectedYear != item) {
                    selectedYear = item
                    notifyDataSetChanged()
                    onYearSelectedListener?.invoke(item)
                }
            }
        }
    }
}