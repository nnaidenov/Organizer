package me.naidenov.organizer;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;

public class CreateTodoActivity extends Activity {
	private Button save_button;
	private Button back_button;
	private Button choose_date;


	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_todo);

		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		int width = displaymetrics.widthPixels;

		save_button = (Button) findViewById(R.id.button_create_todo_save);
		back_button = (Button) findViewById(R.id.button_create_todo_back);
		save_button.setWidth(width / 2);
		back_button.setWidth(width / 2);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.loged_in, menu);
		return true;
	}
}
