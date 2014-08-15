package com.sunnydaycorp.simpletodolist;

import java.util.ArrayList;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class TodoActivity extends Activity {
	
	private ListView lvTodoItems = null;
	private EditText etNewItemName = null;
	private TodoListItemAdapter adapter = null;
	
	private ArrayList <TodoItem> todoItems;
	private TodoItemDAO todoItemDAO = null;
	private InputMethodManager imm = null;
		
	private OnTodoUpdatedListener onTodoUpdatedListener = new OnTodoUpdatedListener() {

		@Override
		public void onTodoUpdated(DialogInterface dialog, long id) {
			todoItems = todoItemDAO.getTodoItemList();
			adapter.updateTodoListData(todoItems);	
		}	

	};
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo);
		
		etNewItemName = (EditText) findViewById(R.id.etNewItem);
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		
		// get todo items data
		todoItemDAO = new TodoItemDAO(this);
		todoItems = todoItemDAO.getTodoItemList();

		// set adapter for the ListView
		adapter = new TodoListItemAdapter(this, todoItems);
		lvTodoItems = (ListView) findViewById(R.id.lvItems);
		lvTodoItems.setAdapter(adapter);

		// set onItemClickListener for the ListView to show Update Todo Dialog
		lvTodoItems.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View clickView,
					int position, long id) {
				closeInputFromWindow();
				showUpdateTodoDialog(id);
			}
			
		});
		
		// set onItemClickListener for the ListView to delete selected todo item
		lvTodoItems.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				// deleted todo item from data - set status as deleted
				todoItemDAO.deleteTodoItem(id);
				// refresh ListView
				todoItems = todoItemDAO.getTodoItemList();
				adapter.updateTodoListData(todoItems);
				return true;
			}
	
		});
			
	}
	
	private void showUpdateTodoDialog(long id) {
			DialogFragment newFragment = new UpdateTodoFragment(this,
					onTodoUpdatedListener, id);
			newFragment.show(getFragmentManager(), "updateTodoDialog");
	}
				
	
	public void addNewTodo(View view){
		String name = etNewItemName.getText().toString().trim();
		
		if (name.equals("")) {
			Toast.makeText(getApplicationContext(), "Please enter new todo name",
					Toast.LENGTH_SHORT).show();
			return;
		}
		else {
			//  if not empty name then save new todo
			todoItemDAO.saveNewTodoItem(name);
			// refresh ListView
			todoItems = todoItemDAO.getTodoItemList();
			adapter.updateTodoListData(todoItems);
			//clean EditText field
			etNewItemName.setText("");
			etNewItemName.clearFocus();
			closeInputFromWindow();
		}

	}
	
	private void closeInputFromWindow() {
		imm.hideSoftInputFromWindow(etNewItemName.getWindowToken(), 0);
	}
	
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.todo, menu);
//		return true;
//	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			// to be implemented
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
