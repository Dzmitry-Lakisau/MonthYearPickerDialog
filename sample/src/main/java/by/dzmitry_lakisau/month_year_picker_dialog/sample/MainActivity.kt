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
            R.style.MonthPickerDialogStyle,
            object : MonthYearPickerDialog.OnDateSetListener {
                override fun onDateSet(year: Int, month: Int) {
                    val calendar = Calendar.getInstance()
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, month)
                    val dateString = SimpleDateFormat(DATE_PATTERN, Locale.getDefault()).format(calendar.time)
                    findViewById<MaterialTextView>(R.id.tv_month_range_selected_date).text = dateString
                }
            },
            selectedCalendar[Calendar.YEAR],
            selectedCalendar[Calendar.MONTH]
        )
            .setAnnualMode(true)
            .setMinMonth(Calendar.MAY)
            .setMaxMonth(Calendar.AUGUST)
            .setMinYear(2010)
            .setMaxYear(2021)
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
            R.style.MonthPickerDialogStyle,
            object : MonthYearPickerDialog.OnDateSetListener {
                override fun onDateSet(year: Int, month: Int) {
                    val calendar = Calendar.getInstance()
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, month)
                    val dateString = SimpleDateFormat(DATE_PATTERN, Locale.getDefault()).format(calendar.time)
                    findViewById<MaterialTextView>(R.id.tv_min_month_year_selected_date).text = dateString
                }
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
                set(Calendar.YEAR, 2021)
                set(Calendar.MONTH, Calendar.JULY)
            }
        } else {
            Calendar.getInstance().apply {
                time = SimpleDateFormat(DATE_PATTERN, Locale.getDefault()).parse(selectedDateString)!!
            }
        }

        val dialog = MonthYearPickerDialog.Builder(
            this,
            R.style.MonthPickerDialogStyle,
            object : MonthYearPickerDialog.OnDateSetListener {
                override fun onDateSet(year: Int, month: Int) {
                    val calendar = Calendar.getInstance()
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, month)
                    val dateString = SimpleDateFormat(DATE_PATTERN, Locale.getDefault()).format(calendar.time)
                    findViewById<MaterialTextView>(R.id.tv_max_month_year_selected_date).text = dateString
                }
            },
            selectedCalendar[Calendar.YEAR],
            selectedCalendar[Calendar.MONTH]
        )
            .setMaxMonth(Calendar.AUGUST)
            .setMaxYear(2021)
            .build()

        MonthPickerDialogFragment.newInstance(dialog)
            .showNow(supportFragmentManager, MonthPickerDialogFragment::class.java.simpleName)
    }

    private fun showMinMaxMonthYearDialog() {
        val selectedDateString = findViewById<MaterialTextView>(R.id.tv_min_max_month_year_selected_date).text.toString()
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
            R.style.MonthPickerDialogStyle,
            object : MonthYearPickerDialog.OnDateSetListener {
                override fun onDateSet(year: Int, month: Int) {
                    val calendar = Calendar.getInstance()
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, month)
                    val dateString = SimpleDateFormat(DATE_PATTERN, Locale.getDefault()).format(calendar.time)
                    findViewById<MaterialTextView>(R.id.tv_min_max_month_year_selected_date).text = dateString
                }
            },
            selectedCalendar[Calendar.YEAR],
            selectedCalendar[Calendar.MONTH]
        )
            .setMinMonth(Calendar.MAY)
            .setMinYear(2018)
            .setMaxMonth(Calendar.FEBRUARY)
            .setMaxYear(2021)
            .build()

        MonthPickerDialogFragment.newInstance(dialog)
            .showNow(supportFragmentManager, MonthPickerDialogFragment::class.java.simpleName)
    }

    companion object {
        private const val DATE_PATTERN = "LLLL yyyy"
    }

}