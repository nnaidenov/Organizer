package me.naidenov.organizer;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class HomeActivity extends Activity implements OnClickListener {

	private ProgressDialog progress;
	private Button createTodo; 
	private Button createEvent; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		createTodo = (Button) findViewById(R.id.button_create_todo_view);
		createTodo.setOnClickListener(this);
		
		createEvent = (Button) findViewById(R.id.button_create_event_view);
		createEvent.setOnClickListener(this);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.loged_in, menu);
		return true;
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.button_create_todo_view) {
			Intent intent = new Intent(HomeActivity.this, CreateTodoActivity.class);
			startActivity(intent);
		} else if (view.getId() == R.id.button_create_event_view) {
			Intent intent = new Intent(HomeActivity.this, CreateEventActivity.class);
			startActivity(intent);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_logout) {

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

			Toast.makeText(this, total.toString(), Toast.LENGTH_LONG).show();
			LogoutUser logouter = new LogoutUser();
			logouter.execute(new String[] {"http://mobileorganizer.apphb.com/api/users/logout", total.toString()});
		}

		return true;
	}

	private class LogoutUser extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... params) {

			try {
				URL url = new URL(params[0]);
				HttpURLConnection httpCon = (HttpURLConnection) url
						.openConnection();
				httpCon.setRequestMethod("PUT");
				httpCon.setRequestProperty("X-sessionKey",
						params[1]);
				httpCon.connect();
				int responseCode = httpCon.getResponseCode();
	
				Log.d("My", String.valueOf(responseCode));
				
				OutputStream os = openFileOutput("sessionKey", Context.MODE_PRIVATE);
				os.write(new String().getBytes());
				os.close();
				
				Intent intent = new Intent(HomeActivity.this, MainActivity.class);
				startActivity(intent);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
//			UserDb userDb = ((OrganizerApplication)getApplication()).getUserDb();
//			
//			userDb.addUser(email, authCode, sessionKey);
			progress.dismiss();
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progress = ProgressDialog.show(HomeActivity.this, "Wait", "Please wait");
			progress.show();
		}	
		
	}
}
