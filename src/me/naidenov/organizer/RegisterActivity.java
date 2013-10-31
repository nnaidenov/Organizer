package me.naidenov.organizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import me.naidenov.organizer.persister.*;

public class RegisterActivity extends Activity implements OnClickListener{
	
	private Button button_register;
	private EditText editorText_email;
	private EditText editorText_password;
	private EditText editorText_rePassword;
	private String registerModel;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		editorText_email = (EditText) findViewById(R.id.editText_email_register);
		editorText_password = (EditText) findViewById(R.id.editText_password_register);
		editorText_rePassword = (EditText) findViewById(R.id.editText_rePassword_register);

		button_register = (Button) findViewById(R.id.button_register);
		button_register.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.button_register) {
			String email = String.valueOf(editorText_email.getText());
			String password = String.valueOf(editorText_password.getText());
			String rePassword = String.valueOf(editorText_rePassword.getText());
			
			if (!password.equals(rePassword)) {
				editorText_password.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
				editorText_rePassword.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
				Toast.makeText(this, "Authentication Failed...", Toast.LENGTH_SHORT).show();
				return;
			}

			String authCode = "";
			try {
				authCode = sha1(password);
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			JSONObject object = new JSONObject();
			try {

				object.put("email", email);
				object.put("authCode", authCode);

			} catch (Exception ex) {

			}
			
			registerModel = object.toString();
			RegisterUser register = new RegisterUser();
			register.execute(new String[] {"http://mobileorganizer.apphb.com/api/users/register",registerModel});
		}
		
	}
	
	private class RegisterUser extends AsyncTask<String, Void, Void> {

		private String result;

		@Override
		protected Void doInBackground(String... params) {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(params[0]);
			
			try {
				post.setEntity(new StringEntity(params[1], "UTF8"));
				post.setHeader("Content-type", "application/json");
				HttpResponse response = client.execute(post);

				HttpEntity entity = response.getEntity();
				InputStream webs = entity.getContent();

				BufferedReader bufferReader = new BufferedReader(
						new InputStreamReader(webs));

				StringBuilder builder = new StringBuilder();
				String chunk = null;

				while ((chunk = bufferReader.readLine()) != null) {
					builder.append(chunk + "\n");
				}
				webs.close();
				result = builder.toString();

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			JSONObject obj;
			try {
				obj = new JSONObject(result);
				Log.d("JSON", obj.getString("sessionKey"));
				Log.d("JSON", obj.getString("email"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}		
	}
	
	static String sha1(String input) throws NoSuchAlgorithmException {
		MessageDigest mDigest = MessageDigest.getInstance("SHA1");
		byte[] result = mDigest.digest(input.getBytes());
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < result.length; i++) {
			sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16)
					.substring(1));
		}

		return sb.toString();
	}
}