package me.naidenov.organizer;

import me.naidenov.organizer.database.DbHelper;
import me.naidenov.organizer.database.TodoDb;
import me.naidenov.organizer.database.UserDb;
import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

public class OrganizerApplication extends Application {

	private SQLiteDatabase db = null;
	private UserDb userDb = null;
	private TodoDb todoDb = null;

	public SQLiteDatabase getDb() {
		if (db == null) {
			db = new DbHelper(getApplicationContext()).open();
		}
		return db;
	}

	public UserDb getUserDb() {
		if(this.userDb == null) {
			this.userDb = new UserDb(getDb());
		}
		
		return this.userDb;
	}
	
	public TodoDb getTodoDb() {
		if(this.todoDb == null) {
			this.todoDb = new TodoDb(getDb());
		}
		
		return this.todoDb;
	}
}
