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
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class MonthViewActivity extends ListActivity {

	private List<Stuffe> mySuffes = new ArrayList<Stuffe>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_month);

		GetStuffes gs = new GetStuffes();
		gs.execute("http://mobileorganizer.apphb.com/api/Stuffes/bymonth");
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		for (int j = 0; j < mySuffes.size(); j++) {
			Stuffe selectedStuff = mySuffes.get(j);
			if (selectedStuff.getListPossiton() == position) {
				if (selectedStuff.getType().equals("todo")) {
					Intent intent = new Intent(MonthViewActivity.this,
							SingleTodoActivity.class);

					intent.putExtra("todoId", selectedStuff.getId());
					startActivity(intent);
				} else if (selectedStuff.getType().equals("event")) {
					Intent intent = new Intent(MonthViewActivity.this,
							SingleEventActivity.class);

					intent.putExtra("eventId", selectedStuff.getId());
					startActivity(intent);
				}
				
				break;
			}
		}
	}

	private class GetStuffes extends AsyncTask<String, Void, String> {

		private Dialog progress;

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
			Toast.makeText(MonthViewActivity.this, result, Toast.LENGTH_LONG)
					.show();
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
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (java.text.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Calendar cal = Calendar.getInstance();
			Calendar mycal = new GregorianCalendar(cal.YEAR, cal.MONTH + 1, 1);

			// Get the number of days in that month
			int daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH); // 28
			List<Item> items = new ArrayList<Item>();
			int possitionCounter = 0;
			for (int i = 1; i <= daysInMonth; i++) {
				items.add(new Header(null, String.valueOf(i)));

				for (int j = 0; j < mySuffes.size(); j++) {
					Stuffe selectedStuff = mySuffes.get(j);
					if (selectedStuff.getDate().getDate() == i) {
						items.add(new ListItem(null, selectedStuff.getTitle(),
								selectedStuff.getType()));
						possitionCounter++;
						selectedStuff.setListPossiton(possitionCounter);
					}
				}
				
				possitionCounter++;
			}

			TwoTextArrayAdapter adapter = new TwoTextArrayAdapter(
					MonthViewActivity.this, items);
			setListAdapter(adapter);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progress = ProgressDialog.show(MonthViewActivity.this,
					"Loading...", "Please wait");
			progress.setCancelable(true);
			progress.show();
		}
	}
}