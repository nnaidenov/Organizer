package me.naidenov.organizer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;

public class MonthViewActivity extends ListActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_month);

		Calendar mycal = new GregorianCalendar(1999, Calendar.FEBRUARY, 1);

		// Get the number of days in that month
		int daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH); // 28
		List<Item> items = new ArrayList<Item>();
		for (int i = 1; i <= daysInMonth; i++) {
			items.add(new Header(null, String.valueOf(i)));
		}
		
		
		
		

		TwoTextArrayAdapter adapter = new TwoTextArrayAdapter(this, items);
		setListAdapter(adapter);
	}
}