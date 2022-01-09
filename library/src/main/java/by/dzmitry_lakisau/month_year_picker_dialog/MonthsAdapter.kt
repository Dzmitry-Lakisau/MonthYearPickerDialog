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

import android.graphics.Canvas
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import java.text.SimpleDateFormat
import java.util.*

internal class MonthsAdapter(private val onMonthSelectedListener: ((Int) -> Unit)? = null) : RecyclerView.Adapter<MonthsAdapter.ViewHolder>() {

    var isAnnualMode = false

    var minMonth = Calendar.JANUARY
    var selectedMonth = Calendar.JANUARY
    var maxMonth = Calendar.DECEMBER

    var minYear = 0
    private var selectedYear = 0
    var maxYear = 0

    lateinit var monthFormat: SimpleDateFormat

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_month, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBindViewHolder(months[position])
    }

    override fun getItemCount(): Int {
        return months.size
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

        fun onBindViewHolder(item: Int) {
            tvName.text = getMonthName(item, monthFormat)

            itemView.isEnabled = isInRange(item)
            tvName.isEnabled = itemView.isEnabled

            itemView.isSelected = item == selectedMonth

            itemView.setOnClickListener {
                selectedMonth = item
                notifyDataSetChanged()
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