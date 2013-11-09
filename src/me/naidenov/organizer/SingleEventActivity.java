package me.naidenov.organizer;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class SingleEventActivity extends Activity {
	private TextView id;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_single_event);

		id = (TextView) findViewById(R.id.textView_id);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		int idd = getIntent().getIntExtra("todoId", -1);

		id.setText(String.valueOf(idd));
	}
}
