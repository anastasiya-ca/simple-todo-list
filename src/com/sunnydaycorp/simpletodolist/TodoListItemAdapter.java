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
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class TodoListItemAdapter extends ArrayAdapter<TodoItem> {

	private ArrayList<TodoItem> todoItems;
	private TodoItemDAO todoItemDAO;

	public TodoListItemAdapter(Context context, ArrayList<TodoItem> todoItems) {
		super(context, R.layout.todo_list_element, todoItems);
		this.todoItems = todoItems;
		this.todoItemDAO = new TodoItemDAO(context);
	}

	public static class ViewHolder {
		TextView tvName;
		CheckBox cbCompleted;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TodoItem item = getItem(position);

		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.todo_list_element, parent, false);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.tvName = (TextView) convertView.findViewById(R.id.tvItemName);
			viewHolder.cbCompleted = (CheckBox) convertView.findViewById(R.id.cbItemCompleted);
			convertView.setTag(viewHolder);
		}

		ViewHolder viewHolder = (ViewHolder) convertView.getTag();

		viewHolder.tvName.setText(item.getName());
		viewHolder.cbCompleted.setTag(item);

		// format completed items
		if (item.getStatus() == TodoItem.Status.DONE) {
			viewHolder.cbCompleted.setChecked(true);
			viewHolder.tvName.setPaintFlags(viewHolder.tvName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
			viewHolder.tvName.setTextAppearance(getContext(), R.style.MyCompletedTodo);
		} else {
			viewHolder.cbCompleted.setChecked(false);
			viewHolder.tvName.setPaintFlags(0);
			viewHolder.tvName.setTextAppearance(getContext(), R.style.MyActiveTodo);
		}

		// set onClickListener for completed status checkbox
		viewHolder.cbCompleted.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				TodoItem.Status status;
				if (isChecked) {
					status = TodoItem.Status.DONE;
				} else {
					status = TodoItem.Status.ACTIVE;
				}
				TodoItem item = (TodoItem) (buttonView.getTag());
				item.setStatus(status);
				notifyDataSetChanged();
				// update todo item completion status in DB
				todoItemDAO.updateTodoItemCompletionStaus(item.getId(), status);
			}
		});
		return convertView;

	}

	@Override
	public long getItemId(int position) {
		return getItem(position).getId();
	}

	public void updateTodoListData() {
		todoItems = todoItemDAO.getTodoItemList();
		clear();
		addAll(todoItems);
	}

}