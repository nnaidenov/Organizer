package me.naidenov.organizer;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DayViewActivity extends Activity {

	private List<Stuffe> mySuffes = new ArrayList<Stuffe>();

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_day_view);
		
		View empty = findViewById(R.id.empty);
	    ListView list = (ListView) findViewById(R.id.asdf);
	    list.setEmptyView(empty);

		int[] date = getIntent().getIntArrayExtra("selectedDate");
		
		Calendar cal = Calendar.getInstance();
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int month = cal.get(Calendar.MONTH) + 1;
		int year = cal.get(Calendar.YEAR);
		
		if (date != null) {
			day = date[1];
			month = date[0] + 1;
			year = date[2];
		}

		GetStuffes gs = new GetStuffes();
		gs.execute("http://mobileorganizer.apphb.com/api/Stuffes/byDate/" + day
				+ "/" + month + "/" + year + "");
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
			try {

				JSONArray jsonArray = new JSONArray(result);
				for (int i = 0; i < jsonArray.length(); i++) {
					int id = Integer.parseInt(jsonArray.getJSONObject(i)
							.getString("id"));
					String title = jsonArray.getJSONObject(i)
							.getString("title");
					String type = jsonArray.getJSONObject(i).getString("type");

					Stuffe cStuffe = new Stuffe();
					cStuffe.setId(id);
					cStuffe.setTitle(title);
					cStuffe.setType(type);

					mySuffes.add(cStuffe);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Toast.makeText(DayViewActivity.this, result, Toast.LENGTH_LONG)
					.show();

			final ListView lv = (ListView) findViewById(R.id.asdf);
			StuffesAdapter adapter = new StuffesAdapter(DayViewActivity.this,
					R.layout.day_list_row_stuffe, mySuffes);
			lv.setAdapter(adapter);

			lv.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> arg0, View v,
						int position, long id) {
					Stuffe st = mySuffes.get(position);

					if (st.getType().equals("todo")) {
						Intent intent = new Intent(DayViewActivity.this,
								SingleTodoActivity.class);

						intent.putExtra("todoId", st.getId());
						startActivity(intent);
					} else if (st.getType().equals("event")) {
						Intent intent = new Intent(DayViewActivity.this,
								SingleEventActivity.class);

						intent.putExtra("eventId", st.getId());
						startActivity(intent);
					}

				}
			});
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progress = ProgressDialog.show(DayViewActivity.this, "Loading...",
					"Please wait");
			progress.setCancelable(true);
			progress.show();
		}
	}
}
