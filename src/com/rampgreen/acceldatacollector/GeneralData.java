package com.rampgreen.acceldatacollector;

import java.util.HashMap;

public class GeneralData {
	private String action;
	private String task;
	private String parameters;

	public String getAction() {
		return action;
	}
	
	public void setAction(String action) {
		this.action = action;
	}
	
	public String getTask() {
		return task;
	}
	
	public void setTask(String task) {
		this.task = task;
	}
	
	public HashMap<String, String> getMap() {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("action", getAction());
		params.put("task", getTask());
		return params;
	}
}
