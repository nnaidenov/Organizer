package me.naidenov.organizer;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class NotesActivity extends Activity {
	private List<Note> myNotes = new ArrayList<Note>();

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_note_view);

		View empty = findViewById(R.id.empty_note);
		ListView list = (ListView) findViewById(R.id.listView_notes);
		list.setEmptyView(empty);

		GetNotes gs = new GetNotes();
		gs.execute("http://mobileorganizer.apphb.com/api/Notes/all/");
	}

	private class GetNotes extends AsyncTask<String, Void, String> {

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
							.getString("Id"));
					String title = jsonArray.getJSONObject(i)
							.getString("Title");

					Note cNote = new Note();
					cNote.setId(id);
					cNote.setTitle(title);

					myNotes.add(cNote);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Toast.makeText(NotesActivity.this, String.valueOf(myNotes.size()), Toast.LENGTH_LONG)
					.show();

			final ListView lv = (ListView) findViewById(R.id.listView_notes);
			NotesAdapter adapter = new NotesAdapter(NotesActivity.this,
					R.layout.day_list_row_stuffe, myNotes);
			lv.setAdapter(adapter);

			lv.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> arg0, View v,
						int position, long id) {
					Note st = myNotes.get(position);

					Intent intent = new Intent(NotesActivity.this,
							SingleNoteActivity.class);

					intent.putExtra("noteId", st.getId());
					startActivity(intent);
				}
			});
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progress = ProgressDialog.show(NotesActivity.this, "Loading...",
					"Please wait");
			progress.setCancelable(true);
			progress.show();
		}
	}
}
