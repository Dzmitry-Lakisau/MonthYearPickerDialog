package by.dzmitry_lakisau.month_year_picker_dialog.sample

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import by.dzmitry_lakisau.month_year_picker_dialog.MonthYearPickerDialog
import java.util.*

class MonthPickerDialogFragment : DialogFragment(), MonthYearPickerDialog.OnDateSetListener {

    private lateinit var initialDate: Date
    private lateinit var onDateSet: ((Date) -> Unit)

    private val calendar = Calendar.getInstance()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        calendar.time = initialDate

        return MonthYearPickerDialog.Builder(requireContext(), R.style.MonthPickerDialogStyle, this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH))
            .build()
    }

    override fun onDateSet(selectedMonth: Int, selectedYear: Int) {
        calendar.set(Calendar.YEAR, selectedYear)
        calendar.set(Calendar.MONTH, selectedMonth)

        onDateSet.invoke(calendar.time)
    }

    companion object {

        fun newInstance(initialDate: Date, callback: ((Date) -> Unit)): MonthPickerDialogFragment {
            return MonthPickerDialogFragment().apply {
                this.initialDate = initialDate
                onDateSet = callback
            }
        }

    }
}