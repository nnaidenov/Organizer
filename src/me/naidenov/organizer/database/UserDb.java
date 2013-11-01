package me.naidenov.organizer.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UserDb {
	private SQLiteDatabase db;
	private static final String TABLE_NAME = "users";

	public UserDb(SQLiteDatabase db) {
		this.db = db;
	}

	public void addUser(String email, String authCode) throws Exception {
		if (email != null && authCode.length() == 40) {
			String trimedEmail = email.trim();
			
			ContentValues values = new ContentValues();
			values.put("email", trimedEmail);
			values.put("authCode", authCode);
			
			db.insert(TABLE_NAME, null, values);
		} else {
			throw new Exception("Url cannot be empty!");
		}
	}
	
	public Cursor getAllFeeds() {
		Cursor feedCursor = this.db.query(TABLE_NAME, new String[] {"_id","feed_url"}, null, null, null, null, null);
		
		return feedCursor;
	}
	
	public void deleteFeed(long id) {
		this.db.delete(TABLE_NAME, "_id = ?", new String[] {String.valueOf(id)});
	}
}
