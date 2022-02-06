package by.dzmitry_lakisau.month_year_picker_dialog.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import by.dzmitry_lakisau.month_year_picker_dialog.MonthYearPickerDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<MaterialButton>(R.id.btn_dialog).setOnClickListener {
            showDialog()
        }
        findViewById<MaterialButton>(R.id.btn_month_range).setOnClickListener {
            showMonthRangeDialog()
        }
        findViewById<MaterialButton>(R.id.btn_min_month_year).setOnClickListener {
            showMinMonthYear()
        }
        findViewById<MaterialButton>(R.id.btn_max_month_year).setOnClickListener {
            showMaxMonthYearDialog()
        }
        findViewById<MaterialButton>(R.id.btn_min_max_month_year).setOnClickListener {
            showMinMaxMonthYearDialog()
        }
        findViewById<MaterialButton>(R.id.btn_month_only).setOnClickListener {
            showMonthOnlyDialog()
        }
        findViewById<MaterialButton>(R.id.btn_year_only).setOnClickListener {
            showYearOnlyDialog()
        }
    }

    private fun showDialog() {
        val selectedDateString = findViewById<MaterialTextView>(R.id.tv_dialog_selected_date).text.toString()
        val selectedCalendar = if (selectedDateString.isEmpty()) {
            Calendar.getInstance().apply {
                set(Calendar.MONTH, Calendar.JULY)
            }
        } else {
            Calendar.getInstance().apply {
                time = SimpleDateFormat(DATE_PATTERN, Locale.getDefault()).parse(selectedDateString)!!
            }
        }

        val dialog = MonthYearPickerDialog.Builder(
            this,
            R.style.Style_MonthYearPickerDialog_Orange,
            { year, month ->
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                val dateString = SimpleDateFormat(DATE_PATTERN, Locale.getDefault()).format(calendar.time)
                findViewById<MaterialTextView>(R.id.tv_dialog_selected_date).text = dateString
            },
            selectedCalendar[Calendar.YEAR],
            selectedCalendar[Calendar.MONTH]
        )
            .setNegativeButton("Return")
            .setPositiveButton(R.string.set)
            .build()

        dialog.setTitle("Select month and year")

        MonthPickerDialogFragment.newInstance(dialog)
            .showNow(supportFragmentManager, MonthPickerDialogFragment::class.java.simpleName)
    }

    private fun showMonthRangeDialog() {
        val selectedDateString = findViewById<MaterialTextView>(R.id.tv_month_range_selected_date).text.toString()
        val selectedCalendar = if (selectedDateString.isEmpty()) {
            Calendar.getInstance().apply {
                set(Calendar.MONTH, Calendar.JULY)
            }
        } else {
            Calendar.getInstance().apply {
                time = SimpleDateFormat(DATE_PATTERN, Locale.getDefault()).parse(selectedDateString)!!
            }
        }

        val dialog = MonthYearPickerDialog.Builder(
            this,
            R.style.Style_MonthYearPickerDialog_Orange,
            { year, month ->
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                val dateString = SimpleDateFormat(DATE_PATTERN, Locale.getDefault()).format(calendar.time)
                findViewById<MaterialTextView>(R.id.tv_month_range_selected_date).text = dateString
            },
            selectedCalendar[Calendar.YEAR],
            selectedCalendar[Calendar.MONTH]
        )
            .setAnnualMode(true)
            .setMinMonth(Calendar.MAY)
            .setMaxMonth(Calendar.AUGUST)
            .setMinYear(2010)
            .setMaxYear(2022)
            .build()

        MonthPickerDialogFragment.newInstance(dialog)
            .showNow(supportFragmentManager, MonthPickerDialogFragment::class.java.simpleName)
    }

    private fun showMinMonthYear() {
        val selectedDateString = findViewById<MaterialTextView>(R.id.tv_min_month_year_selected_date).text.toString()
        val selectedCalendar = if (selectedDateString.isEmpty()) {
            Calendar.getInstance().apply {
                set(Calendar.YEAR, 2020)
                set(Calendar.MONTH, Calendar.JULY)
            }
        } else {
            Calendar.getInstance().apply {
                time = SimpleDateFormat(DATE_PATTERN, Locale.getDefault()).parse(selectedDateString)!!
            }
        }

        val dialog = MonthYearPickerDialog.Builder(
            this,
            R.style.Style_MonthYearPickerDialog_Orange,
            { year, month ->
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                val dateString = SimpleDateFormat(DATE_PATTERN, Locale.getDefault()).format(calendar.time)
                findViewById<MaterialTextView>(R.id.tv_min_month_year_selected_date).text = dateString
            },
            selectedCalendar[Calendar.YEAR],
            selectedCalendar[Calendar.MONTH]
        )
            .setMinMonth(Calendar.MAY)
            .setMinYear(2020)
            .build()

        MonthPickerDialogFragment.newInstance(dialog)
            .showNow(supportFragmentManager, MonthPickerDialogFragment::class.java.simpleName)
    }

    private fun showMaxMonthYearDialog() {
        val selectedDateString = findViewById<MaterialTextView>(R.id.tv_max_month_year_selected_date).text.toString()
        val selectedCalendar = if (selectedDateString.isEmpty()) {
            Calendar.getInstance().apply {
                set(Calendar.YEAR, 2022)
                set(Calendar.MONTH, Calendar.FEBRUARY)
            }
        } else {
            Calendar.getInstance().apply {
                time = SimpleDateFormat(DATE_PATTERN, Locale.getDefault()).parse(selectedDateString)!!
            }
        }

        val dialog = MonthYearPickerDialog.Builder(
            this,
            R.style.Style_MonthYearPickerDialog_Orange,
            { year, month ->
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                val dateString = SimpleDateFormat(DATE_PATTERN, Locale.getDefault()).format(calendar.time)
                findViewById<MaterialTextView>(R.id.tv_max_month_year_selected_date).text = dateString
            },
            selectedCalendar[Calendar.YEAR],
            selectedCalendar[Calendar.MONTH]
        )
            .setMaxMonth(Calendar.MARCH)
            .setMaxYear(2022)
            .build()

        MonthPickerDialogFragment.newInstance(dialog)
            .showNow(supportFragmentManager, MonthPickerDialogFragment::class.java.simpleName)
    }

    private fun showMinMaxMonthYearDialog() {
        val selectedDateString = findViewById<MaterialTextView>(R.id.tv_min_max_month_year_selected_date).text.toString()
        val selectedCalendar = if (selectedDateString.isEmpty()) {
            Calendar.getInstance().apply {
                set(Calendar.YEAR, 2022)
                set(Calendar.MONTH, Calendar.FEBRUARY)
            }
        } else {
            Calendar.getInstance().apply {
                time = SimpleDateFormat(DATE_PATTERN, Locale.getDefault()).parse(selectedDateString)!!
            }
        }

        val dialog = MonthYearPickerDialog.Builder(
            this,
            R.style.Style_MonthYearPickerDialog_Red,
            { year, month ->
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                val dateString = SimpleDateFormat(DATE_PATTERN, Locale.getDefault()).format(calendar.time)
                findViewById<MaterialTextView>(R.id.tv_min_max_month_year_selected_date).text = dateString
            },
            selectedCalendar[Calendar.YEAR],
            selectedCalendar[Calendar.MONTH]
        )
            .setMinMonth(Calendar.MAY)
            .setMinYear(2018)
            .setMaxMonth(Calendar.FEBRUARY)
            .setMaxYear(2022)
            .build()

        MonthPickerDialogFragment.newInstance(dialog)
            .showNow(supportFragmentManager, MonthPickerDialogFragment::class.java.simpleName)
    }

    private fun showMonthOnlyDialog() {
        val selectedDateString = findViewById<MaterialTextView>(R.id.tv_month_only_selected_date).text.toString()
        val selectedCalendar = if (selectedDateString.isEmpty()) {
            Calendar.getInstance().apply {
                set(Calendar.MONTH, Calendar.FEBRUARY)
            }
        } else {
            Calendar.getInstance().apply {
                time = SimpleDateFormat(MONTH_PATTERN, Locale.getDefault()).parse(selectedDateString)!!
            }
        }

        val dialog = MonthYearPickerDialog.Builder(
            this,
            R.style.Style_MonthYearPickerDialog_Red,
            { _, month ->
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.MONTH, month)
                val dateString = SimpleDateFormat(MONTH_PATTERN, Locale.getDefault()).format(calendar.time)
                findViewById<MaterialTextView>(R.id.tv_month_only_selected_date).text = dateString
            },
            selectedMonth = selectedCalendar[Calendar.MONTH]
        )
            .setMinMonth(Calendar.FEBRUARY)
            .setMaxMonth(Calendar.JULY)
            .setMonthFormat(MONTH_PATTERN)
            .setMode(MonthYearPickerDialog.Mode.MONTH_ONLY)
            .setOnMonthSelectedListener { month ->
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.MONTH, month)
                val dateString = SimpleDateFormat(MONTH_PATTERN, Locale.getDefault()).format(calendar.time)
                findViewById<MaterialTextView>(R.id.tv_month_only_selected_date).text = dateString
            }
            .build()

        MonthPickerDialogFragment.newInstance(dialog)
            .showNow(supportFragmentManager, MonthPickerDialogFragment::class.java.simpleName)
    }

    private fun showYearOnlyDialog() {
        val selectedDateString = findViewById<MaterialTextView>(R.id.tv_year_only_selected_date).text.toString()
        val selectedCalendar = if (selectedDateString.isEmpty()) {
            Calendar.getInstance()
        } else {
            Calendar.getInstance().apply {
                set(Calendar.YEAR, selectedDateString.toInt())
            }
        }

        val dialog = MonthYearPickerDialog.Builder(
            this,
            R.style.Style_MonthYearPickerDialog_Red,
            { year, _ ->
                findViewById<MaterialTextView>(R.id.tv_year_only_selected_date).text = year.toString()
            },
            selectedCalendar[Calendar.YEAR]
        )
            .setMinYear(2010)
            .setMode(MonthYearPickerDialog.Mode.YEAR_ONLY)
            .setOnYearSelectedListener() { year ->
                findViewById<MaterialTextView>(R.id.tv_year_only_selected_date).text = year.toString()
            }
            .build()

        MonthPickerDialogFragment.newInstance(dialog)
            .showNow(supportFragmentManager, MonthPickerDialogFragment::class.java.simpleName)
    }

    companion object {
        private const val DATE_PATTERN = "LLLL yyyy"
        private const val MONTH_PATTERN = "MMM"
    }

}