package com.sunnydaycorp.simpletodolist;

import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class TodoItemDAO {
	
	private SimpleTodoListDB localDB = null;	
	String[] columnNames = { TodoItemTable.ID_COL_NAME, TodoItemTable.NAME_COL_NAME,
			TodoItemTable.PRIORITY_COL_NAME, TodoItemTable.STATUS_COL_NAME };

	public TodoItemDAO(Context context) {
		localDB = SimpleTodoListDB.getSimpleTodoListDB(context);
	}
	
	public ArrayList<TodoItem> getTodoItemList() {

		ArrayList<TodoItem> allTodoItems = null;

		// Get cursor with all active todos info
		SQLiteDatabase db = localDB.getReadableDatabase();
		Cursor todosCursor = db.query(TodoItemTable.TABLE_NAME, columnNames, TodoItemTable.STATUS_COL_NAME + "='"
				+ TodoItem.Status.ACTIVE.toString() + "' OR " + TodoItemTable.STATUS_COL_NAME + "='"
				+ TodoItem.Status.DONE.toString() + "'", null, null, null, null);

		if (todosCursor.getCount() > 0) {
			todosCursor.moveToFirst();
			allTodoItems = createTodoItemList(todosCursor);
		} else {
			allTodoItems = new ArrayList<TodoItem>();
		}
		todosCursor.close();
		return allTodoItems;
	}
	
	public TodoItem getTodoItemInfo(long id) {

		TodoItem todo = null;
		
		// Get cursor with the todo info
		SQLiteDatabase db = localDB.getReadableDatabase();
		Cursor todoCursor = db.query(TodoItemTable.TABLE_NAME, columnNames,
				TodoItemTable.ID_COL_NAME + "=" + id, null, null, null, null);

		if (todoCursor.getCount() > 0) {
			todoCursor.moveToFirst();
			todo = createTodoFromData(todoCursor);
		}
		todoCursor.close();
		return todo;
	}
	
	public boolean saveNewTodoItem (String name){
		
		boolean transactionStatus = false;

		// Save new todo info
		SQLiteDatabase db = localDB.getWritableDatabase();
		db.beginTransaction();
		// default priority is 0
		db.execSQL(TodoItemTable.getInsertNewTodoSQL(name, 0, TodoItem.Status.ACTIVE.toString()));
		db.setTransactionSuccessful();
		transactionStatus = true;
		db.endTransaction();

		return transactionStatus;
	}
	
	public boolean updateTodoItem (long id, String name){
		
		boolean transactionStatus = false;

		// Update name for the todo
		SQLiteDatabase db = localDB.getWritableDatabase();
		db.beginTransaction();
		
		ContentValues values = new ContentValues();
		values.put(TodoItemTable.NAME_COL_NAME, name);
		
		db.update(TodoItemTable.TABLE_NAME, values, TodoItemTable.ID_COL_NAME + "=" + id, null);
		db.setTransactionSuccessful();
		transactionStatus = true;
		db.endTransaction();

		return transactionStatus;
		
	}
	
	public boolean updateTodoItemCompletionStaus (long id, TodoItem.Status status){
		
		boolean transactionStatus = false;
		
		if (status == TodoItem.Status.DONE || status == TodoItem.Status.ACTIVE){

			// Update status to DONE or ACTIVE for the todo
			SQLiteDatabase db = localDB.getWritableDatabase();
			db.beginTransaction();
			
			ContentValues values = new ContentValues();
			values.put(TodoItemTable.STATUS_COL_NAME, status.toString());
			
			db.update(TodoItemTable.TABLE_NAME, values, TodoItemTable.ID_COL_NAME + "=" + id, null);
			db.setTransactionSuccessful();
			transactionStatus = true;
			db.endTransaction();
		}

		return transactionStatus;
		
	}
	
	public boolean deleteTodoItem (long id){
		
		boolean transactionStatus = false;

		// Update status to deleted for the todo
		SQLiteDatabase db = localDB.getWritableDatabase();
		db.beginTransaction();
		
		ContentValues values = new ContentValues();
		values.put(TodoItemTable.STATUS_COL_NAME, TodoItem.Status.DELETED.toString());
		
		db.update(TodoItemTable.TABLE_NAME, values, TodoItemTable.ID_COL_NAME + "=" + id, null);
		db.setTransactionSuccessful();
		transactionStatus = true;
		db.endTransaction();

		return transactionStatus;
		
	}
	
	private ArrayList<TodoItem> createTodoItemList(Cursor c) {
		ArrayList<TodoItem> todoList = new ArrayList<TodoItem>();
		if (c.getCount() > 0) {
			c.moveToFirst();
			while (!c.isAfterLast()) {
				TodoItem todo = createTodoFromData(c);
				if (todo != null) {
					todoList.add(todo);
				}
				c.moveToNext();
			}

		}
		return todoList;
	}

	private TodoItem createTodoFromData(Cursor c) {
		TodoItem todo = null;
		if (c.getPosition() >= 0) {
			todo = new TodoItem();
			try {
				todo.setId(c.getLong(c
						.getColumnIndexOrThrow(TodoItemTable.ID_COL_NAME)));
				todo.setName(c.getString(c
						.getColumnIndexOrThrow(TodoItemTable.NAME_COL_NAME)));
				todo.setPriority(c.getInt(c
						.getColumnIndexOrThrow(TodoItemTable.PRIORITY_COL_NAME)));
				todo.setStatus(TodoItem.Status.valueOf(c.getString(c
						.getColumnIndexOrThrow(TodoItemTable.STATUS_COL_NAME))));
			} catch (IllegalArgumentException iax) {
				todo = null;
			}
		}
		return todo;
	}

}
