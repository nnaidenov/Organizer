package me.naidenov.organizer;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class SingleEventActivity extends Activity {
	private TextView id;
	private TextView tewxtView_title;
	private TextView tewxtView_description;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_single_event);

		id = (TextView) findViewById(R.id.textView_event_id);
		tewxtView_title = (TextView) findViewById(R.id.textView_event_title);
		tewxtView_description = (TextView) findViewById(R.id.textView_event_description);
	}

	public void deleteEvent(View view) {
		DeleteEvent deleter = new DeleteEvent();
		deleter.execute("http://mobileorganizer.apphb.com/api/events/delete/" + id.getText());
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		int idFromIntent = getIntent().getIntExtra("eventId", -1);
		id.setText(String.valueOf(idFromIntent));
		
		GetEvent getter = new GetEvent();
		getter.execute("http://mobileorganizer.apphb.com/api/events/getById/" + idFromIntent);
	}
	
	private class GetEvent extends AsyncTask<String, Void, String> {

		private Dialog progress;
		private Event event = new Event();

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
				JSONObject jsonObj = new JSONObject(result);
				int id = Integer.parseInt(jsonObj.getString("Id"));
				String title = jsonObj.getString("Title");
				String description = jsonObj.getString("Description");

				event.setId(id);
				event.setTitle(title);
				event.setDescription(description);
				
				tewxtView_title.setText(event.getTitle());
				tewxtView_description.setText(event.getDescription());		
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Toast.makeText(SingleEventActivity.this, result, Toast.LENGTH_LONG)
					.show();
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progress = ProgressDialog.show(SingleEventActivity.this,
					"Loading...", "Please wait");
			progress.setCancelable(true);
			progress.show();
		}
	}
	
	private class DeleteEvent extends AsyncTask<String, Void, String> {

		private Dialog progress;

		@Override
		protected String doInBackground(String... params) {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(params[0]);
			HttpResponse dsd = null;
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

				post.setHeader("X-sessionKey", total.toString());
				dsd = client.execute(post);
			} catch (Exception e) {

			}
			
			int code = dsd.getStatusLine().getStatusCode();
			
			return String.valueOf(code);
		}

		@Override
		protected void onPostExecute(String result) {
			// UserDb userDb =
			// ((OrganizerApplication)getApplication()).getUserDb();
			//
			// userDb.addUser(email, authCode, sessionKey);
			progress.dismiss();
			
			if(result.equals("200")) {
				
			}

			Toast.makeText(SingleEventActivity.this, "Deleted", Toast.LENGTH_LONG)
			.show();
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progress = ProgressDialog.show(SingleEventActivity.this,
					"Loading...", "Please wait");
			progress.setCancelable(true);
			progress.show();
		}
	}
}