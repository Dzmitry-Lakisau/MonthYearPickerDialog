package by.dzmitry_lakisau.month_year_picker_dialog

import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import java.util.*

internal class MonthsAdapter(private val monthTextColorStateList: ColorStateList, private val onMonthSelectedListener: ((Int) -> Unit)? = null) : RecyclerView.Adapter<MonthsAdapter.ViewHolder>() {

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
        this.maxMonth = maxMonth
    }

    fun setMinMonth(minMonth: Int) {
        this.minMonth = minMonth
    }

    fun setSelectedMonth(selectedMonth: Int) {
        this.selectedMonth = selectedMonth
    }

    fun setSelectedYear(year: Int) {
        selectedYear = year
        if (!isAnnualMode) {
            if (selectedYear == minYear) {
                if (selectedMonth < minMonth) {
                    selectedMonth = minMonth
                    onMonthSelectedListener?.invoke(selectedMonth)
                }
            } else if (selectedYear == maxYear) {
                if (selectedMonth > maxMonth) {
                    selectedMonth = maxMonth
                    onMonthSelectedListener?.invoke(selectedMonth)
                }
            }
            notifyItemRangeChanged(0, months.size)
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
            tvName.setTextColor(monthTextColorStateList)
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

    class SelectedItemDecoration(selectedMonthBackgroundColor: Int): RecyclerView.ItemDecoration() {

        private val selectedMonthBackgroundPaint: Paint = Paint().apply { color = selectedMonthBackgroundColor }

        override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
            parent.children.forEach { view ->
                if (view.isSelected) {
                    val radius = (view.height / 2).toFloat()
                    c.drawCircle(view.left + radius, view.top + radius, radius, selectedMonthBackgroundPaint)
                }
            }
        }
    }

}