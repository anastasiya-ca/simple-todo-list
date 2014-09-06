package com.sunnydaycorp.simpletodolist;

public class TodoItem {

	private long id = 0;
	private String name;
	private int priority = 0;
	private Status status = Status.ACTIVE;

	public enum Status {
		ACTIVE, DONE, DELETED
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}
