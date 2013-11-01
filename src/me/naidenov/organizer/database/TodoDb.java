package me.naidenov.organizer.database;

import android.database.sqlite.SQLiteDatabase;

public class TodoDb {
	private SQLiteDatabase db;
	private static final String TABLE_NAME = "todos";

	public TodoDb(SQLiteDatabase db) {
		this.db = db;
	}
}
