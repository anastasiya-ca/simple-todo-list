package com.sunnydaycorp.simpletodolist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

public class UpdateTodoFragment extends DialogFragment {

	private OnTodoUpdatedListener onTodoUpdatedListener;
	private EditText etTodoName;
	private Button btnSave;
	private long todoId;
	private TodoItemDAO todoItemDAO;
	private InputMethodManager imm;

	public UpdateTodoFragment() {
	}

	public static UpdateTodoFragment newInstance(long id) {
		UpdateTodoFragment updateTodoFragment = new UpdateTodoFragment();
		Bundle args = new Bundle();
		args.putLong("TODO_ID", id);
		updateTodoFragment.setArguments(args);
		return updateTodoFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		todoId = getArguments().getLong("TODO_ID", 0);
		imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		todoItemDAO = new TodoItemDAO(getActivity());
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Create a new instance of Alert Dialog - UpdateTodoDialog
		// and return it
		final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialog);
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_update_todo, null);
		builder.setView(view);

		// set initial values
		etTodoName = (EditText) view.findViewById(R.id.etItemName);
		TodoItem todo = todoItemDAO.getTodoItemInfo(todoId);

		if (todo != null) {
			etTodoName.setText(todo.getName());
			etTodoName.setSelection(todo.getName().length(), todo.getName().length());
			etTodoName.requestFocus();
			imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
		}

		etTodoName.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// if name is empty notify user
				if (etTodoName.getText().toString().trim().isEmpty()) {
					etTodoName.setError("Name can not be blank", getResources().getDrawable(R.drawable.ic_action_error));
					if (btnSave != null) {
						btnSave.setEnabled(false);
					}
				} else {
					etTodoName.setError(null);
					if (btnSave != null) {
						btnSave.setEnabled(true);
					}
				}

			}
		});

		builder.setTitle(R.string.dialog_update_todo_title);
		builder.setPositiveButton(R.string.save_button_label, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				String name = etTodoName.getText().toString().trim();

				if (!name.isEmpty()) {
					todoItemDAO.updateTodoItem(todoId, name);
					closeInputFromWindow();
					if (onTodoUpdatedListener != null) {
						onTodoUpdatedListener.onTodoUpdated(dialog, todoId);
					}
				}
			}
		});

		builder.setNegativeButton(R.string.cancel_button_label, new DialogInterface.OnClickListener() {

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

	public void setOnUpdateTodoListener(OnTodoUpdatedListener listener) {
		onTodoUpdatedListener = listener;
	}

	@Override
	public void onStart() {
		super.onStart();

		int titleDividerId = getResources().getIdentifier("titleDivider", "id", "android");
		Dialog dialog = this.getDialog();

		// update Title styling
		View titleDividerView = dialog.findViewById(titleDividerId);
		if (titleDividerView != null) {
			titleDividerView.setBackgroundColor(getResources().getColor(R.color.custom_actionbar_color));
		}

		// initialize btnSave
		btnSave = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);

	}

}
