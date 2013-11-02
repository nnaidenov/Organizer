package me.naidenov.organizer;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class CreateTodoActivity extends FragmentActivity implements
		OnClickListener {
	private String createTodoModel;
	private ProgressDialog progress;

	private Button save_button;
	private Button back_button;

	private EditText title;
	private EditText description;
	private TextView date;
	private RadioGroup priority;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_todo);

		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		int width = displaymetrics.widthPixels;

		save_button = (Button) findViewById(R.id.button_create_todo_save);
		save_button.setOnClickListener(this);
		back_button = (Button) findViewById(R.id.button_create_todo_back);
		save_button.setWidth(width / 2);
		back_button.setWidth(width / 2);

		title = (EditText) findViewById(R.id.editText_todo_title);
		description = (EditText) findViewById(R.id.editText_todo_description);
		date = (TextView) findViewById(R.id.textView_todo_selected_date);
		priority = (RadioGroup) findViewById(R.id.radioGroup_todo_priority);
	}

	public void showDatePickerDialog(View v) {
		DialogFragment newFragment = new DataPickerFragment();
		newFragment.show(getSupportFragmentManager(), "datePicker");
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.loged_in, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.button_create_todo_save) {
			String str_title = title.getText().toString();
			String str_description = description.getText().toString();
			String str_date = date.getText().toString();

			int checkd_priority_id = priority.getCheckedRadioButtonId();
			RadioButton checked_priority = (RadioButton) findViewById(checkd_priority_id);
			String priority_value = (String) checked_priority.getText();

			JSONObject object = new JSONObject();
			try {
				object.put("title", str_title);
				object.put("description", str_description);
				object.put("date", str_date);
				object.put("priority", priority_value);

			} catch (Exception ex) {
			}

			createTodoModel = object.toString();
			CreateTodo creater = new CreateTodo();
			creater.execute(new String[] {
					"http://mobileorganizer.apphb.com/api/Todos/create",
					createTodoModel });

		}
	}

	private class CreateTodo extends AsyncTask<String, Void, Void> {
		
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
			Toast.makeText(CreateTodoActivity.this, "Created",
					Toast.LENGTH_LONG).show();
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progress = ProgressDialog.show(CreateTodoActivity.this, "Wait",
					"Please wait");
			progress.show();
		}

	}
}
