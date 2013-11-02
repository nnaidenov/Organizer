package me.naidenov.organizer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CreateTodoActivity extends  FragmentActivity implements OnClickListener {
	private Button save_button;
	private Button back_button;
	
	private EditText title;
	private EditText description;
	private TextView date;

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
		} 
	}
}
