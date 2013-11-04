package me.naidenov.organizer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.xml.sax.InputSource;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.FROYO)
public class CreateNoteActivity extends Activity implements OnClickListener {
	private static final int CAMERA_CODE = 0;
	private static final int LOAD_IMAGES_CODE = 1;

	private Button camera_button;
	private Button photo_button;
	private ImageView image_imageView;
	private String filePath;
	private String filename;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_note);

		PackageManager pm = this.getPackageManager();
		boolean hasCamera = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);

		camera_button = (Button) findViewById(R.id.button_note_camera);
		photo_button = (Button) findViewById(R.id.button_note_photo);
		photo_button.setOnClickListener(this);
		if (hasCamera) {
			camera_button.setOnClickListener(this);
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
			startActivityForResult(intent, CAMERA_CODE);
		} else if (v.getId() == R.id.button_note_photo) {
			Intent intent = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(intent, LOAD_IMAGES_CODE);
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

				String iconsStoragePath = Environment
						.getExternalStorageDirectory() + "/myAppDir/myImages/";
				String name = "dsds";

				File file = new File(iconsStoragePath, name);

				// Convert bitmap to byte array
				Bitmap bitmap = image;
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				bitmap.compress(CompressFormat.PNG, 0 /* ignored for PNG */, bos);
				byte[] bitmapdata = bos.toByteArray();

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
			} else if(requestCode == LOAD_IMAGES_CODE){
				String iconsStoragePath = Environment
						.getExternalStorageDirectory() + "/myAppDir/myImages/";
				String name = "dsdsa1";

				File file2 = new File(iconsStoragePath, name);
				
				Uri selectedImage = data.getData();
				String[] filePathColumn = { MediaStore.Images.Media.DATA };

				Cursor cursor = getContentResolver().query(selectedImage,
						filePathColumn, null, null, null);
				cursor.moveToFirst();

				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				// file path of captured image
				filePath = cursor.getString(columnIndex);
				cursor.close();
				
				Bitmap bitmap = BitmapFactory.decodeFile(filePath);
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				bitmap.compress(CompressFormat.PNG, 0 /* ignored for PNG */, bos);
				byte[] bitmapdata = bos.toByteArray();

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
			}
		}
	}
}