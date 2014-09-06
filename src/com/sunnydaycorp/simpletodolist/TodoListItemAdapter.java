package com.sunnydaycorp.simpletodolist;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class TodoListItemAdapter extends ArrayAdapter<TodoItem> {

	private final LayoutInflater mInflater;
	private ArrayList<TodoItem> todoItems;
	private TodoItemDAO todoItemDAO = null;

	public TodoListItemAdapter(Context context, ArrayList<TodoItem> todoItems) {
		super(context, R.layout.todo_list_element, todoItems);
		this.todoItems = todoItems;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		todoItemDAO = new TodoItemDAO(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View rowView = mInflater.inflate(R.layout.todo_list_element, parent,
				false);
		TextView tvName = (TextView) rowView.findViewById(R.id.tvItemName);
		CheckBox cbCompleted = (CheckBox) rowView
				.findViewById(R.id.cbItemCompleted);

		tvName.setText(todoItems.get(position).getName());
		cbCompleted.setTag(todoItems.get(position).getId());

		// format completed items
		if (todoItems.get(position).getStatus() == TodoItem.Status.DONE) {
			cbCompleted.setChecked(true);
			tvName.setPaintFlags(tvName.getPaintFlags()
					| Paint.STRIKE_THRU_TEXT_FLAG);
			tvName.setTextAppearance(getContext(), R.style.MyCompletedTodo);
		}

		// set onClickListener for completed status checkbox
		cbCompleted.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				TodoItem.Status status = null;
				if (isChecked) {
					status = TodoItem.Status.DONE;
				} else {
					status = TodoItem.Status.ACTIVE;
				}
				// update todo item completion status
				todoItemDAO.updateTodoItemCompletionStaus(
						Integer.valueOf(buttonView.getTag().toString()), status);
				// refresh ListView
				updateTodoListData();
			}
		});

		return rowView;

	}

	@Override
	public long getItemId(int position) {

		return todoItems.get(position).getId();

	}

	public void updateTodoListData() {
		todoItems = todoItemDAO.getTodoItemList();
		clear();
		addAll(todoItems);
	}

}