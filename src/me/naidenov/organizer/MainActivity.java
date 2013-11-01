package me.naidenov.organizer;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {

	private Button buttonRegister;
	private Button buttonLogin;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		buttonRegister = (Button) findViewById(R.id.button_register_view);
		buttonRegister.setOnClickListener(this);

		buttonLogin = (Button) findViewById(R.id.button_login_view);
		buttonLogin.setOnClickListener(this);
	}
	
	

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
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
		
		if (total.length() > 0) {
			Intent intent = new Intent(MainActivity.this, HomeActivity.class);
			startActivity(intent);
		}
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.button_login_view) {
			Intent intent = new Intent(MainActivity.this, LoginActivity.class);
			startActivity(intent);
		} else if (v.getId() == R.id.button_register_view) {
			Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
			startActivity(intent);
		}
	}
}
