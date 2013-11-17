package me.naidenov.organizer;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.TooManyListenersException;

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
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ToggleButton;

public class SingleTodoActivity extends Activity implements OnClickListener {

	private TextView tewxtView_id;
	private TextView tewxtView_title;
	private TextView tewxtView_description;
	private ToggleButton toggle_isDone;

	private Todo todo = new Todo();

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_single_todo);

		tewxtView_id = (TextView) findViewById(R.id.textView_todo_id);
		tewxtView_title = (TextView) findViewById(R.id.textView_todo_title);
		tewxtView_description = (TextView) findViewById(R.id.textView_todo_description);
		toggle_isDone = (ToggleButton) findViewById(R.id.toggleButton_todo_is_done);
		toggle_isDone.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		int id = getIntent().getIntExtra("todoId", -1);

		GetTodo getter = new GetTodo();
		getter.execute("http://mobileorganizer.apphb.com/api/todos/getById/"
				+ id);
	}

	public void deteleTodo(View view) {
		DeleteTodo deleter = new DeleteTodo();
		deleter.execute("http://mobileorganizer.apphb.com/api/todos/delete/"
				+ tewxtView_id.getText());
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.toggleButton_todo_is_done) {
			UpdateTodo updater = new UpdateTodo();
			updater.execute("http://mobileorganizer.apphb.com/api/todos/update/"
					+ todo.getId());
		}
	}

	private class GetTodo extends AsyncTask<String, Void, String> {

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
				JSONObject jsonObj = new JSONObject(result);
				int id = Integer.parseInt(jsonObj.getString("Id"));
				String title = jsonObj.getString("Title");
				String description = jsonObj.getString("Description");
				Boolean isDone = jsonObj.getBoolean("IsDone");

				todo.setId(id);
				todo.setTitle(title);
				todo.setDescription(description);
				todo.setIsDone(isDone);

				tewxtView_id.setText(String.valueOf(todo.getId()));
				tewxtView_title.setText(todo.getTitle());
				tewxtView_description.setText(todo.getDescription());
				toggle_isDone.setChecked(todo.getIsDone());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Toast.makeText(SingleTodoActivity.this, result, Toast.LENGTH_LONG)
					.show();
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progress = ProgressDialog.show(SingleTodoActivity.this,
					"Loading...", "Please wait");
			progress.setCancelable(true);
			progress.show();
		}
	}

	private class UpdateTodo extends AsyncTask<String, Void, Void> {

		private Dialog progress;

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
			Toast.makeText(SingleTodoActivity.this, "Updated",
					Toast.LENGTH_LONG).show();

			todo.setIsDone(true);
			toggle_isDone.setChecked(todo.getIsDone());
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progress = ProgressDialog.show(SingleTodoActivity.this,
					"Updating...", "Please wait");
			progress.show();
		}
	}

	private class DeleteTodo extends AsyncTask<String, Void, String> {

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

			if (result.equals("200")) {

			}

			Toast.makeText(SingleTodoActivity.this, "Deleted",
					Toast.LENGTH_LONG).show();
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progress = ProgressDialog.show(SingleTodoActivity.this,
					"Loading...", "Please wait");
			progress.setCancelable(true);
			progress.show();
		}
	}
}