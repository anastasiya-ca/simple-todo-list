package com.sunnydaycorp.simpletodolist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;


public class SimpleTodoListDB extends SQLiteOpenHelper {
	
	private static final String DB_NAME = "simpleTodoListDB";
	private static final int DB_VERSION = 1;
	private static SimpleTodoListDB simpleTodoListDB = null;

	public static SimpleTodoListDB getSimpleTodoListDB(Context context) {
		if (simpleTodoListDB == null) {
			// Create the SQLiteOpenHelper if does not exist
			CursorFactory factory = null;
			simpleTodoListDB = new SimpleTodoListDB(context, DB_NAME, factory);
		}
		return simpleTodoListDB;
	}

	private SimpleTodoListDB(Context context, String name,
			SQLiteDatabase.CursorFactory factory) {
		super(context, name, factory, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		createTables(db);
		insertTestData(db);
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// proper behavior is to be implemented
		dropTables(db);
		createTables(db);
	}

	private void createTables(SQLiteDatabase db) {
		db.execSQL(TodoItemTable.getCreateSQL());
	}

	private void dropTables(SQLiteDatabase db) {
		db.execSQL(TodoItemTable.getDropTableSQL());
	}
	
	private void insertTestData(SQLiteDatabase db) {
		db.execSQL(TodoItemTable.getInsertNewTodoSQL("Pack Bag", 1, TodoItem.Status.ACTIVE.toString()));
		db.execSQL(TodoItemTable.getInsertNewTodoSQL("Buy Milk", 2, TodoItem.Status.ACTIVE.toString()));
		db.execSQL(TodoItemTable.getInsertNewTodoSQL("Pickup Laundry", 3, TodoItem.Status.ACTIVE.toString()));
		db.execSQL(TodoItemTable.getInsertNewTodoSQL("Clean Dishes", 4, TodoItem.Status.ACTIVE.toString()));	
	}

}
