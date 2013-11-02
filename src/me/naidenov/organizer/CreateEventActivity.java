package me.naidenov.organizer;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class CreateEventActivity extends Activity {
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_event);
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.loged_in, menu);
		return true;
	}
}
