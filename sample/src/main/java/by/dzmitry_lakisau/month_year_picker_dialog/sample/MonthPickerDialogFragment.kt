package by.dzmitry_lakisau.month_year_picker_dialog.sample

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import by.dzmitry_lakisau.month_year_picker_dialog.MonthYearPickerDialog

class MonthPickerDialogFragment : DialogFragment() {

    private lateinit var dialog: MonthYearPickerDialog

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return dialog
    }

    companion object {

        fun newInstance(dialog: MonthYearPickerDialog): MonthPickerDialogFragment {
            return MonthPickerDialogFragment().apply {
                this.dialog = dialog
            }
        }

    }
}