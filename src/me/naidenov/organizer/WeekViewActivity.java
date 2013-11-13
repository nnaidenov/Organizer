package me.naidenov.organizer;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Point;
import android.inputmethodservice.Keyboard.Row;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class WeekViewActivity extends Activity {

	private TextView textView_firstDay;
	private TextView textView_secondDay;
	private TextView textView_thirdDay;
	private TextView textView_fourthDay;
	private TextView textView_fifthDay;
	private TextView textView_sixthhDay;
	private TextView textView_seventhDay;

	private Intent intentFirstDay;
	private Intent intentSecondDay;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_week);

		textView_firstDay = (TextView) findViewById(R.id.textView_firstDay);
		textView_secondDay = (TextView) findViewById(R.id.textView_secondDay);
		textView_thirdDay = (TextView) findViewById(R.id.textView_thirdDay);
		textView_fourthDay = (TextView) findViewById(R.id.textView_fourthDay);
		textView_fifthDay = (TextView) findViewById(R.id.textView_fifthDay);
		textView_sixthhDay = (TextView) findViewById(R.id.textView_sixthhDay);
		textView_seventhDay = (TextView) findViewById(R.id.textView_seventhDay);
		
		intentFirstDay = new Intent(WeekViewActivity.this,
				DayViewActivity.class);
		intentSecondDay = new Intent(WeekViewActivity.this,
				DayViewActivity.class);
		
		LinearLayout ty = (LinearLayout) findViewById(R.id.ponedelnik);
		ty.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(WeekViewActivity.this, "Понеделник",
						Toast.LENGTH_LONG).show();
			
				startActivity(intentFirstDay);
			}
		});
		
		LinearLayout ty2 = (LinearLayout) findViewById(R.id.vtornik);
		ty2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(WeekViewActivity.this, "Вторник",
						Toast.LENGTH_LONG).show();
			
				startActivity(intentSecondDay);
			}
		});

		GetStuffes gs = new GetStuffes();
		gs.execute("http://mobileorganizer.apphb.com/api/Stuffes/byweek");
	}

	private class GetStuffes extends AsyncTask<String, Void, String> {

		private Dialog progress;
		private List<Stuffe> mySuffes = new ArrayList<Stuffe>();

		@Override
		protected String doInBackground(String... params) {
			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet(params[0]);
			StringBuilder sb = new StringBuilder();
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

				get.setHeader("X-sessionKey", total.toString());
				HttpResponse dsd = client.execute(get);
				InputStream is = dsd.getEntity().getContent();
				InputStreamReader isr = new InputStreamReader(is);

				BufferedReader br = new BufferedReader(isr);

				String chunk = null;
				while ((chunk = br.readLine()) != null) {
					sb.append(chunk);
				}
			} catch (Exception e) {

			}

			return sb.toString();
		}

		@Override
		protected void onPostExecute(String result) {
			// UserDb userDb =
			// ((OrganizerApplication)getApplication()).getUserDb();
			//
			// userDb.addUser(email, authCode, sessionKey);
			progress.dismiss();
			try {

				JSONArray jsonArray = new JSONArray(result);
				for (int i = 0; i < jsonArray.length(); i++) {
					int id = Integer.parseInt(jsonArray.getJSONObject(i)
							.getString("id"));
					String title = jsonArray.getJSONObject(i)
							.getString("title");
					String type = jsonArray.getJSONObject(i).getString("type");

					String startDateString = jsonArray.getJSONObject(i)
							.getString("date");

					DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					Date startDate = df.parse(startDateString);

					Stuffe cStuffe = new Stuffe();
					cStuffe.setId(id);
					cStuffe.setTitle(title);
					cStuffe.setType(type);
					cStuffe.setDate(startDate);

					mySuffes.add(cStuffe);

					Calendar c = Calendar.getInstance();
					c.setTime(startDate);
					int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
					int[] selectedDate = new int[] {
							startDate.getMonth(), startDate.getDate(), startDate.getYear()+1900};
					
					if (dayOfWeek == 1) {
						String old = textView_firstDay.getText().toString();
						String newT = old + "\n" + title;
						textView_firstDay.setText(newT);
						intentFirstDay.putExtra("selectedDate", selectedDate);
					} else if (dayOfWeek == 2) {
						String old = textView_secondDay.getText().toString();
						String newT = old + "\n" + title;
						textView_secondDay.setText(newT);
						
						intentSecondDay.putExtra("selectedDate", selectedDate);
					} else if (dayOfWeek == 3) {
						String old = textView_thirdDay.getText().toString();
						String newT = old + "\n" + title;
						textView_thirdDay.setText(newT);
					} else if (dayOfWeek == 4) {
						String old = textView_fourthDay.getText().toString();
						String newT = old + "\n" + title;
						textView_fourthDay.setText(newT);
					} else if (dayOfWeek == 5) {
						String old = textView_fifthDay.getText().toString();
						String newT = old + "\n" + title;
						textView_fifthDay.setText(newT);
					} else if (dayOfWeek == 6) {
						String old = textView_sixthhDay.getText().toString();
						String newT = old + "\n" + title;
						textView_sixthhDay.setText(newT);
					} else if (dayOfWeek == 7) {
						String old = textView_seventhDay.getText().toString();
						String newT = old + "\n" + title;
						textView_seventhDay.setText(newT);
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (java.text.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progress = ProgressDialog.show(WeekViewActivity.this, "Loading...",
					"Please wait");
			progress.setCancelable(true);
			progress.show();
		}
	}
}
