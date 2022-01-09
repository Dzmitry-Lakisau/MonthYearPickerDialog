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

import java.text.SimpleDateFormat
import java.util.*

internal fun getMonthName(month: Int, monthFormat: SimpleDateFormat): String {
    val calendar = Calendar.getInstance()
    calendar[Calendar.DAY_OF_MONTH] = 1
    calendar[Calendar.MONTH] = month
    return monthFormat.format(calendar.time).replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}