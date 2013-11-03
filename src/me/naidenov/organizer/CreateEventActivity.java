package me.naidenov.organizer;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

public class CreateEventActivity extends FragmentActivity implements
		OnClickListener {
	private String createEventModel;
	private ProgressDialog progress;

	private EditText title;
	private EditText description;
	private ToggleButton whole_day;
	private TextView text_view_start_time;
	private TextView text_view_start_time_value;
	private Button start_time;
	private TextView text_view_end_time;
	private TextView text_view_end_time_value;
	private Button end_time;
	private Spinner reminder;
	private Button create;
	private Button cancel;

	private Button setTime;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_event);

		title = (EditText) findViewById(R.id.editText_event_title);
		description = (EditText) findViewById(R.id.editText_event_description);

		whole_day = (ToggleButton) findViewById(R.id.toggleButton_event_whole_day);
		whole_day.setOnClickListener(this);

		text_view_start_time = (TextView) findViewById(R.id.textView_event_start_time);
		text_view_start_time_value = (TextView) findViewById(R.id.textView_event_start_time_value);
		start_time = (Button) findViewById(R.id.button_event_start_time);
		start_time.setOnClickListener(this);

		text_view_end_time = (TextView) findViewById(R.id.textView_event_end_time);
		text_view_end_time_value = (TextView) findViewById(R.id.textView_event_end_time_value);
		end_time = (Button) findViewById(R.id.button_event_end_time);
		end_time.setOnClickListener(this);

		reminder = (Spinner) findViewById(R.id.spinner_event_reminder);
		create = (Button) findViewById(R.id.button_create_event_save);
		create.setOnClickListener(this);
		
		cancel = (Button) findViewById(R.id.button_create_back);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.loged_in, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.button_create_event_save) {
			String str_title = title.getText().toString();
			String str_description = description.getText().toString();
			String str_start_date = text_view_start_time_value.getText().toString();
			String str_end_date = text_view_end_time_value.getText().toString();

			JSONObject object = new JSONObject();
			try {
				object.put("StartDate", str_start_date);
				object.put("EndDate", str_end_date);
				object.put("Description", str_description);
				object.put("Title", str_title);
				object.put("Reminder", str_end_date);
			} catch (Exception ex) {
			}

			createEventModel = object.toString();
			CreateEvent creater = new CreateEvent();
			creater.execute(new String[] {
					"http://mobileorganizer.apphb.com/api/events/create",
					createEventModel });
		} else if (v.getId() == R.id.toggleButton_event_whole_day) {
			if (whole_day.isChecked()) {
				text_view_start_time.setVisibility(View.GONE);
				text_view_end_time.setVisibility(View.GONE);
				start_time.setVisibility(View.GONE);
				end_time.setVisibility(View.GONE);
			} else {
				text_view_start_time.setVisibility(View.VISIBLE);
				text_view_end_time.setVisibility(View.VISIBLE);
				start_time.setVisibility(View.VISIBLE);
				end_time.setVisibility(View.VISIBLE);
			}
		} else if (v.getId() == R.id.button_event_start_time) {

			final Dialog dialog = new Dialog(this);
			dialog.setContentView(R.layout.sdfsa);
			dialog.setTitle("Save New Number");
			dialog.setCancelable(true);
			dialog.show();

			Button saveButton = (Button) dialog
					.findViewById(R.id.button_set_date);
			saveButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String hours = ((TimePicker) dialog
							.findViewById(R.id.timePicker1)).getCurrentHour()
							.toString();
					String minutes = ((TimePicker) dialog
							.findViewById(R.id.timePicker1)).getCurrentMinute()
							.toString();

					int day = ((DatePicker) dialog
							.findViewById(R.id.datePicker1)).getDayOfMonth();
					int month = ((DatePicker) dialog
							.findViewById(R.id.datePicker1)).getMonth();
					int year = ((DatePicker) dialog
							.findViewById(R.id.datePicker1)).getYear();

					Calendar calendar = Calendar.getInstance();
					calendar.set(Calendar.MONTH, month);
					calendar.set(Calendar.DAY_OF_MONTH, day);
					calendar.set(Calendar.YEAR, year);

					SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
							"MMM");
					simpleDateFormat.setCalendar(calendar);
					String monthName = simpleDateFormat.format(calendar
							.getTime());

					TextView selectedDate = (TextView) findViewById(R.id.textView_event_start_time_value);
					selectedDate.setText(day + "-" + monthName + "-" + year
							+ " " + hours + ":" + minutes);

					dialog.dismiss();
				}
			});
		} else if (v.getId() == R.id.button_event_end_time) {

			final Dialog dialog = new Dialog(this);
			dialog.setContentView(R.layout.sdfsa);
			dialog.setTitle("Save New Number");
			dialog.setCancelable(true);
			dialog.show();

			Button saveButton = (Button) dialog
					.findViewById(R.id.button_set_date);
			saveButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String hours = ((TimePicker) dialog
							.findViewById(R.id.timePicker1)).getCurrentHour()
							.toString();
					String minutes = ((TimePicker) dialog
							.findViewById(R.id.timePicker1)).getCurrentMinute()
							.toString();

					int day = ((DatePicker) dialog
							.findViewById(R.id.datePicker1)).getDayOfMonth();
					int month = ((DatePicker) dialog
							.findViewById(R.id.datePicker1)).getMonth();
					int year = ((DatePicker) dialog
							.findViewById(R.id.datePicker1)).getYear();

					Calendar calendar = Calendar.getInstance();
					calendar.set(Calendar.MONTH, month);

					SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
							"MMM");
					simpleDateFormat.setCalendar(calendar);
					String monthName = simpleDateFormat.format(calendar
							.getTime());

					TextView selectedDate = (TextView) findViewById(R.id.textView_event_end_time_value);
					selectedDate.setText(day + "-" + monthName + "-" + year
							+ " " + hours + ":" + minutes);
					dialog.dismiss();
				}
			});
		}
	}
	
	private class CreateEvent extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... params) {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(params[0]);

			try {
				FileInputStream os = null;
				try {
					os = openFileInput("sessionKey");
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				BufferedReader r = new BufferedReader(new InputStreamReader(os));
				StringBuilder total = new StringBuilder();
				String line;
				try {
					while ((line = r.readLine()) != null) {
						total.append(line);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				post.setEntity(new StringEntity(params[1], "UTF8"));
				post.setHeader("Content-type", "application/json");
				post.setHeader("X-sessionKey", total.toString());
				client.execute(post);
			} catch (Exception e) {

			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// UserDb userDb =
			// ((OrganizerApplication)getApplication()).getUserDb();
			//
			// userDb.addUser(email, authCode, sessionKey);
			progress.dismiss();
			Toast.makeText(CreateEventActivity.this, "Created",
					Toast.LENGTH_LONG).show();
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progress = ProgressDialog.show(CreateEventActivity.this, "Wait",
					"Please wait");
			progress.show();
		}
	}
}
