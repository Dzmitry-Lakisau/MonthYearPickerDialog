package by.dzmitry_lakisau.month_year_picker_dialog

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

internal class YearsAdapter(private val yearTextColorStateList: ColorStateList, private val onYearSelectedListener: ((Int) -> Unit)? = null) : RecyclerView.Adapter<YearsAdapter.ViewHolder>() {

    var maxYear = 0
    var minYear = 0
    private var selectedYear = 0

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
            (itemView as TextView).setTextColor(yearTextColorStateList)
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