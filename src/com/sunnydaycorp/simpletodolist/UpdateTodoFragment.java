package com.sunnydaycorp.simpletodolist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateTodoFragment extends DialogFragment {

	private OnTodoUpdatedListener onTodoUpdatedListener = null;
	private EditText etTodoName = null;
	private Context context = null;
	private long todoId = 0;
	private TodoItemDAO todoItemDAO = null;
	private InputMethodManager imm = null;

	public UpdateTodoFragment(Context context,
			OnTodoUpdatedListener onTodoUpdatedListener, long id) {
		this.onTodoUpdatedListener = onTodoUpdatedListener;
		this.todoId = id;
		this.context = context;
		this.todoItemDAO = new TodoItemDAO(context);

	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Create a new instance of Alert Dialog - UpdateTodoDialog
		// and return it

		imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);

		AlertDialog.Builder builder = new AlertDialog.Builder(context,
				R.style.MyAlertDialog);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View view = inflater.inflate(R.layout.dialog_update_todo, null);
		builder.setView(view);

		// set initial values
		etTodoName = (EditText) view.findViewById(R.id.etItemName);

		TodoItem todo = todoItemDAO.getTodoItemInfo(todoId);

		if (todo != null) {
			etTodoName.setText(todo.getName());
			etTodoName.setSelection(todo.getName().length(), todo.getName()
					.length());
			etTodoName.requestFocus();
			imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
		}

		etTodoName.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// if name is empty notify user
				if (etTodoName.getText().toString().trim().isEmpty()) {
					etTodoName.setError("Name can not be blank", getResources()
							.getDrawable(R.drawable.ic_action_error));
				} else {
					etTodoName.setError(null);
				}
			}

		});

		builder.setTitle(R.string.dialog_update_todo_title);
		builder.setPositiveButton(R.string.save_button_label,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						String name = etTodoName.getText().toString().trim();

						if (name.equals("")) {
							closeInputFromWindow();
							Toast.makeText(
									context,
									"Empty name is not allowed. No changes saved",
									Toast.LENGTH_SHORT).show();
							return;
						}

						todoItemDAO.updateTodoItem(todoId, name);
						closeInputFromWindow();
						onTodoUpdatedListener.onTodoUpdated(dialog, todoId);
					}
				});

		builder.setNegativeButton(R.string.cancel_button_label,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						closeInputFromWindow();

					}
				});

		return builder.create();

	}

	private void closeInputFromWindow() {
		imm.hideSoftInputFromWindow(etTodoName.getWindowToken(), 0);
	}

	@Override
	public void onStart() {
		super.onStart();

		int titleDividerId = getResources().getIdentifier("titleDivider", "id",
				"android");
		Dialog dialog = this.getDialog();
		View titleDividerView = dialog.findViewById(titleDividerId);
		if (titleDividerView != null) {
			titleDividerView.setBackgroundColor(getResources().getColor(
					R.color.custom_actionbar_color));
		}

	}

}
