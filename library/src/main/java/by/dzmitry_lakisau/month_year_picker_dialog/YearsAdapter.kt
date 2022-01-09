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

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

internal class YearsAdapter(private val onYearSelectedListener: ((Int) -> Unit)? = null) : RecyclerView.Adapter<YearsAdapter.ViewHolder>() {

    var maxYear = 0
    var minYear = 0
    var selectedYear = 0
        private set

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
        selectedYear = year
        notifyItemChanged(selectedYear)
    }

    private fun getYearForPosition(position: Int): Int {
        return minYear + position
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

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