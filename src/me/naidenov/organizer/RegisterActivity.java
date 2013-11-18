package me.naidenov.organizer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
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
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends Activity implements OnClickListener {

	private Button button_register;
	private EditText editorText_email;
	private EditText editorText_password;
	private EditText editorText_rePassword;
	private String registerModel;
	private ProgressDialog progress;

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

			if (email.trim().equals("")) {
				editorText_email.setError("E-mail is required!");
				return;
				// You can Toast a message here that the Username is Empty
			}
			if (password.trim().equals("")) {
				editorText_password.setError("Password is required!");
				return;
				// You can Toast a message here that the Username is Empty
			}
			if (rePassword.trim().equals("")) {
				editorText_rePassword.setError("Re-Password is required!");
				return;
				// You can Toast a message here that the Username is Empty
			}
			if (!password.equals(rePassword)) {
				editorText_rePassword
						.setError("Password and Re-Password are not same!");
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
			register.execute(new String[] {
					"http://mobileorganizer.apphb.com/api/users/register",
					registerModel });
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

				if (response.getStatusLine().getStatusCode() != 201) {
					result = "No";
					return null;
				}

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
				String sessionKey = obj.getString("sessionKey");

				OutputStream os = openFileOutput("sessionKey",
						Context.MODE_PRIVATE);
				os.write(sessionKey.getBytes());
				os.close();
				result = "No";
				Intent intent = new Intent(RegisterActivity.this,
						HomeActivity.class);
				startActivity(intent);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void res) {
			// UserDb userDb =
			// ((OrganizerApplication)getApplication()).getUserDb();
			//
			// userDb.addUser(email, authCode, sessionKey);

			progress.dismiss();

			if (result.equals("No")) {
				Toast.makeText(RegisterActivity.this, "Wrong e-mail!",
						Toast.LENGTH_LONG).show();
			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progress = ProgressDialog.show(RegisterActivity.this, "Wait",
					"Please wait");
			progress.show();
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