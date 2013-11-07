package me.naidenov.organizer;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;


@SuppressWarnings("deprecation")
public class CalendarMonthViewActivity extends TabActivity{

	private TabHost mTabHost;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_activity_layout);

		mTabHost = getTabHost();
		
		Intent intentWeek = new Intent(this,WeekViewActivity.class);
		TabHost.TabSpec specWeek = mTabHost.newTabSpec("Week").setIndicator("Week").setContent(intentWeek);
		mTabHost.addTab(specWeek);
		
		Intent intentMonth = new Intent(this,MonthViewActivity.class);
		TabHost.TabSpec specMonth = mTabHost.newTabSpec("Month").setIndicator("Month").setContent(intentMonth);
		mTabHost.addTab(specMonth);
		
		Intent intentDay = new Intent(this,DayViewActivity.class);
		TabHost.TabSpec specDay = mTabHost.newTabSpec("Day").setIndicator("Day").setContent(intentDay);
		mTabHost.addTab(specDay);
	}

	// private class LoginUser extends AsyncTask<String, Void, Void> {
	//
	// private String result;
	//
	// @Override
	// protected Void doInBackground(String... params) {
	// HttpClient client = new DefaultHttpClient();
	// HttpPost post = new HttpPost(params[0]);
	//
	// try {
	// post.setEntity(new StringEntity(params[1], "UTF8"));
	// post.setHeader("Content-type", "application/json");
	// HttpResponse response = client.execute(post);
	//
	// HttpEntity entity = response.getEntity();
	// InputStream webs = entity.getContent();
	//
	// BufferedReader bufferReader = new BufferedReader(
	// new InputStreamReader(webs));
	//
	// StringBuilder builder = new StringBuilder();
	// String chunk = null;
	//
	// while ((chunk = bufferReader.readLine()) != null) {
	// builder.append(chunk + "\n");
	// }
	// webs.close();
	// result = builder.toString();
	//
	// } catch (ClientProtocolException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// JSONObject obj;
	// try {
	// obj = new JSONObject(result);
	// String sessionKey = obj.getString("sessionKey");
	//
	// OutputStream os = openFileOutput("sessionKey", Context.MODE_PRIVATE);
	// os.write(sessionKey.getBytes());
	// os.close();
	//
	// Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
	// startActivity(intent);
	// } catch (JSONException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (FileNotFoundException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// return null;
	// }
	//
	// @Override
	// protected void onPostExecute(Void result) {
	// // UserDb userDb = ((OrganizerApplication)getApplication()).getUserDb();
	// //
	// // userDb.addUser(email, authCode, sessionKey);
	// progress.dismiss();
	// }
	//
	// @Override
	// protected void onPreExecute() {
	// // TODO Auto-generated method stub
	// super.onPreExecute();
	// progress = ProgressDialog.show(LoginActivity.this, "Wait",
	// "Please wait");
	// progress.show();
	// }
	//
}