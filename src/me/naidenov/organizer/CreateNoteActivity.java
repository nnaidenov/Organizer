package me.naidenov.organizer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.InputSource;

import com.cloudinary.Cloudinary;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.FROYO)
public class CreateNoteActivity extends Activity implements OnClickListener {
	private static final int CAMERA_CODE = 0;
	private static final int VIDEO_CODE = 2;
	private static final int LOAD_IMAGES_CODE = 1;

	private Button camera_button;
	private Button video_button;
	private Button photo_button;
	private ImageView image_imageView;
	private String mCapturedImageURI;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_note);

		PackageManager pm = this.getPackageManager();
		boolean hasCamera = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);

		camera_button = (Button) findViewById(R.id.button_note_camera);
		video_button = (Button) findViewById(R.id.button_note_video);

		photo_button = (Button) findViewById(R.id.button_note_photo);
		photo_button.setOnClickListener(this);
		if (hasCamera) {
			camera_button.setOnClickListener(this);
			video_button.setOnClickListener(this);
		} else {
			camera_button.setVisibility(View.GONE);
		}

		image_imageView = (ImageView) findViewById(R.id.imageView);
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
		} else if (v.getId() == R.id.button_note_video) {
			Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
			startActivityForResult(takeVideoIntent, VIDEO_CODE);
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
				byte[] bitmapdata = bos.toByteArray();

				InputStream is = new ByteArrayInputStream(bitmapdata);
				
				uploadPicture aa = new uploadPicture();
				aa.execute(is);
				// write the bytes in file
				FileOutputStream fos = null;
				try {
					fos = new FileOutputStream(file);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					fos.write(bitmapdata);
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
			byte[] bitmapdata = bos.toByteArray();

			String iconsStoragePath = Environment.getExternalStorageDirectory()
					+ "/myAppDir/myImages/";

			File file2 = new File(iconsStoragePath, filename);
			
			InputStream is = new ByteArrayInputStream(bitmapdata);
			
			uploadPicture aa = new uploadPicture();
			aa.execute(is);

			// write the bytes in file
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(file2);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				fos.write(bitmapdata);
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
		} else if (requestCode == VIDEO_CODE)

			Toast.makeText(this, "Record", Toast.LENGTH_LONG).show();
	}
	
private class uploadPicture extends AsyncTask<InputStream, Void, Void>{

	@Override
	protected Void doInBackground(InputStream... arg0) {

		InputStream is = arg0[0];
		
		Map config = new HashMap();
		config.put("cloud_name", "djlwcsyiz");
		config.put("api_key", "781383948985498");
		config.put("api_secret", "Vh5BQmeTxvSKvTGTg-wRDYKqPz4");
		Cloudinary cloudinary = new Cloudinary(config);
		cloudinary.url().generate("saaa");
		
		try {

		JSONObject dsd = cloudinary.uploader().upload(is, Cloudinary.emptyMap());
		String loudScreaming = dsd.getString("url");
		Log.d("My", loudScreaming);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
		
		
	}
}
