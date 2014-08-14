package com.sunnydaycorp.simpletodolist;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TodoListItemAdapter extends ArrayAdapter<TodoItem> {

	private final LayoutInflater mInflater;
	private ArrayList<TodoItem> todoItems;

	public TodoListItemAdapter(Context context, ArrayList<TodoItem> todoItems) {
		super(context, R.layout.todo_list_element, todoItems);
		this.todoItems = todoItems;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View rowView = mInflater.inflate(R.layout.todo_list_element, parent,
				false);
		TextView tvName = (TextView) rowView
				.findViewById(R.id.tvItemName);
		tvName.setText(todoItems.get(position).getName());
		return rowView;

	}

	@Override
	public long getItemId(int position) {

		return todoItems.get(position).getId();

	}
	
	public void updateTodoListData(ArrayList<TodoItem> todoItems){
		this.todoItems = todoItems;
		clear();
		addAll(todoItems);
	}
	
}