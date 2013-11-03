package me.naidenov.organizer;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

public class DataPickerFragment extends DialogFragment implements
		DatePickerDialog.OnDateSetListener {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current date as the default date in the picker
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);

		// Create a new instance of DatePickerDialog and return it
		return new DatePickerDialog(getActivity(), this, year, month, day);
	}

	public void onDateSet(DatePicker view, int year, int month, int day) {

		if (getActivity().getClass().equals(CreateTodoActivity.class)) {

			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.MONTH, month);

			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM");
			simpleDateFormat.setCalendar(calendar);
			String monthName = simpleDateFormat.format(calendar.getTime());

			TextView selectedDate = (TextView) getActivity().findViewById(
					R.id.textView_todo_selected_date);
			selectedDate.setText(day + "-" + monthName + "-" + year);

		} else if (getActivity().getClass().equals(CreateEventActivity.class)) {

			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.MONTH, month);

			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM");
			simpleDateFormat.setCalendar(calendar);
			String monthName = simpleDateFormat.format(calendar.getTime());

			TextView selectedDate = (TextView) getActivity().findViewById(
					R.id.textView_event_start_time_value);
			selectedDate.setText(day + "-" + monthName + "-" + year);
		}
	}
}
