package by.dzmitry_lakisau.month_year_picker_dialog.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textview.MaterialTextView
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var tvSelectedDate: MaterialTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvSelectedDate = findViewById(R.id.tv_selected_date)
    }

    override fun onStart() {
        super.onStart()

        val selectedDateString = tvSelectedDate.text.toString()
        val date = if (selectedDateString.isEmpty()) {
            Calendar.getInstance().time
        } else {
            SimpleDateFormat("LLLL yyyy", Locale.getDefault()).parse(selectedDateString)!!
        }

        MonthPickerDialogFragment
            .newInstance(date, ::onDateSet)
            .showNow(supportFragmentManager, MonthPickerDialogFragment::class.java.simpleName)
    }

    private fun onDateSet(date: Date) {
        val dateString = SimpleDateFormat("LLLL yyyy", Locale.getDefault()).format(date)
        tvSelectedDate.text = dateString
    }

}