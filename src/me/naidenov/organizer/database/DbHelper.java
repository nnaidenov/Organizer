package me.naidenov.organizer.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper {
	private static final String DB_NAME = "organizer";
	private static final int DB_VERSION = 1;

	public DbHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE users (_id integer primary key autoincrement, "
				+ "email text not null, sessionKey text, authCode text not null);");

		db.execSQL("CREATE TABLE todos (_id integer primary key autoincrement, "
				+ "title text not null, description text not null, date text not null, "
				+ "reminder text not null, isDone integer not null, priority integer not null, "
				+ "userId integer not null);");
		
		Log.d("My", "DbCreated");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	public SQLiteDatabase open() {
		return getWritableDatabase();
	}

	public void close() {
		close();
	}
}
