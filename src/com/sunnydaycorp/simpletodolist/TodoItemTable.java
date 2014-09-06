package com.sunnydaycorp.simpletodolist;

public class TodoItemTable {

	public static final String TABLE_NAME = "todo_items";
	public static final String ID_COL_NAME = "_id";
	public static final String NAME_COL_NAME = "todo_name";
	public static final String PRIORITY_COL_NAME = "todo_priority";
	public static final String STATUS_COL_NAME = "todo_status";

	public static String getCreateSQL() {
		return "CREATE TABLE " + TABLE_NAME + " ("
				+ "_id INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME_COL_NAME
				+ " TEXT, " + PRIORITY_COL_NAME + " INTEGER, "
				+ STATUS_COL_NAME + " TEXT);";
	}

	public static String getInsertNewTodoSQL(String name, int priority,
			String status) {
		return "INSERT INTO " + TABLE_NAME + " (" + NAME_COL_NAME + ", "
				+ PRIORITY_COL_NAME + ", " + STATUS_COL_NAME + ") "
				+ " SELECT '" + name + "' as " + NAME_COL_NAME + ", "
				+ priority + " as " + PRIORITY_COL_NAME + ", '" + status
				+ "' as " + STATUS_COL_NAME + ";";
	}

	public static String getDropTableSQL() {
		return "DROP TABLE IF EXIST " + TABLE_NAME + ";";
	}

}
