package me.naidenov.organizer;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.bool;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SingleNoteActivity extends Activity {

	private List<Bitmap> images = new ArrayList<Bitmap>();
	private ImageView imageView_one;
	private ImageView imageView_two;
	private TextView note_id;
	private TextView note_title;
	private TextView note_description;
	private boolean isFirst = true;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_single_note);

		imageView_one = (ImageView) findViewById(R.id.imageView_note_one);
		imageView_two = (ImageView) findViewById(R.id.imageView_note_tow);
		note_id = (TextView) findViewById(R.id.textView_id_note_view);
		note_title = (TextView) findViewById(R.id.textView_title_note_view);
		note_description = (TextView) findViewById(R.id.textView_description_note_view);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		int id = getIntent().getIntExtra("noteId", -1);

		GetNote getter = new GetNote();
		getter.execute("http://mobileorganizer.apphb.com/api/notes/getById/"
				+ id);
	}

	private class GetNote extends AsyncTask<String, Void, String> {

		private Dialog progress;
		private Note note = new Note();

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
				GetImage getter = new GetImage();

				JSONObject jsonObj = new JSONObject(result);
				List<String> paths = new ArrayList<String>();
				// int id = Integer.parseInt(jsonObj.getString("Id"));
				String title = jsonObj.getString("Title");
				String description = jsonObj.getString("Description");
				String imagesUrls = jsonObj.getString("ImagesUrls");

				JSONArray jsonArray = new JSONArray(imagesUrls);
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject explrObject = jsonArray.getJSONObject(i);
					paths.add(explrObject.getString("Path"));
				}

				getter.execute(new String[] { paths.get(0), paths.get(1) });
				// todo.setId(id);
				note.setTitle(title);
				note.setDescription(description);

				note_title.setText(note.getTitle());
				note_description.setText(note.getDescription());

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Toast.makeText(SingleNoteActivity.this, result, Toast.LENGTH_LONG)
					.show();
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progress = ProgressDialog.show(SingleNoteActivity.this,
					"Loading...", "Please wait");
			progress.setCancelable(true);
			progress.show();
		}
	}

	private class GetImage extends AsyncTask<String, Void, Void> {

		private Dialog progress;

		@Override
		protected Void doInBackground(String... params) {
			for (int i = 0; i < params.length; i++) {
				URL url = null;
				try {
					url = new URL(params[i]);
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				Bitmap bmp = null;
				try {
					bmp = BitmapFactory.decodeStream(url.openConnection()
							.getInputStream());
					images.add(bmp);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// UserDb userDb =
			// ((OrganizerApplication)getApplication()).getUserDb();
			//
			// userDb.addUser(email, authCode, sessionKey);

			if (isFirst) {
				imageView_one.setImageBitmap(images.get(0));
				isFirst = false;
			} else {
				imageView_two.setImageBitmap(images.get(1));
			}
		}
	}
}
