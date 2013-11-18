package me.naidenov.organizer;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cloudinary.Cloudinary;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class CreateNoteActivity extends Activity implements OnClickListener {
	private static final int CAMERA_CODE = 0;
	private static final int VIDEO_CODE = 2;
	private static final int LOAD_IMAGES_CODE = 1;

	private byte[] photo;
	private byte[] image;
	private List<String> resourseUrls = new ArrayList<String>();

	private EditText editorText_title;
	private EditText editorText_description;

	private Button camera_button;
	private Button photo_button;
	private ImageView image_imageView;
	private ImageView photoView;
	private String mCapturedImageURI;

	private Button save_button;
	private Button button_note_back;
	private String newNoteModel;

	private int readyToSend = 0;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_note);

		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		int width = displaymetrics.widthPixels;

		PackageManager pm = this.getPackageManager();
		boolean hasCamera = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);

		editorText_title = (EditText) findViewById(R.id.editText_note_title);
		editorText_description = (EditText) findViewById(R.id.editText_note_description);
		image_imageView = (ImageView) findViewById(R.id.imageView);
		photoView = (ImageView) findViewById(R.id.photoView);

		camera_button = (Button) findViewById(R.id.button_note_camera);

		photo_button = (Button) findViewById(R.id.button_note_photo);
		photo_button.setOnClickListener(this);

		if (hasCamera) {
			camera_button.setOnClickListener(this);
		} else {
			camera_button.setVisibility(View.GONE);
		}

		save_button = (Button) findViewById(R.id.button_note_save);
		save_button.setOnClickListener(this);

		button_note_back = (Button) findViewById(R.id.button_note_back);

		save_button.setWidth(width / 2);
		button_note_back.setWidth(width / 2);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.loged_in, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.button_note_camera) {
			Intent intent = new Intent(
					android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
			startActivityForResult(intent, CAMERA_CODE);
		} else if (v.getId() == R.id.button_note_photo) {
			Intent intent = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(intent, LOAD_IMAGES_CODE);
		} else if (v.getId() == R.id.button_note_save) {
			if (image != null) {
				InputStream is = new ByteArrayInputStream(photo);

				uploadPicture aa = new uploadPicture();
				aa.execute(is);
			}

			if (photo != null) {
				InputStream is = new ByteArrayInputStream(photo);

				uploadPicture aa = new uploadPicture();
				aa.execute(is);
			}

			if (readyToSend == 0) {
				String title = String.valueOf(editorText_title.getText());
				String description = String.valueOf(editorText_description
						.getText());

				List<String> images = resourseUrls;

				JSONObject object = new JSONObject();
				try {
					object.put("Title", title);
					object.put("Text", description);
					object.put("Images", images);

				} catch (Exception ex) {
				}

				newNoteModel = object.toString();

				CreateNote noter = new CreateNote();
				noter.execute(new String[] {
						"http://mobileorganizer.apphb.com/api/notes/create",
						newNoteModel });
			}
		} else if (v.getId() == R.id.button_note_back) {
			Intent goToHomePageIntent = new Intent(new Intent(
					CreateNoteActivity.this, HomeActivity.class));
			startActivity(goToHomePageIntent);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == CAMERA_CODE) {
			Bundle extras = data.getExtras();
			if (extras != null) {
				Bitmap image = (Bitmap) extras.get("data");
				image_imageView.setImageBitmap(image);

				File folder = new File(
						Environment.getExternalStorageDirectory()
								+ "/myAppDir/myImages/");
				if (!folder.exists()) {
					if (folder.mkdirs())
						Toast.makeText(this, "New Folder Created",
								Toast.LENGTH_SHORT).show();
				}

				String iconsStoragePath = Environment
						.getExternalStorageDirectory() + "/myAppDir/myImages/";
				long dafa = System.currentTimeMillis() / 1000;

				String name = "IMG_" + String.valueOf(dafa) + ".png";

				File file = new File(iconsStoragePath, name);
				// Convert bitmap to byte array
				Bitmap bitmap = image;
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				bitmap.compress(CompressFormat.PNG, 0 /* ignored for PNG */, bos);
				photo = bos.toByteArray();

				// write the bytes in file
				FileOutputStream fos = null;
				try {
					fos = new FileOutputStream(file);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					fos.write(photo);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				readyToSend++;
			}
		} else if (requestCode == LOAD_IMAGES_CODE) {

			File folder = new File(Environment.getExternalStorageDirectory()
					+ "/myAppDir/myImages/");
			if (!folder.exists()) {
				if (folder.mkdirs())
					Toast.makeText(this, "New Folder Created",
							Toast.LENGTH_SHORT).show();
			}

			if (data == null) {
				return;
			}
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			// file path of captured image
			String filePath = cursor.getString(columnIndex);
			File f = new File(filePath);
			String filename = f.getName();
			cursor.close();

			Bitmap bitmap = BitmapFactory.decodeFile(filePath);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			bitmap.compress(CompressFormat.PNG, 0 /* ignored for PNG */, bos);
			image = bos.toByteArray();
			photoView.setImageBitmap(bitmap);
			String iconsStoragePath = Environment.getExternalStorageDirectory()
					+ "/myAppDir/myImages/";

			File file2 = new File(iconsStoragePath, filename);

			// write the bytes in file
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(file2);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				fos.write(image);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			readyToSend++;
		} else if (requestCode == VIDEO_CODE)

			Toast.makeText(this, "Record", Toast.LENGTH_LONG).show();
	}

	private class uploadPicture extends AsyncTask<InputStream, Void, String> {

		private ProgressDialog progress;

		@Override
		protected String doInBackground(InputStream... arg0) {

			InputStream is = arg0[0];

			Map<String, String> config = new HashMap<String, String>();
			config.put("cloud_name", "djlwcsyiz");
			config.put("api_key", "781383948985498");
			config.put("api_secret", "Vh5BQmeTxvSKvTGTg-wRDYKqPz4");
			Cloudinary cloudinary = new Cloudinary(config);

			String loudScreaming = null;
			try {

				JSONObject dsd = cloudinary.uploader().upload(is,
						Cloudinary.emptyMap());
				loudScreaming = dsd.getString("url");
				Log.d("My", loudScreaming);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return loudScreaming;
		}

		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progress = ProgressDialog.show(CreateNoteActivity.this, "Wait",
					"Please wait");
			progress.show();
		}

		@Override
		protected void onPostExecute(String result) {
			progress.dismiss();
			Toast.makeText(CreateNoteActivity.this, result, Toast.LENGTH_LONG)
					.show();
			resourseUrls.add(result);
			readyToSend--;

			if (readyToSend == 0) {
				String title = String.valueOf(editorText_title.getText());
				String description = String.valueOf(editorText_description
						.getText());

				List<String> images = resourseUrls;
				JSONArray jsArray = new JSONArray(images);

				JSONObject object = new JSONObject();
				try {
					object.put("Title", title);
					object.put("Text", description);
					object.put("Images", jsArray);

				} catch (Exception ex) {
				}

				newNoteModel = object.toString();

				CreateNote noter = new CreateNote();
				noter.execute(new String[] {
						"http://mobileorganizer.apphb.com/api/notes/create",
						newNoteModel });
			}
		}
	}

	private class CreateNote extends AsyncTask<String, Void, Void> {

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
			Toast.makeText(CreateNoteActivity.this, "Created",
					Toast.LENGTH_LONG).show();
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progress = ProgressDialog.show(CreateNoteActivity.this, "Wait",
					"Please wait");
			progress.show();
		}
	}
}
