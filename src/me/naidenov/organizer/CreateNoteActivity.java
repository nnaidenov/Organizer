package me.naidenov.organizer;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class CreateNoteActivity extends Activity implements OnClickListener {
	private static final int CAMERA_CODE = 0;

	private Button camera_button;
	private ImageView image_imageView;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_note);

		PackageManager pm = this.getPackageManager();
		boolean hasCamera = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);

		camera_button = (Button) findViewById(R.id.button_note_camera);
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
			}
		}
	}
}
